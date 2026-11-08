package pos.api.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pos.api.db.Database;

public class EmployeeDao {
   public Employee findActiveByEmail(String email) {
      if (email == null || email.isBlank()) {
         return null;
      }

      Connection conn = null;
      try {
         conn = Database.open();
         PreparedStatement stmt = conn.prepareStatement(
            "SELECT id, email, name, role FROM employee "
            + "WHERE LOWER(email) = LOWER(?) AND is_active = TRUE");
         stmt.setString(1, email.trim());
         ResultSet rows = stmt.executeQuery();
         Employee found = null;
         if (rows.next()) {
            found = new Employee(
               rows.getInt("id"),
               rows.getString("email"),
               rows.getString("name"),
               rows.getString("role"));
         }
         rows.close();
         stmt.close();
         return found;
      } catch (Exception e) {
         throw new RuntimeException("Could not look up employee: " + e.getMessage(), e);
      } finally {
         Database.close(conn);
      }
   }
}
