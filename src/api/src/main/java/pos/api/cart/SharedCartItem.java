package pos.api.cart;

/**
 * One line in a shared cart: a menu item someone added, and who added it.
 */
public class SharedCartItem {
   private final int id;
   private final int menuItemId;
   private final String name;
   private final double price;
   private final int quantity;
   private final String addedByEmail;
   private final String addedByName;

   public SharedCartItem(int id, int menuItemId, String name, double price, int quantity,
      String addedByEmail, String addedByName) {
      this.id = id;
      this.menuItemId = menuItemId;
      this.name = name;
      this.price = price;
      this.quantity = quantity;
      this.addedByEmail = addedByEmail;
      this.addedByName = addedByName;
   }

   public int getId() {
      return id;
   }

   public int getMenuItemId() {
      return menuItemId;
   }

   public String getName() {
      return name;
   }

   public double getPrice() {
      return price;
   }

   public int getQuantity() {
      return quantity;
   }

   public String getAddedByEmail() {
      return addedByEmail;
   }

   public String getAddedByName() {
      return addedByName;
   }
}
