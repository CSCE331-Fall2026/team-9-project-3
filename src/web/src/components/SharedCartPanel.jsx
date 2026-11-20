import { useState } from 'react'
import './order.css'
import './sharedCart.css'

// "Order with friends" box. The host creates a shared cart and gets a code to give out.
// Creating it does not place an order.
function SharedCartPanel({ sharedCart }) {
  const { cart, loading, creating, error, create } = sharedCart
  const [copied, setCopied] = useState(false)

  async function copyCode() {
    try {
      await navigator.clipboard.writeText(cart.joinCode)
      setCopied(true)
    } catch {
      setCopied(false)
    }
  }

  return (
    <section className="order-panel shared-cart" aria-label="Order with friends">
      <h2>Order with friends</h2>

      {loading ? <p role="status">Checking for your shared cart…</p> : null}

      {!loading && !cart ? (
        <>
          <p className="shared-cart__hint">
            Make a shared cart and give the code to your friends so you can order together.
          </p>
          <button type="button" onClick={create} disabled={creating}>
            {creating ? 'Creating…' : 'Create shared cart'}
          </button>
        </>
      ) : null}

      {cart ? (
        <div aria-live="polite">
          <p className="shared-cart__hint">Your shared cart is ready. Give friends this code:</p>
          <p className="shared-cart__code">{cart.joinCode}</p>
          <div className="shared-cart__actions">
            <button type="button" className="secondary" onClick={copyCode}>
              Copy code
            </button>
            {copied ? <span role="status">Copied!</span> : null}
          </div>
          <p className="shared-cart__note">No order has been placed yet.</p>
        </div>
      ) : null}

      {error ? <p className="error" role="alert">{error}</p> : null}
    </section>
  )
}

export default SharedCartPanel
