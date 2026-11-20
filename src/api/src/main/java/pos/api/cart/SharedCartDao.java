package pos.api.cart;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pos.api.db.Database;

/**
 * JDBC reads and writes for shared_cart. Creating a cart never touches the orders table,
 * so making a shared cart does not submit an order.
 */
public class SharedCartDao {
   public static final String STATUS_OPEN = "open";

   // No 0/O or 1/I/L, so a code read out loud or typed from a phone is hard to get wrong.
   private static final String CODE_LETTERS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
   private static final int CODE_LENGTH = 6;
   private static final int MAX_CODE_TRIES = 10;

   private static final String SELECT_COLUMNS =
      "SELECT id, join_code, host_email, host_name, status, created_at FROM shared_cart";

   private final Random random = new SecureRandom();

   /**
    * The open shared cart this customer is hosting, or null if they have none.
    * The host's page uses this after a refresh to find their cart again.
    */
   public SharedCart findOpenByHost(String hostEmail) {
      Connection conn = null;
      try {
         conn = Database.open();
         PreparedStatement stmt = conn.prepareStatement(
            SELECT_COLUMNS + " WHERE LOWER(host_email) = LOWER(?) AND status = ? "
            + "ORDER BY id DESC LIMIT 1");
         stmt.setString(1, hostEmail);
         stmt.setString(2, STATUS_OPEN);
         ResultSet rows = stmt.executeQuery();

         SharedCart found = null;
         if (rows.next()) {
            found = readRow(rows);
         }
         rows.close();
         stmt.close();
         return found;
      } catch (Exception e) {
         throw new RuntimeException("Could not look up your shared cart: " + e.getMessage(), e);
      } finally {
         Database.close(conn);
      }
   }

   /**
    * The open shared cart with this join code, or null if there is none (wrong or closed code).
    * Codes are matched ignoring case and spaces, so "k7qm4p " finds "K7QM4P".
    */
   public SharedCart findOpenByCode(String joinCode) {
      if (joinCode == null) {
         return null;
      }
      Connection conn = null;
      try {
         conn = Database.open();
         PreparedStatement stmt = conn.prepareStatement(
            SELECT_COLUMNS + " WHERE join_code = ? AND status = ?");
         stmt.setString(1, joinCode.trim().toUpperCase());
         stmt.setString(2, STATUS_OPEN);
         ResultSet rows = stmt.executeQuery();

         SharedCart found = null;
         if (rows.next()) {
            found = readRow(rows);
         }
         rows.close();
         stmt.close();
         return found;
      } catch (Exception e) {
         throw new RuntimeException("Could not look up that join code: " + e.getMessage(), e);
      } finally {
         Database.close(conn);
      }
   }

   /**
    * Adds a menu item to a cart. Adding the same item again by the same person bumps the
    * quantity instead of making a second line. Returns false if the item is not on the menu
    * or is not available.
    */
   public boolean addItem(int cartId, int menuItemId, String email, String name) {
      Connection conn = null;
      try {
         conn = Database.open();

         // 1. Only items that are on the menu and available can be added.
         PreparedStatement check = conn.prepareStatement(
            "SELECT 1 FROM menu_item WHERE id = ? AND is_available = TRUE");
         check.setInt(1, menuItemId);
         ResultSet found = check.executeQuery();
         boolean available = found.next();
         found.close();
         check.close();
         if (!available) {
            return false;
         }

         // 2. Add a new line, or add 1 to the quantity if this person already added this item.
         // The unique index on (cart, item, person) makes this safe even if two taps arrive
         // at the same moment.
         PreparedStatement insert = conn.prepareStatement(
            "INSERT INTO shared_cart_item (shared_cart_id, menu_item_id, added_by_email, "
            + "added_by_name) VALUES (?, ?, ?, ?) "
            + "ON CONFLICT (shared_cart_id, menu_item_id, LOWER(added_by_email)) "
            + "DO UPDATE SET quantity = shared_cart_item.quantity + 1");
         insert.setInt(1, cartId);
         insert.setInt(2, menuItemId);
         insert.setString(3, email);
         insert.setString(4, name);
         insert.executeUpdate();
         insert.close();
         return true;
      } catch (Exception e) {
         throw new RuntimeException("Could not add that item: " + e.getMessage(), e);
      } finally {
         Database.close(conn);
      }
   }

