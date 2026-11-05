package pos.api.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Opens JDBC connections for the API.
 * Prefer DATABASE_URL (Neon / Render). Fall back to local/db.txt for laptop work
 * against either Neon or the old Project 2 host.
 */
public final class Database {
   private Database() {
   }

   public static Connection open() throws Exception {
      String databaseUrl = System.getenv("DATABASE_URL");
      if (databaseUrl != null && !databaseUrl.isBlank()) {
         return openFromDatabaseUrl(databaseUrl.trim());
      }

      return openFromLocalFile();
   }

   public static void close(Connection conn) {
      if (conn == null) {
         return;
      }

      try {
         conn.close();
      } catch (Exception e) {
         System.out.println("Could not close the database connection.");
      }
   }

   private static Connection openFromDatabaseUrl(String databaseUrl) throws Exception {
      // Neon / Render often give postgres://user:pass@host/db?sslmode=require
      String jdbcUrl = databaseUrl;
      if (jdbcUrl.startsWith("postgres://") || jdbcUrl.startsWith("postgresql://")) {
         URI uri = URI.create(jdbcUrl);
         String userInfo = uri.getUserInfo();
         if (userInfo == null || !userInfo.contains(":")) {
            throw new Exception("DATABASE_URL must include user:password");
         }

         String user = userInfo.substring(0, userInfo.indexOf(':'));
         String password = userInfo.substring(userInfo.indexOf(':') + 1);
         String host = uri.getHost();
         int port = uri.getPort() > 0 ? uri.getPort() : 5432;
         String path = uri.getPath();
         String query = uri.getQuery();
         StringBuilder jdbc = new StringBuilder();
         jdbc.append("jdbc:postgresql://").append(host).append(':').append(port).append(path);
         if (query != null && !query.isBlank()) {
            jdbc.append('?').append(query);
         } else {
            jdbc.append("?sslmode=require");
         }
         return DriverManager.getConnection(jdbc.toString(), user, password);
      }

      if (jdbcUrl.startsWith("jdbc:postgresql://")) {
         return DriverManager.getConnection(jdbcUrl);
      }

      throw new Exception("DATABASE_URL must be a postgres:// or jdbc:postgresql:// URL");
   }

   private static Connection openFromLocalFile() throws Exception {
      Map<String, String> values = readLocalDbFile();
      String url = values.getOrDefault(
         "url",
         "jdbc:postgresql://csce-315-db.engr.tamu.edu:5432/team9db");
      String user = values.getOrDefault("user", "");
      String password = values.getOrDefault("password", "");

      if (user.isEmpty() || password.isEmpty()) {
         throw new Exception(
            "Set DATABASE_URL, or put user=/password= (and optional url=) in local/db.txt");
      }

      return DriverManager.getConnection(url, user, password);
   }

   private static Map<String, String> readLocalDbFile() throws Exception {
      Map<String, String> values = new HashMap<String, String>();
      // Resolve from repo root when running locally; also try cwd for flexibility.
      String[] candidates = {
         "local/db.txt",
         "../../local/db.txt",
         "../../../local/db.txt"
      };

      Exception last = null;
      for (String path : candidates) {
         try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            try {
               String line = reader.readLine();
               while (line != null) {
                  int eq = line.indexOf('=');
                  if (eq > 0) {
                     String key = line.substring(0, eq).trim();
                     String value = line.substring(eq + 1).trim();
                     values.put(key, value);
                  }
                  line = reader.readLine();
               }
            } finally {
               reader.close();
            }
            return values;
         } catch (Exception e) {
            last = e;
         }
      }

      throw new Exception("Could not read local/db.txt: " + (last == null ? "" : last.getMessage()));
   }
}
