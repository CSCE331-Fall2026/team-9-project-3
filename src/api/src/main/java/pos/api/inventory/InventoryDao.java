package pos.api.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pos.api.db.Database;

/** Read-only inventory queries against the configured live database. */
public class InventoryDao {
   public List<Map<String, Object>> loadAll() throws Exception {
      String sql = "SELECT id, store_id, name, storage_location, unit_of_measure, "
         + "qty_on_hand, reorder_threshold FROM inventory_item ORDER BY store_id, name, id";
      List<Map<String, Object>> items = new ArrayList<>();
      try (Connection conn = Database.open();
           PreparedStatement stmt = conn.prepareStatement(sql);
           ResultSet rows = stmt.executeQuery()) {
         while (rows.next()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", rows.getInt("id"));
            item.put("storeId", rows.getInt("store_id"));
            item.put("name", rows.getString("name"));
            item.put("storageLocation", rows.getString("storage_location"));
            item.put("unitOfMeasure", rows.getString("unit_of_measure"));
            item.put("qtyOnHand", rows.getDouble("qty_on_hand"));
            item.put("reorderThreshold", rows.getDouble("reorder_threshold"));
            items.add(item);
         }
      }
      return items;
   }
}
