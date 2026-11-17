package pos.api.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pos.api.db.Database;

/**
 * JDBC writes for a new cashier order. Same idea as Project 2 OrderDao.submitOrder:
 * the orders row and its order_item rows are saved together or not at all.
 */
public class OrderDao {
   private static final int STORE_ID = 1;
   private static final String ORDER_TYPE = "in-store";

   // Payment is a later story, so a new order is saved as open and not paid yet.
   private static final String STATUS_OPEN = "open";
   private static final String PAYMENT_PENDING = "pending";

   // sides, entrees. Keep this in step with src/web/src/lib/ticket.js.
   private static final Map<String, int[]> MEAL_SLOTS = new HashMap<String, int[]>();

   static {
      MEAL_SLOTS.put("Bowl", new int[] {1, 1});
      MEAL_SLOTS.put("Plate", new int[] {1, 2});
      MEAL_SLOTS.put("Bigger Plate", new int[] {1, 3});
      MEAL_SLOTS.put("Kids Meal", new int[] {1, 1});
      MEAL_SLOTS.put("A La Carte", new int[] {0, 1});
   }

   /**
    * Saves a new order and returns its id and subtotal.
    * Meal sides are stored at $0 because the size price already includes them.
    * Premium entrees keep their upcharge. Loose entrees and sizes are rejected.
    */
   public SavedOrder createOrder(List<OrderLine> extras, List<MealSelection> meals) {
      boolean noExtras = extras == null || extras.isEmpty();
      boolean noMeals = meals == null || meals.isEmpty();
      if (noExtras && noMeals) {
         throw new IllegalArgumentException("The order has no items.");
      }

      Connection conn = null;
      try {
         conn = Database.open();
         conn.setAutoCommit(false);

         List<PricedLine> priced = new ArrayList<PricedLine>();
         if (meals != null) {
            for (MealSelection meal : meals) {
               priced.addAll(priceMeal(conn, meal));
            }
         }
         if (extras != null) {
            for (OrderLine extra : extras) {
               priced.add(priceExtra(conn, extra));
            }
         }

         double subtotal = 0.0;
         for (PricedLine line : priced) {
            subtotal = subtotal + line.unitPrice * line.quantity;
         }
         subtotal = Math.round(subtotal * 100.0) / 100.0;

         int orderId = nextId(conn, "orders");
         insertOrder(conn, orderId, subtotal);
         insertOrderItems(conn, orderId, priced);

         conn.commit();
         return new SavedOrder(orderId, subtotal, priced.size());
      } catch (IllegalArgumentException e) {
         rollback(conn);
         throw e;
      } catch (Exception e) {
         rollback(conn);
         throw new RuntimeException("Could not save the order: " + e.getMessage(), e);
      } finally {
         restoreAutoCommit(conn);
         Database.close(conn);
      }
   }

   private List<PricedLine> priceMeal(Connection conn, MealSelection meal) throws Exception {
      SoldItem size = findItem(conn, meal.getSizeId());
      int[] slots = MEAL_SLOTS.get(size.name);
      if (!"Size".equals(size.category) || slots == null) {
         throw new IllegalArgumentException(size.name + " is not a meal size.");
      }
      if (meal.getSideIds().size() != slots[0] || meal.getEntreeIds().size() != slots[1]) {
         throw new IllegalArgumentException(size.name + " needs " + slots[0]
            + (slots[0] == 1 ? " side and " : " sides and ")
            + slots[1] + (slots[1] == 1 ? " entree." : " entrees."));
      }

      List<PricedLine> lines = new ArrayList<PricedLine>();
      lines.add(new PricedLine(size.id, 1, size.price));
      for (Integer sideId : meal.getSideIds()) {
         SoldItem side = findItem(conn, sideId.intValue());
         if (!"Side".equals(side.category)) {
            throw new IllegalArgumentException(side.name + " is not a side.");
         }
         lines.add(new PricedLine(side.id, 1, 0.0));
      }
      for (Integer entreeId : meal.getEntreeIds()) {
         SoldItem entree = findItem(conn, entreeId.intValue());
         if (!"Entree".equals(entree.category)) {
            throw new IllegalArgumentException(entree.name + " is not an entree.");
         }
         lines.add(new PricedLine(entree.id, 1, entree.price));
      }
      return lines;
   }

