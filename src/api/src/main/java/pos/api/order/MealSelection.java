package pos.api.order;

import java.util.List;

/**
 * One meal the cashier is saving: a size plus the sides and entrees in it.
 * Prices are not included. OrderDao looks those up.
 */
public class MealSelection {
   private final int sizeId;
   private final List<Integer> sideIds;
   private final List<Integer> entreeIds;

   public MealSelection(int sizeId, List<Integer> sideIds, List<Integer> entreeIds) {
      this.sizeId = sizeId;
      this.sideIds = sideIds;
      this.entreeIds = entreeIds;
   }

   public int getSizeId() {
      return sizeId;
   }

   public List<Integer> getSideIds() {
      return sideIds;
   }

   public List<Integer> getEntreeIds() {
      return entreeIds;
   }
}
