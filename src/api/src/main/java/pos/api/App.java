package pos.api;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pos.api.auth.AuthException;
import pos.api.auth.AuthService;
import pos.api.auth.Employee;
import pos.api.auth.EmployeeDao;
import pos.api.auth.GoogleTokenVerifier;
import pos.api.auth.SessionTokens;
import pos.api.db.Database;
import pos.api.menu.MenuDao;
import pos.api.menu.MenuItem;
import pos.api.order.OrderDao;
import pos.api.order.OrderLine;

/**
 * Thin HTTP layer over Project 2 JDBC for Sprint 1 demos.
 */
public class App {
   public static void main(String[] args) {
      int port = readPort();
      MenuDao menuDao = new MenuDao();
      OrderDao orderDao = new OrderDao();
      AuthService authService = buildAuthService();

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

      app.post("/api/auth/google", ctx -> loginGoogle(ctx, authService));
      app.get("/api/auth/me", ctx -> me(ctx, authService));

      app.get("/api/cashier/home", ctx -> roleHome(ctx, authService, "cashier"));
      app.get("/api/manager/home", ctx -> roleHome(ctx, authService, "manager"));

      app.post("/api/orders", ctx -> createOrder(ctx, authService, orderDao));

      app.exception(AuthException.class, (e, ctx) -> {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(401).json(error);
      });

      app.start(port);
      System.out.println("Team 9 API listening on port " + port);
   }

   private static AuthService buildAuthService() {
      String googleClientId = envOrEmpty("GOOGLE_CLIENT_ID");
      String sessionSecret = envOrEmpty("SESSION_SECRET");
      if (sessionSecret.isBlank()) {
         sessionSecret = UUID.randomUUID().toString();
         System.out.println("WARNING: SESSION_SECRET not set; using a temporary secret for this process.");
      }
      return new AuthService(
         new GoogleTokenVerifier(googleClientId),
         new EmployeeDao(),
         new SessionTokens(sessionSecret));
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

   private static void loginGoogle(Context ctx, AuthService authService) {
      try {
         Map<?, ?> body = ctx.bodyAsClass(Map.class);
         Object rawToken = body == null ? null : body.get("idToken");
         String idToken = rawToken == null ? null : String.valueOf(rawToken);
         ctx.json(authService.loginWithGoogle(idToken));
      } catch (AuthException e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(401).json(error);
      } catch (Exception e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", "Could not complete Google sign-in: " + e.getMessage());
         ctx.status(500).json(error);
      }
   }

   private static void me(Context ctx, AuthService authService) throws AuthException {
      Employee employee = authService.requireEmployee(ctx.header("Authorization"));
      Map<String, Object> body = new HashMap<String, Object>();
      body.put("user", AuthService.userMap(employee));
      ctx.json(body);
   }

   private static void roleHome(Context ctx, AuthService authService, String requiredRole)
      throws AuthException {
      Employee employee = authService.requireEmployee(ctx.header("Authorization"));
      if (!requiredRole.equalsIgnoreCase(employee.getRole())) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", "Your role cannot open the " + requiredRole + " interface.");
         ctx.status(403).json(error);
         return;
      }

      Map<String, Object> body = new HashMap<String, Object>();
      body.put("message", "Welcome to the " + requiredRole + " POS.");
      body.put("user", AuthService.userMap(employee));
      ctx.json(body);
   }

   /**
    * Saves a new cashier order.
    * Body: {"items": [{"menuItemId": 2, "quantity": 1}, ...]}
    * Reply: {"orderId": 91501, "subtotal": 13.50, "lineCount": 2}
    */
   private static void createOrder(Context ctx, AuthService authService, OrderDao orderDao)
      throws AuthException {
      Employee employee = authService.requireEmployee(ctx.header("Authorization"));
      if (!"cashier".equalsIgnoreCase(employee.getRole())) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", "Only a cashier can create an order.");
         ctx.status(403).json(error);
         return;
      }

      try {
         List<OrderLine> lines = readOrderLines(ctx);
         ctx.status(201).json(orderDao.createOrder(lines));
      } catch (IllegalArgumentException e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(400).json(error);
      } catch (Exception e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(500).json(error);
      }
   }

   // Turns the JSON "items" list into OrderLine objects.
   private static List<OrderLine> readOrderLines(Context ctx) {
      Map<?, ?> body = ctx.bodyAsClass(Map.class);
      Object rawItems = body == null ? null : body.get("items");
      if (!(rawItems instanceof List)) {
         throw new IllegalArgumentException("Send an \"items\" list.");
      }

      List<OrderLine> lines = new ArrayList<OrderLine>();
      for (Object rawItem : (List<?>) rawItems) {
         if (!(rawItem instanceof Map)) {
            throw new IllegalArgumentException("Each item needs a menuItemId and quantity.");
         }
         Map<?, ?> item = (Map<?, ?>) rawItem;
         Object menuItemId = item.get("menuItemId");
         Object quantity = item.get("quantity");
         if (!(menuItemId instanceof Number) || !(quantity instanceof Number)) {
            throw new IllegalArgumentException("Each item needs a menuItemId and quantity.");
         }
         lines.add(new OrderLine(((Number) menuItemId).intValue(),
            ((Number) quantity).intValue()));
      }
      return lines;
   }

   private static int readPort() {
      String raw = System.getenv("PORT");
      if (raw == null || raw.isBlank()) {
         return 8080;
      }
      return Integer.parseInt(raw.trim());
   }

   private static String envOrEmpty(String name) {
      String value = System.getenv(name);
      return value == null ? "" : value.trim();
   }
}
