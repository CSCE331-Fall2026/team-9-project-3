package pos.api.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pos.api.db.Database;

/**
 * Read-only sales summary for the manager: order count and sales total for the last N days,
 * plus the most recent orders in that window.
 *
 * The window is measured with the database's own clock (LOCALTIMESTAMP), the same clock that
 * stamps orders.created_at. Neon runs in UTC, so this avoids "today" ending at 6 PM Texas time.
 */
public class SalesSummaryDao {
   private static final int RECENT_LIMIT = 10;

   private static final String SUMMARY_SQL =
      "SELECT COUNT(*) AS order_count, COALESCE(SUM(total), 0) AS sales_total "
      + "FROM orders "
      + "WHERE created_at >= LOCALTIMESTAMP - (? * INTERVAL '1 day')";

   // GREATEST(0, ...) so a small clock difference can never show a negative "minutes ago".
   private static final String RECENT_SQL =
      "SELECT id, order_type, status, total, "
      + "GREATEST(0, FLOOR(EXTRACT(EPOCH FROM (LOCALTIMESTAMP - created_at)) / 60)) "
      + "AS minutes_ago "
      + "FROM orders "
      + "WHERE created_at >= LOCALTIMESTAMP - (? * INTERVAL '1 day') "
      + "ORDER BY created_at DESC, id DESC "
      + "LIMIT ?";

   public Map<String, Object> loadSummary(int days) throws Exception {
      Map<String, Object> summary = new LinkedHashMap<>();
      summary.put("days", days);

      try (Connection conn = Database.open()) {
         int orderCount = 0;
         double salesTotal = 0.0;
         try (PreparedStatement stmt = conn.prepareStatement(SUMMARY_SQL)) {
            stmt.setInt(1, days);
            try (ResultSet rows = stmt.executeQuery()) {
               rows.next();
               orderCount = rows.getInt("order_count");
               salesTotal = rows.getDouble("sales_total");
            }
         }

         double average = orderCount == 0 ? 0.0 : salesTotal / orderCount;
         summary.put("orderCount", orderCount);
         summary.put("salesTotal", roundToCents(salesTotal));
         summary.put("averageOrder", roundToCents(average));
         summary.put("recentOrders", loadRecent(conn, days));
      }
      return summary;
   }

   private List<Map<String, Object>> loadRecent(Connection conn, int days) throws Exception {
      List<Map<String, Object>> orders = new ArrayList<>();
      try (PreparedStatement stmt = conn.prepareStatement(RECENT_SQL)) {
         stmt.setInt(1, days);
         stmt.setInt(2, RECENT_LIMIT);
         try (ResultSet rows = stmt.executeQuery()) {
            while (rows.next()) {
               Map<String, Object> order = new LinkedHashMap<>();
               order.put("id", rows.getInt("id"));
               order.put("orderType", rows.getString("order_type"));
               order.put("status", rows.getString("status"));
               order.put("total", roundToCents(rows.getDouble("total")));
               order.put("minutesAgo", rows.getInt("minutes_ago"));
               orders.add(order);
            }
         }
      }
      return orders;
   }

   // Totals are FLOAT in the schema, so round before sending them to the screen.
   private static double roundToCents(double amount) {
      return Math.round(amount * 100.0) / 100.0;
   }
}
