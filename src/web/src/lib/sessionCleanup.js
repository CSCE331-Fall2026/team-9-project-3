// Browser leftovers for carts. Cleared on sign-out so the next login starts empty.

const PERSONAL_CART_PREFIX = 'team9.customer.cart.v2.'
const SHARED_JOIN_CODE_KEY = 'team9.pos.sharedCartCode'

export function clearPersonalCart(email) {
  if (!email) {
    return
  }
  try {
    sessionStorage.removeItem(`${PERSONAL_CART_PREFIX}${email}`)
  } catch {
    // Storage can be blocked; sign-out still continues.
  }
}

export function clearJoinedSharedCartCode() {
  try {
    localStorage.removeItem(SHARED_JOIN_CODE_KEY)
  } catch {
    // Storage can be blocked; sign-out still continues.
  }
}

export function clearCustomerCartState(email) {
  clearPersonalCart(email)
  clearJoinedSharedCartCode()
}
