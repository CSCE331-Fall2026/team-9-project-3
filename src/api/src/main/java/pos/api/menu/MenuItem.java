package pos.api.menu;

/**
 * One row of menu_item. Adapted from Project 2 for the HTTP API.
 * Nutrition columns are nullable in the schema, so they are Integer (null = unknown).
 */
public class MenuItem {
   private final int id;
   private final String name;
   private final String category;
   private final double basePrice;
   private final boolean available;
   private final Integer calorie;
   private final Integer protein;
   private final Integer fat;
   private final Integer carbohydrates;
   private final String allergens;

   public MenuItem(int id, String name, String category, double basePrice, boolean available,
                   Integer calorie, Integer protein, Integer fat, Integer carbohydrates,
                   String allergens) {
      this.id = id;
      this.name = name;
      this.category = category;
      this.basePrice = basePrice;
      this.available = available;
      this.calorie = calorie;
      this.protein = protein;
      this.fat = fat;
      this.carbohydrates = carbohydrates;
      this.allergens = allergens;
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

   public Integer getCalorie() {
      return calorie;
   }

   public Integer getProtein() {
      return protein;
   }

   public Integer getFat() {
      return fat;
   }

   public Integer getCarbohydrates() {
      return carbohydrates;
   }

   public String getAllergens() {
      return allergens;
   }
}
