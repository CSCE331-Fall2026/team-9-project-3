package pos.api.order;

/**
 * What the API sends back after an order is saved, so the cashier can see it worked.
 */
public class SavedOrder {
   private final int orderId;
   private final double subtotal;
   private final int lineCount;

   public SavedOrder(int orderId, double subtotal, int lineCount) {
      this.orderId = orderId;
      this.subtotal = subtotal;
      this.lineCount = lineCount;
   }

   public int getOrderId() {
      return orderId;
   }

   public double getSubtotal() {
      return subtotal;
   }

   public int getLineCount() {
      return lineCount;
   }
}
