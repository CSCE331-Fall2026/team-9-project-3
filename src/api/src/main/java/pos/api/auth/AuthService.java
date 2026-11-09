package pos.api.auth;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
   private final GoogleTokenVerifier googleTokenVerifier;
   private final EmployeeDao employeeDao;
   private final SessionTokens sessionTokens;

   public AuthService(
      GoogleTokenVerifier googleTokenVerifier,
      EmployeeDao employeeDao,
      SessionTokens sessionTokens) {
      this.googleTokenVerifier = googleTokenVerifier;
      this.employeeDao = employeeDao;
      this.sessionTokens = sessionTokens;
   }

   public Map<String, Object> loginWithGoogle(String idToken) throws Exception {
      GoogleTokenVerifier.GoogleIdentity identity = googleTokenVerifier.verify(idToken);
      Employee employee = employeeDao.findActiveByEmail(identity.getEmail());
      if (employee == null) {
         throw new AuthException(
            "This Google account is not linked to an employee role. Ask a manager to add "
            + identity.getEmail() + " to the employee list.");
      }

      String token = sessionTokens.issue(employee);
      Map<String, Object> body = new HashMap<String, Object>();
      body.put("token", token);
      body.put("user", userMap(employee));
      return body;
   }

   // Customer browsing needs a verified identity, not an employee database record.
   // ID 0 is a session placeholder, not a persisted customer ID or order identity.
   public Map<String, Object> loginCustomerWithGoogle(String idToken) throws Exception {
      GoogleTokenVerifier.GoogleIdentity identity = googleTokenVerifier.verify(idToken);
      Employee customer = new Employee(0, identity.getEmail(), identity.getName(), "customer");
      Map<String, Object> body = new HashMap<>();
      body.put("token", sessionTokens.issue(customer));
      body.put("user", userMap(customer));
      return body;
   }

   public Employee requireEmployee(String authorizationHeader) throws AuthException {
      Employee user = requireUser(authorizationHeader);
      if (!"cashier".equalsIgnoreCase(user.getRole()) && !"manager".equalsIgnoreCase(user.getRole())) {
         throw new AuthException("Employee sign-in required.");
      }
      return user;
   }

   public Employee requireUser(String authorizationHeader) throws AuthException {
      if (authorizationHeader == null || authorizationHeader.isBlank()) {
         throw new AuthException("Sign in required.");
      }
      String token = authorizationHeader.trim();
      if (token.regionMatches(true, 0, "Bearer ", 0, 7)) {
         token = token.substring(7).trim();
      }
      return sessionTokens.verify(token);
   }

   public static Map<String, Object> userMap(Employee employee) {
      Map<String, Object> user = new HashMap<String, Object>();
      user.put("id", employee.getId());
      user.put("email", employee.getEmail());
      user.put("name", employee.getName());
      user.put("role", employee.getRole());
      return user;
   }
}
