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

   // The customer row is resolved by Google subject when the first order is submitted.
   public Map<String, Object> loginCustomerWithGoogle(String idToken) throws Exception {
      GoogleTokenVerifier.GoogleIdentity identity = googleTokenVerifier.verify(idToken);
      if (identity.getSubject() == null || identity.getSubject().isBlank()) {
         throw new AuthException("Google did not return an account ID.");
      }
      Employee customer = new Employee(0, identity.getEmail(), identity.getName(), "customer",
         identity.getSubject());
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
