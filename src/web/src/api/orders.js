import { apiPost } from './client'

// Saves the order through the Java API. Only ids and quantities are sent;
// the API looks up each price in the database.
// Returns { orderId, subtotal, lineCount }.
export function saveOrder(lines, token) {
  const items = lines.map((line) => ({
    menuItemId: line.item.id,
    quantity: line.quantity,
  }))
  return apiPost('/api/orders', { items }, token)
}
