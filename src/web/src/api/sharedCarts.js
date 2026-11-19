import { apiGet, apiPost } from './client'

// Creates a shared cart for the signed-in customer (or returns the one they already host).
// Returns the cart: { id, joinCode, hostName, status, createdAt }.
export async function createSharedCart(token) {
  const data = await apiPost('/api/shared-carts', {}, token)
  return data.cart
}

// The customer's open shared cart, or null if they have not made one.
export async function fetchMySharedCart(token) {
  const data = await apiGet('/api/shared-carts/mine', token)
  return data.cart || null
}
