package pos.api.cart;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
