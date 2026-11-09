package pos.api.auth;

import java.util.Map;

/** Standalone regression checks; no Google network call or database required. */
public class CustomerAuthTest {
   public static void main(String[] args) throws Exception {
      SessionTokens tokens = new SessionTokens("test-only-session-secret");
      GoogleTokenVerifier verifier = new GoogleTokenVerifier("test-client") {
         @Override
         public GoogleIdentity verify(String token) throws Exception {
            if (!"valid-google-token".equals(token)) throw new AuthException("Invalid Google token");
            return new GoogleIdentity("customer@example.com", "Customer");
         }
      };
      EmployeeDao employees = new EmployeeDao() {
         @Override
         public Employee findActiveByEmail(String email) {
            throw new AssertionError("Customer sign-in must not query employees");
         }
      };
      AuthService auth = new AuthService(verifier, employees, tokens);
      Map<String, Object> login = auth.loginCustomerWithGoogle("valid-google-token");
      String bearer = "Bearer " + login.get("token");
      Employee customer = auth.requireUser(bearer);
      check("customer".equals(customer.getRole()), "Customer role must survive session restore");
      check("customer@example.com".equals(customer.getEmail()), "Verified email must survive session restore");
      expectRejected(() -> auth.requireEmployee(bearer));
      expectRejected(() -> auth.requireUser(null));
      expectRejected(() -> auth.requireUser(bearer + "tampered"));
      expectRejected(() -> auth.loginCustomerWithGoogle("invalid"));
      for (String role : new String[] {"cashier", "manager"}) {
         String employeeToken = tokens.issue(new Employee(1, "employee@example.com", "Employee", role));
         check(role.equals(auth.requireEmployee(employeeToken).getRole()), "Employee access must still work");
      }
      System.out.println("Customer authentication regression checks passed.");
   }

   private static void check(boolean condition, String message) {
      if (!condition) throw new AssertionError(message);
   }

   private static void expectRejected(Action action) throws Exception {
      try {
         action.run();
      } catch (AuthException expected) {
         return;
      }
      throw new AssertionError("Expected authentication rejection");
   }

   private interface Action { void run() throws Exception; }
}
