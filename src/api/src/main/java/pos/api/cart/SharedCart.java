package pos.api.cart;

/**
 * One row of shared_cart. The join code is what the host gives to friends.
 */
public class SharedCart {
   private final int id;
   private final String joinCode;
   private final String hostEmail;
   private final String hostName;
   private final String status;
   private final String createdAt;

   public SharedCart(int id, String joinCode, String hostEmail, String hostName, String status,
      String createdAt) {
      this.id = id;
      this.joinCode = joinCode;
      this.hostEmail = hostEmail;
      this.hostName = hostName;
      this.status = status;
      this.createdAt = createdAt;
   }

   public int getId() {
      return id;
   }

   public String getJoinCode() {
      return joinCode;
   }

   public String getHostEmail() {
      return hostEmail;
   }

   public String getHostName() {
      return hostName;
   }

   public String getStatus() {
      return status;
   }

   public String getCreatedAt() {
      return createdAt;
   }
}
