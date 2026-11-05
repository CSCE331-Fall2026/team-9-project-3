package pos.api.menu;

/**
 * One row of menu_item. Adapted from Project 2 for the HTTP API.
 */
public class MenuItem {
   private final int id;
   private final String name;
   private final String category;
   private final double basePrice;
   private final boolean available;

   public MenuItem(int id, String name, String category, double basePrice, boolean available) {
      this.id = id;
      this.name = name;
      this.category = category;
      this.basePrice = basePrice;
      this.available = available;
   }

   public int getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getCategory() {
      return category;
   }

   public double getBasePrice() {
      return basePrice;
   }

   public boolean isAvailable() {
      return available;
   }
}
