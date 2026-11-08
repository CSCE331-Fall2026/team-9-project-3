package pos.api.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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

   /**
    * Saves a new order and returns its id and subtotal.
    * Throws IllegalArgumentException when the order is empty or an item cannot be sold.
    */
   public SavedOrder createOrder(List<OrderLine> lines) {
      if (lines == null || lines.isEmpty()) {
         throw new IllegalArgumentException("The order has no items.");
      }

      Connection conn = null;
      try {
         conn = Database.open();
         conn.setAutoCommit(false);

         // Prices come from the database, not the browser.
         List<Double> prices = new ArrayList<Double>();
         double subtotal = 0.0;
         for (OrderLine line : lines) {
            if (line.getQuantity() < 1) {
               throw new IllegalArgumentException("Quantity must be at least 1.");
            }
            double price = findPrice(conn, line.getMenuItemId());
            prices.add(price);
            subtotal = subtotal + price * line.getQuantity();
         }
         subtotal = Math.round(subtotal * 100.0) / 100.0;

         int orderId = nextId(conn, "orders");
         insertOrder(conn, orderId, subtotal);
         insertOrderItems(conn, orderId, lines, prices);

         conn.commit();
         return new SavedOrder(orderId, subtotal, lines.size());
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

   // The current price of an item the cashier is allowed to sell.
   private double findPrice(Connection conn, int menuItemId) throws Exception {
      PreparedStatement stmt = conn.prepareStatement(
         "SELECT base_price FROM menu_item WHERE id = ? AND is_available = TRUE");
      stmt.setInt(1, menuItemId);
      ResultSet rows = stmt.executeQuery();

      if (!rows.next()) {
         rows.close();
         stmt.close();
         throw new IllegalArgumentException(
            "Menu item " + menuItemId + " does not exist or is not available.");
      }

      double price = rows.getDouble("base_price");
      rows.close();
      stmt.close();
      return price;
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

   private void insertOrderItems(Connection conn, int orderId, List<OrderLine> lines,
      List<Double> prices) throws Exception {
      PreparedStatement stmt = conn.prepareStatement(
         "INSERT INTO order_item (id, order_id, menu_item_id, quantity, custom, unit_price) "
         + "VALUES (?, ?, ?, ?, ?, ?)");

      int itemId = nextId(conn, "order_item");
      for (int i = 0; i < lines.size(); i++) {
         OrderLine line = lines.get(i);
         stmt.setInt(1, itemId);
         stmt.setInt(2, orderId);
         stmt.setInt(3, line.getMenuItemId());
         stmt.setInt(4, line.getQuantity());
         stmt.setNull(5, Types.VARCHAR);
         stmt.setDouble(6, prices.get(i));
         stmt.addBatch();
         itemId = itemId + 1;
      }

      stmt.executeBatch();
      stmt.close();
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
