package pos.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Compact HMAC-signed session tokens for the SPA.
 */
public class SessionTokens {
   private static final ObjectMapper MAPPER = new ObjectMapper();
   private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
   private static final Base64.Decoder B64D = Base64.getUrlDecoder();
   private static final long TTL_SECONDS = 60L * 60L * 12L;

   private final byte[] secret;

   public SessionTokens(String secret) {
      if (secret == null || secret.isBlank()) {
         throw new IllegalStateException("SESSION_SECRET is required");
      }
      this.secret = secret.getBytes(StandardCharsets.UTF_8);
   }

   public String issue(Employee employee) {
      try {
         Map<String, Object> payload = new HashMap<String, Object>();
         payload.put("sub", employee.getId());
         payload.put("email", employee.getEmail());
         payload.put("name", employee.getName());
         payload.put("role", employee.getRole());
         payload.put("exp", Instant.now().getEpochSecond() + TTL_SECONDS);
         String body = B64.encodeToString(MAPPER.writeValueAsBytes(payload));
         String signature = B64.encodeToString(hmac(body));
         return body + "." + signature;
      } catch (Exception e) {
         throw new RuntimeException("Could not issue session token", e);
      }
   }

   public Employee verify(String token) throws AuthException {
      if (token == null || token.isBlank() || !token.contains(".")) {
         throw new AuthException("Missing or invalid session token.");
      }

      try {
         String[] parts = token.split("\\.", 2);
         String body = parts[0];
         String signature = parts[1];
         String expected = B64.encodeToString(hmac(body));
         if (!constantTimeEquals(expected, signature)) {
            throw new AuthException("Invalid session token.");
         }

         Map<?, ?> payload = MAPPER.readValue(B64D.decode(body), Map.class);
         Object expValue = payload.get("exp");
         long exp = expValue instanceof Number ? ((Number) expValue).longValue() : 0L;
         if (exp < Instant.now().getEpochSecond()) {
            throw new AuthException("Session expired. Please sign in again.");
         }

         int id = ((Number) payload.get("sub")).intValue();
         String email = String.valueOf(payload.get("email"));
         String name = String.valueOf(payload.get("name"));
         String role = String.valueOf(payload.get("role"));
         return new Employee(id, email, name, role);
      } catch (AuthException e) {
         throw e;
      } catch (Exception e) {
         throw new AuthException("Invalid session token.");
      }
   }

   private byte[] hmac(String body) throws Exception {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secret, "HmacSHA256"));
      return mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
   }

   private static boolean constantTimeEquals(String a, String b) {
      if (a == null || b == null || a.length() != b.length()) {
         return false;
      }
      int result = 0;
      for (int i = 0; i < a.length(); i++) {
         result |= a.charAt(i) ^ b.charAt(i);
      }
      return result == 0;
   }
}
