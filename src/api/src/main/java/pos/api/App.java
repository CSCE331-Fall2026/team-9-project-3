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
import pos.api.cart.SharedCart;
import pos.api.cart.SharedCartDao;
import pos.api.db.Database;
import pos.api.inventory.InventoryDao;
import pos.api.menu.MenuDao;
import pos.api.menu.MenuItem;
import pos.api.order.MealSelection;
import pos.api.order.OrderDao;
import pos.api.order.OrderLine;
import pos.api.order.SalesSummaryDao;

/**
 * Thin HTTP layer over Project 2 JDBC for Sprint 1 demos.
 */
public class App {
   public static void main(String[] args) {
      int port = readPort();
      MenuDao menuDao = new MenuDao();
      OrderDao orderDao = new OrderDao();
      InventoryDao inventoryDao = new InventoryDao();
      SalesSummaryDao salesSummaryDao = new SalesSummaryDao();
      SharedCartDao sharedCartDao = new SharedCartDao();
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

      app.post("/api/auth/google", ctx -> loginGoogle(ctx, authService, false));
      app.post("/api/auth/customer/google", ctx -> loginGoogle(ctx, authService, true));
      app.get("/api/auth/me", ctx -> me(ctx, authService));

      app.get("/api/cashier/home", ctx -> roleHome(ctx, authService, "cashier"));
      app.get("/api/manager/home", ctx -> roleHome(ctx, authService, "manager"));

      app.get("/api/manager/inventory", ctx -> inventory(ctx, authService, inventoryDao));
      app.get("/api/manager/sales-summary",
         ctx -> salesSummary(ctx, authService, salesSummaryDao));

      app.post("/api/orders", ctx -> createOrder(ctx, authService, orderDao, false));
      app.post("/api/customer/orders", ctx -> createOrder(ctx, authService, orderDao, true));

      app.post("/api/shared-carts", ctx -> createSharedCart(ctx, authService, sharedCartDao));
      app.get("/api/shared-carts/mine", ctx -> mySharedCart(ctx, authService, sharedCartDao));
      app.post("/api/shared-carts/join", ctx -> joinSharedCart(ctx, authService, sharedCartDao));
      app.get("/api/shared-carts/{code}", ctx -> viewSharedCart(ctx, authService, sharedCartDao));
      app.post("/api/shared-carts/{code}/items",
         ctx -> addSharedCartItem(ctx, authService, sharedCartDao));

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

   private static void loginGoogle(Context ctx, AuthService authService, boolean customer) {
      try {
         Map<?, ?> body = ctx.bodyAsClass(Map.class);
         Object rawToken = body == null ? null : body.get("idToken");
         String idToken = rawToken == null ? null : String.valueOf(rawToken);
         ctx.json(customer ? authService.loginCustomerWithGoogle(idToken) : authService.loginWithGoogle(idToken));
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
      Employee employee = authService.requireUser(ctx.header("Authorization"));
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
    * Order count and sales total for the last 1, 7, or 30 days, plus the latest orders.
    * Example: GET /api/manager/sales-summary?days=7
    * Reply: {"days": 7, "orderCount": 12, "salesTotal": 154.5, "averageOrder": 12.88,
    *         "recentOrders": [{"id": 14, "orderType": "in-store", "status": "open",
    *         "total": 10.5, "minutesAgo": 5}, ...]}
    */
   private static void salesSummary(Context ctx, AuthService authService,
      SalesSummaryDao salesSummaryDao) throws AuthException {
      Employee employee = authService.requireEmployee(ctx.header("Authorization"));
      if (!"manager".equalsIgnoreCase(employee.getRole())) {
         ctx.status(403).json(Map.of("error", "Only a manager can view the sales summary."));
         return;
      }

      String rawDays = ctx.queryParam("days");
      int days = 1;
      if (rawDays != null && !rawDays.isBlank()) {
         if (!rawDays.equals("1") && !rawDays.equals("7") && !rawDays.equals("30")) {
            ctx.status(400).json(Map.of("error", "days must be 1, 7, or 30."));
            return;
         }
         days = Integer.parseInt(rawDays);
      }

      ctx.header("Cache-Control", "no-store");
      try {
         ctx.json(salesSummaryDao.loadSummary(days));
      } catch (Exception e) {
         ctx.status(500).json(Map.of("error", "Could not load the sales summary. Please try again."));
         System.err.println("Could not load sales summary: " + e.getMessage());
      }
   }

   private static void inventory(Context ctx, AuthService authService, InventoryDao inventoryDao)
      throws AuthException {
      Employee employee = authService.requireEmployee(ctx.header("Authorization"));
      if (!"manager".equalsIgnoreCase(employee.getRole())) {
         ctx.status(403).json(Map.of("error", "Only a manager can view inventory."));
         return;
      }

      ctx.header("Cache-Control", "no-store");
      try {
         ctx.json(inventoryDao.loadAll());
      } catch (Exception e) {
         ctx.status(500).json(Map.of("error", "Could not load inventory. Please try again."));
         System.err.println("Could not load inventory: " + e.getMessage());
      }
   }

   /**
    * Saves a cashier order or personal pickup order.
    * Body: {"meals":[{"sizeId":2,"sideIds":[20],"entreeIds":[8,14]}],
    *        "items":[{"menuItemId":4,"quantity":1}]}
    * Reply: {"orderId": 91501, "subtotal": 13.50, "lineCount": 5}
    */
   private static void createOrder(Context ctx, AuthService authService, OrderDao orderDao,
      boolean pickup) throws AuthException {
      String authorization = ctx.header("Authorization");
      Employee user = pickup ? authService.requireUser(authorization)
         : authService.requireEmployee(authorization);
      String requiredRole = pickup ? "customer" : "cashier";
      if (!requiredRole.equalsIgnoreCase(user.getRole())) {
         ctx.status(403).json(Map.of("error", "Only a " + requiredRole + " can create this order."));
         return;
      }
      if (pickup && user.getGoogleSubject() == null) {
         throw new AuthException("Sign out and sign in again with Google to place a pickup order.");
      }

      Map<?, ?> body;
      try {
         body = ctx.bodyAsClass(Map.class);
      } catch (Exception e) {
         ctx.status(400).json(Map.of("error", "Send a valid JSON order."));
         return;
      }

      try {
         List<MealSelection> meals = readMeals(body);
         List<OrderLine> extras = readOrderLines(body);
         ctx.status(201).json(pickup ? orderDao.createPickupOrder(extras, meals, user)
            : orderDao.createOrder(extras, meals));
      } catch (IllegalArgumentException e) {
         ctx.status(400).json(Map.of("error", e.getMessage()));
      } catch (Exception e) {
         ctx.status(500).json(Map.of("error", "Could not save the order. Please try again."));
         System.err.println("Could not save the order: " + e.getMessage());
      }
   }

   /**
    * Creates a shared cart for the signed-in customer and returns its join code.
    * If they already host an open cart, that one is returned instead of making another.
    * Reply: {"cart": {"id": 1, "joinCode": "K7QM4P", "hostName": "...", "status": "open", ...}}
    */
   private static void createSharedCart(Context ctx, AuthService authService,
      SharedCartDao sharedCartDao) throws AuthException {
      Employee customer = requireCustomer(ctx, authService);
      if (customer == null) {
         return;
      }

      try {
         SharedCart cart = sharedCartDao.findOpenByHost(customer.getEmail());
         if (cart == null) {
            cart = sharedCartDao.create(customer.getEmail(), customer.getName());
            ctx.status(201);
         }
         Map<String, Object> body = new HashMap<String, Object>();
         body.put("cart", cart);
         ctx.json(body);
      } catch (Exception e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(500).json(error);
      }
   }

   /**
    * The signed-in customer's open shared cart, so it is still there after a refresh.
    * Reply: {"cart": {...}} or {"cart": null} when they have not made one.
    */
   private static void mySharedCart(Context ctx, AuthService authService,
      SharedCartDao sharedCartDao) throws AuthException {
      Employee customer = requireCustomer(ctx, authService);
      if (customer == null) {
         return;
      }

      try {
         Map<String, Object> body = new HashMap<String, Object>();
         body.put("cart", sharedCartDao.findOpenByHost(customer.getEmail()));
         ctx.json(body);
      } catch (Exception e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(500).json(error);
      }
   }

   /**
    * A customer joins a shared cart with the code the host gave them.
    * Body: {"joinCode": "K7QM4P"}
    * Reply: {"cart": {...}, "items": [...]}, or 404 with an error for a wrong or closed code.
    */
   private static void joinSharedCart(Context ctx, AuthService authService,
      SharedCartDao sharedCartDao) throws AuthException {
      Employee customer = requireCustomer(ctx, authService);
      if (customer == null) {
         return;
      }

      try {
         Map<?, ?> body = ctx.bodyAsClass(Map.class);
         Object rawCode = body == null ? null : body.get("joinCode");
         String code = rawCode == null ? "" : String.valueOf(rawCode);
         sendSharedCart(ctx, sharedCartDao, sharedCartDao.findOpenByCode(code));
      } catch (Exception e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(500).json(error);
      }
   }

   /**
    * The current cart and its items, for refreshing or polling. Same reply as joining.
    */
   private static void viewSharedCart(Context ctx, AuthService authService,
      SharedCartDao sharedCartDao) throws AuthException {
      Employee customer = requireCustomer(ctx, authService);
      if (customer == null) {
         return;
      }

      try {
         sendSharedCart(ctx, sharedCartDao, sharedCartDao.findOpenByCode(ctx.pathParam("code")));
      } catch (Exception e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(500).json(error);
      }
   }

   /**
    * Adds one of a menu item to the cart. Works for the host and for people who joined.
    * Body: {"menuItemId": 24}
    * Reply: the cart and its items, like joining. 404 for a wrong or closed code, 400 when the
    * item is missing or not available.
    */
   private static void addSharedCartItem(Context ctx, AuthService authService,
      SharedCartDao sharedCartDao) throws AuthException {
      Employee customer = requireCustomer(ctx, authService);
      if (customer == null) {
         return;
      }

      try {
         SharedCart cart = sharedCartDao.findOpenByCode(ctx.pathParam("code"));
         if (cart == null) {
            sendSharedCart(ctx, sharedCartDao, null);
            return;
         }

         Map<?, ?> body = ctx.bodyAsClass(Map.class);
         Object rawId = body == null ? null : body.get("menuItemId");
         if (!(rawId instanceof Number)) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "Pick a menu item to add.");
            ctx.status(400).json(error);
            return;
         }

         if (!sharedCartDao.addItem(cart.getId(), ((Number) rawId).intValue(),
            customer.getEmail(), customer.getName())) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "That item is not available.");
            ctx.status(400).json(error);
            return;
         }
         sendSharedCart(ctx, sharedCartDao, cart);
      } catch (Exception e) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", e.getMessage());
         ctx.status(500).json(error);
      }
   }

   // Sends the cart and its items, or a 404 when the code did not match an open cart.
   private static void sendSharedCart(Context ctx, SharedCartDao sharedCartDao, SharedCart cart) {
      if (cart == null) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", "That code did not match an open shared cart. Check it and try again.");
         ctx.status(404).json(error);
         return;
      }
      Map<String, Object> body = new HashMap<String, Object>();
      body.put("cart", cart);
      body.put("items", sharedCartDao.loadItems(cart.getId()));
      ctx.json(body);
   }

   // Signed-in customer, or null after sending a 403 when an employee calls a customer route.
   private static Employee requireCustomer(Context ctx, AuthService authService)
      throws AuthException {
      Employee user = authService.requireUser(ctx.header("Authorization"));
      if (!"customer".equalsIgnoreCase(user.getRole())) {
         Map<String, String> error = new HashMap<String, String>();
         error.put("error", "Only a signed-in customer can use a shared cart.");
         ctx.status(403).json(error);
         return null;
      }
      return user;
   }

   private static List<MealSelection> readMeals(Map<?, ?> body) {
      Object rawMeals = body == null ? null : body.get("meals");
      List<MealSelection> meals = new ArrayList<MealSelection>();
      if (rawMeals == null) {
         return meals;
      }
      if (!(rawMeals instanceof List)) {
         throw new IllegalArgumentException("\"meals\" must be a list.");
      }

      for (Object rawMeal : (List<?>) rawMeals) {
         if (!(rawMeal instanceof Map)) {
            throw new IllegalArgumentException("Each meal needs a sizeId, sideIds, and entreeIds.");
         }
         Map<?, ?> meal = (Map<?, ?>) rawMeal;
         Object sizeId = meal.get("sizeId");
         if (!(sizeId instanceof Number)) {
            throw new IllegalArgumentException("Each meal needs a sizeId.");
         }
         meals.add(new MealSelection(
            ((Number) sizeId).intValue(),
            readIdList(meal.get("sideIds"), "sideIds"),
            readIdList(meal.get("entreeIds"), "entreeIds")));
      }
      return meals;
   }

   private static List<Integer> readIdList(Object raw, String label) {
      if (!(raw instanceof List)) {
         throw new IllegalArgumentException("Each meal needs " + label + ".");
      }
      List<Integer> ids = new ArrayList<Integer>();
      for (Object value : (List<?>) raw) {
         if (!(value instanceof Number)) {
            throw new IllegalArgumentException(label + " must be menu item ids.");
         }
         ids.add(Integer.valueOf(((Number) value).intValue()));
      }
      return ids;
   }

   // Turns the JSON "items" list into extras (apps, drinks, a la carte sides).
   private static List<OrderLine> readOrderLines(Map<?, ?> body) {
      Object rawItems = body == null ? null : body.get("items");
      List<OrderLine> lines = new ArrayList<OrderLine>();
      if (rawItems == null) {
         return lines;
      }
      if (!(rawItems instanceof List)) {
         throw new IllegalArgumentException("\"items\" must be a list.");
      }

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