   /**
    * Every line in a cart, oldest first, with the item's name and current price.
    */
   public List<SharedCartItem> loadItems(int cartId) {
      List<SharedCartItem> items = new ArrayList<SharedCartItem>();
      Connection conn = null;
      try {
         conn = Database.open();
         PreparedStatement stmt = conn.prepareStatement(
            "SELECT i.id, i.menu_item_id, m.name, m.base_price, i.quantity, "
            + "i.added_by_email, i.added_by_name "
            + "FROM shared_cart_item i JOIN menu_item m ON m.id = i.menu_item_id "
            + "WHERE i.shared_cart_id = ? ORDER BY i.id");
         stmt.setInt(1, cartId);
         ResultSet rows = stmt.executeQuery();
         while (rows.next()) {
            items.add(new SharedCartItem(
               rows.getInt("id"),
               rows.getInt("menu_item_id"),
               rows.getString("name"),
               rows.getDouble("base_price"),
               rows.getInt("quantity"),
               rows.getString("added_by_email"),
               rows.getString("added_by_name")));
         }
         rows.close();
         stmt.close();
         return items;
      } catch (Exception e) {
         throw new RuntimeException("Could not load the cart items: " + e.getMessage(), e);
      } finally {
         Database.close(conn);
      }
   }

   /**
    * Creates a new open shared cart with a join code no other cart is using.
    */
   public SharedCart create(String hostEmail, String hostName) {
      Connection conn = null;
      try {
         conn = Database.open();
         String code = unusedJoinCode(conn);

         PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO shared_cart (join_code, host_email, host_name, status) "
            + "VALUES (?, ?, ?, ?) "
            + "RETURNING id, join_code, host_email, host_name, status, created_at");
         stmt.setString(1, code);
         stmt.setString(2, hostEmail);
         stmt.setString(3, hostName);
         stmt.setString(4, STATUS_OPEN);
         ResultSet rows = stmt.executeQuery();
         rows.next();
         SharedCart cart = readRow(rows);
         rows.close();
         stmt.close();
         return cart;
      } catch (Exception e) {
         throw new RuntimeException("Could not create the shared cart: " + e.getMessage(), e);
      } finally {
         Database.close(conn);
      }
   }

   /**
    * A random code like "K7QM4P". Static so it can be checked without a database.
    */
   public static String newJoinCode(Random random) {
      StringBuilder code = new StringBuilder();
      for (int i = 0; i < CODE_LENGTH; i++) {
         code.append(CODE_LETTERS.charAt(random.nextInt(CODE_LETTERS.length())));
      }
      return code.toString();
   }

   // Picks codes until one is free. With 31^6 (about 887 million) codes a repeat is very rare.
   // join_code is also UNIQUE in the table, so two carts can never end up sharing one.
   private String unusedJoinCode(Connection conn) throws Exception {
      for (int i = 0; i < MAX_CODE_TRIES; i++) {
         String code = newJoinCode(random);
         if (!codeExists(conn, code)) {
            return code;
         }
      }
      throw new Exception("Could not find a free join code. Please try again.");
   }

   private boolean codeExists(Connection conn, String code) throws Exception {
      PreparedStatement stmt =
         conn.prepareStatement("SELECT 1 FROM shared_cart WHERE join_code = ?");
      stmt.setString(1, code);
      ResultSet rows = stmt.executeQuery();
      boolean exists = rows.next();
      rows.close();
      stmt.close();
      return exists;
   }

   private SharedCart readRow(ResultSet rows) throws Exception {
      return new SharedCart(
         rows.getInt("id"),
         rows.getString("join_code"),
         rows.getString("host_email"),
         rows.getString("host_name"),
         rows.getString("status"),
         String.valueOf(rows.getTimestamp("created_at")));
   }
}
