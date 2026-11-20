import { useState } from 'react'
import './order.css'
import './sharedCart.css'

// "Order with friends" box. The host creates a shared cart and gets a code to give out.
// A friend types that code to join the same cart. Neither one places an order.
function SharedCartPanel({ sharedCart, shared }) {
  const { cart, loading, creating, error, create } = sharedCart
  const [copied, setCopied] = useState(false)
  const [codeInput, setCodeInput] = useState('')

  const hosting = Boolean(cart) && !shared.joined
  const message = error || shared.error

  async function copyCode() {
    try {
      await navigator.clipboard.writeText(cart.joinCode)
      setCopied(true)
    } catch {
      setCopied(false)
    }
  }

  async function submitJoin(event) {
    event.preventDefault()
    if (await shared.join(codeInput)) {
      setCodeInput('')
    }
  }

  return (
    <section className="order-panel shared-cart" aria-label="Order with friends">
      <h2>Order with friends</h2>

      {loading ? <p role="status">Checking for your shared cart…</p> : null}

      {!loading && !cart && !shared.joined ? (
        <>
          <p className="shared-cart__hint">
            Make a shared cart and give the code to your friends so you can order together.
          </p>
          <button type="button" onClick={create} disabled={creating}>
            {creating ? 'Creating…' : 'Create shared cart'}
          </button>

          <form className="shared-cart__join" onSubmit={submitJoin}>
            <label htmlFor="join-code">Have a code from a friend?</label>
            <div className="shared-cart__actions">
              <input
                id="join-code"
                value={codeInput}
                onChange={(e) => setCodeInput(e.target.value.toUpperCase())}
                maxLength={8}
                autoComplete="off"
                autoCapitalize="characters"
                placeholder="K7QM4P"
              />
              <button type="submit" disabled={shared.joining}>
                {shared.joining ? 'Joining…' : 'Join cart'}
              </button>
            </div>
          </form>
        </>
      ) : null}

      {shared.joined ? (
        <div aria-live="polite">
          <p className="shared-cart__hint">
            {shared.cart
              ? `You joined ${shared.cart.hostName}'s shared cart. Code:`
              : 'You joined a shared cart. Code:'}
          </p>
          <p className="shared-cart__code">{shared.code}</p>
          <div className="shared-cart__actions">
            <button type="button" className="secondary" onClick={shared.leave}>
              Leave cart
            </button>
          </div>
          <p className="shared-cart__note">No order has been placed yet.</p>
        </div>
      ) : null}

      {hosting ? (
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

      {message ? <p className="error" role="alert">{message}</p> : null}
    </section>
  )
}

export default SharedCartPanel
