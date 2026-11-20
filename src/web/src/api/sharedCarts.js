import { apiGet, apiPost } from './client'

// Creates a fresh shared cart (closes any open one this customer already hosts).
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

// Closes the hosted shared cart so the next login does not restore the old code.
export async function closeMySharedCart(token) {
  if (!token) {
    return
  }
  await apiPost('/api/shared-carts/close-mine', {}, token)
}

// Joins a shared cart with the code a friend gave you.
// Returns { cart, items }. Throws the API's message for a wrong or closed code.
export function joinSharedCart(joinCode, token) {
  return apiPost('/api/shared-carts/join', { joinCode }, token)
}

// The cart's current items, for refreshing. Returns { cart, items }.
export function fetchSharedCart(joinCode, token) {
  return apiGet(`/api/shared-carts/${encodeURIComponent(joinCode)}`, token)
}

// Adds one of a menu item to the cart. Returns { cart, items } with the item included.
export function addSharedCartItem(joinCode, menuItemId, token) {
  return apiPost(`/api/shared-carts/${encodeURIComponent(joinCode)}/items`, { menuItemId }, token)
}
