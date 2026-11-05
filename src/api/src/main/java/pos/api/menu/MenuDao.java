package pos.api.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import pos.api.db.Database;

/**
 * JDBC reads against menu_item. Same query shape as Project 2 MenuDao.
 */
public class MenuDao {
   private static final String SELECT_COLUMNS =
      "SELECT id, name, category, base_price, is_available FROM menu_item";

   public List<MenuItem> loadAvailable() {
      return queryList(SELECT_COLUMNS + " WHERE is_available = TRUE ORDER BY category, name");
   }

   private List<MenuItem> queryList(String sql) {
      List<MenuItem> items = new ArrayList<MenuItem>();
      Connection conn = null;

      try {
         conn = Database.open();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rows = stmt.executeQuery();
         while (rows.next()) {
            items.add(new MenuItem(
               rows.getInt("id"),
               rows.getString("name"),
               rows.getString("category"),
               rows.getDouble("base_price"),
               rows.getBoolean("is_available")));
         }
         rows.close();
         stmt.close();
      } catch (Exception e) {
         throw new RuntimeException("Could not load menu items: " + e.getMessage(), e);
      } finally {
         Database.close(conn);
      }

      return items;
   }
}
