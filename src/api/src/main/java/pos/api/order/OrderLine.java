package pos.api.order;

/**
 * One line of an order the cashier sends: which menu item and how many.
 * The price is not sent by the browser. OrderDao looks it up in menu_item.
 */
public class OrderLine {
   private final int menuItemId;
   private final int quantity;

   public OrderLine(int menuItemId, int quantity) {
      this.menuItemId = menuItemId;
      this.quantity = quantity;
   }

   public int getMenuItemId() {
      return menuItemId;
   }

   public int getQuantity() {
      return quantity;
   }
}