   private PricedLine priceExtra(Connection conn, OrderLine extra) throws Exception {
      if (extra.getQuantity() < 1) {
         throw new IllegalArgumentException("Quantity must be at least 1.");
      }
      SoldItem item = findItem(conn, extra.getMenuItemId());
      if ("Entree".equals(item.category) || "Size".equals(item.category)) {
         throw new IllegalArgumentException(
            item.name + " has to be part of a meal. Choose a size first.");
      }
      return new PricedLine(item.id, extra.getQuantity(), item.price);
   }

   private SoldItem findItem(Connection conn, int menuItemId) throws Exception {
      PreparedStatement stmt = conn.prepareStatement(
         "SELECT name, category, base_price FROM menu_item WHERE id = ? AND is_available = TRUE");
      stmt.setInt(1, menuItemId);
      ResultSet rows = stmt.executeQuery();

      if (!rows.next()) {
         rows.close();
         stmt.close();
         throw new IllegalArgumentException(
            "Menu item " + menuItemId + " does not exist or is not available.");
      }

      SoldItem item = new SoldItem(
         menuItemId,
         rows.getString("name"),
         rows.getString("category"),
         rows.getDouble("base_price"));
      rows.close();
      stmt.close();
      return item;
   }

   private void insertOrder(Connection conn, int orderId, double subtotal) throws Exception {
      PreparedStatement stmt = conn.prepareStatement(
         "INSERT INTO orders "
         + "(id, customer_id, store_id, order_type, status, total, discount, payment_method) "
         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      stmt.setInt(1, orderId);

      // Guest order. A cashier does not look up a customer to ring someone up.
      stmt.setNull(2, Types.INTEGER);
      stmt.setInt(3, STORE_ID);
      stmt.setString(4, ORDER_TYPE);
      stmt.setString(5, STATUS_OPEN);
      stmt.setDouble(6, subtotal);
      stmt.setDouble(7, 0.0);
      stmt.setString(8, PAYMENT_PENDING);
      stmt.executeUpdate();
      stmt.close();
   }

   private void insertOrderItems(Connection conn, int orderId, List<PricedLine> lines)
      throws Exception {
      PreparedStatement stmt = conn.prepareStatement(
         "INSERT INTO order_item (id, order_id, menu_item_id, quantity, custom, unit_price) "
         + "VALUES (?, ?, ?, ?, ?, ?)");

      int itemId = nextId(conn, "order_item");
      for (PricedLine line : lines) {
         stmt.setInt(1, itemId);
         stmt.setInt(2, orderId);
         stmt.setInt(3, line.menuItemId);
         stmt.setInt(4, line.quantity);
         stmt.setNull(5, Types.VARCHAR);
         stmt.setDouble(6, line.unitPrice);
         stmt.addBatch();
         itemId = itemId + 1;
      }

      stmt.executeBatch();
      stmt.close();
   }

   private static class SoldItem {
      private final int id;
      private final String name;
      private final String category;
      private final double price;

      private SoldItem(int id, String name, String category, double price) {
         this.id = id;
         this.name = name;
         this.category = category;
         this.price = price;
      }
   }

   private static class PricedLine {
      private final int menuItemId;
      private final int quantity;
      private final double unitPrice;

      private PricedLine(int menuItemId, int quantity, double unitPrice) {
         this.menuItemId = menuItemId;
         this.quantity = quantity;
         this.unitPrice = unitPrice;
      }
   }

   // Our tables have no sequences, so the next id is MAX(id) + 1, the same as Project 2.
   // The table name only ever comes from this class, never from the browser.
   private int nextId(Connection conn, String table) throws Exception {
      PreparedStatement stmt =
         conn.prepareStatement("SELECT COALESCE(MAX(id), 0) + 1 FROM " + table);
      ResultSet rows = stmt.executeQuery();
      rows.next();
      int id = rows.getInt(1);
      rows.close();
      stmt.close();
      return id;
   }

   private void rollback(Connection conn) {
      if (conn == null) {
         return;
      }
      try {
         conn.rollback();
      } catch (Exception e) {
         System.out.println("Rollback failed: " + e.getMessage());
      }
   }

   private void restoreAutoCommit(Connection conn) {
      if (conn == null) {
         return;
      }
      try {
         conn.setAutoCommit(true);
      } catch (Exception e) {
         System.out.println("Could not reset auto-commit: " + e.getMessage());
      }
   }
}
