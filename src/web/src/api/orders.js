import { apiPost } from './client'

// Saves a cashier order. Meals and extra items are priced on the server.
// Returns { orderId, subtotal, lineCount }.
export function saveOrder(payload, token) {
  return apiPost('/api/orders', payload, token)
}
