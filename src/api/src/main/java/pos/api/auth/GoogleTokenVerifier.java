package pos.api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Verifies a Google ID token via Google's tokeninfo endpoint.
 */
public class GoogleTokenVerifier {
   private static final ObjectMapper MAPPER = new ObjectMapper();
   private static final HttpClient CLIENT = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(10))
      .build();

   private final String expectedClientId;

   public GoogleTokenVerifier(String expectedClientId) {
      this.expectedClientId = expectedClientId;
   }

   public GoogleIdentity verify(String idToken) throws Exception {
      if (idToken == null || idToken.isBlank()) {
         throw new AuthException("Missing Google ID token.");
      }
      if (expectedClientId == null || expectedClientId.isBlank()) {
         throw new AuthException("Server is missing GOOGLE_CLIENT_ID.");
      }

      String url = "https://oauth2.googleapis.com/tokeninfo?id_token="
         + URLEncoder.encode(idToken.trim(), StandardCharsets.UTF_8);
      HttpRequest request = HttpRequest.newBuilder(URI.create(url))
         .timeout(Duration.ofSeconds(10))
         .GET()
         .build();
      HttpResponse<InputStream> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
      JsonNode body = MAPPER.readTree(response.body());

      if (response.statusCode() != 200) {
         String message = text(body, "error_description");
         if (message == null || message.isBlank()) {
            message = text(body, "error");
         }
         if (message == null || message.isBlank()) {
            message = "Google rejected the sign-in token.";
         }
         throw new AuthException(message);
      }

      String audience = text(body, "aud");
      if (!expectedClientId.equals(audience)) {
         throw new AuthException("Google token audience does not match this app.");
      }

      String email = text(body, "email");
      if (email == null || email.isBlank()) {
         throw new AuthException("Google did not return an email address.");
      }

      String emailVerified = text(body, "email_verified");
      if (emailVerified != null && !(emailVerified.equalsIgnoreCase("true") || emailVerified.equals("1"))) {
         throw new AuthException("Google email is not verified.");
      }

      String name = text(body, "name");
      if (name == null || name.isBlank()) {
         name = email;
      }

      return new GoogleIdentity(email, name);
   }

   private static String text(JsonNode node, String field) {
      JsonNode value = node.get(field);
      return value == null || value.isNull() ? null : value.asText();
   }

   public static final class GoogleIdentity {
      private final String email;
      private final String name;

      public GoogleIdentity(String email, String name) {
         this.email = email;
         this.name = name;
      }

      public String getEmail() {
         return email;
      }

      public String getName() {
         return name;
      }
   }
}
