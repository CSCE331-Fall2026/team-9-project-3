package pos.api;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pos.api.db.Database;
import pos.api.menu.MenuDao;
import pos.api.menu.MenuItem;

/**
 * Thin HTTP layer over Project 2 JDBC for Sprint 1 demos.
 */
public class App {
   public static void main(String[] args) {
      int port = readPort();
      MenuDao menuDao = new MenuDao();

      Javalin app = Javalin.create(config -> {
         config.bundledPlugins.enableCors(cors -> {
            cors.addRule(rule -> {
               rule.anyHost();
            });
         });
      });

      app.get("/health", App::health);
      app.get("/api/health", App::health);
      app.get("/api/menu", ctx -> menu(ctx, menuDao));

      app.start(port);
      System.out.println("Team 9 API listening on port " + port);
   }

   private static void health(Context ctx) {
      Map<String, Object> body = new HashMap<String, Object>();
      body.put("status", "ok");

      Connection conn = null;
      try {
         conn = Database.open();
         body.put("database", "connected");
         ctx.json(body);
      } catch (Exception e) {
         body.put("database", "error");
         body.put("message", e.getMessage());
         ctx.status(503).json(body);
      } finally {
         Database.close(conn);
      }
   }

   private static void menu(Context ctx, MenuDao menuDao) {
      try {
         List<MenuItem> items = menuDao.loadAvailable();
         ctx.json(items);
      } catch (Exception e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(500).json(error);
      }
   }

   private static int readPort() {
      String raw = System.getenv("PORT");
      if (raw == null || raw.isBlank()) {
         return 8080;
      }
      return Integer.parseInt(raw.trim());
   }
}
