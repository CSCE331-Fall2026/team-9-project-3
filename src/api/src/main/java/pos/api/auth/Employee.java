package pos.api.auth;

public class Employee {
   private final int id;
   private final String email;
   private final String name;
   private final String role;

   public Employee(int id, String email, String name, String role) {
      this.id = id;
      this.email = email;
      this.name = name;
      this.role = role;
   }

   public int getId() {
      return id;
   }

   public String getEmail() {
      return email;
   }

   public String getName() {
      return name;
   }

   public String getRole() {
      return role;
   }
}
