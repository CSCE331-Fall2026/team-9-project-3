import { useState } from 'react'
import { getCategories, visibleItems } from '../lib/menu'
import './order.css'
import './sharedCart.css'

// "Order with friends" box. The host creates a shared cart and gets a code to give out.
// A friend types that code to join the same cart. Everyone in the cart can add menu items,
// and all the items show here. None of this places an order.
function SharedCartPanel({ sharedCart, shared, menuItems }) {
  const { cart, loading, creating, error, create } = sharedCart
  const [copied, setCopied] = useState(false)
  const [codeInput, setCodeInput] = useState('')
  const [pickedId, setPickedId] = useState('')

  const hosting = Boolean(cart) && !shared.joined
  const message = error || shared.error

  // Sizes (Bowl, Plate, ...) only mean something inside a meal, so they are not offered here.
  const addable = visibleItems(menuItems).filter(
    (item) => item.available && item.category !== 'Size',
  )
  const addableCategories = getCategories(addable)

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

  function submitAdd(event) {
    event.preventDefault()
    if (pickedId) {
      shared.addItem(Number(pickedId))
    }
  }

  return (
    <section className="order-panel shared-cart" aria-label="Shared cart">
      <h2>Shared cart</h2>

      {loading ? <p role="status">Checking for your shared cart…</p> : null}

      {!loading && !cart && !shared.joined ? (
        <>
          <p className="shared-cart__hint">
            Separate from My cart. Make a shared cart and give friends the code so you can order together.
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
          <p className="shared-cart__hint">Shared cart is ready (not My cart). Give friends this code:</p>
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

      {shared.code ? (
        <div>
          <h3>Items in this cart</h3>
          {shared.items.length === 0 ? (
            <p className="shared-cart__note">Nothing added yet.</p>
          ) : (
            <ul className="shared-cart__items" aria-live="polite">
              {shared.items.map((item) => (
                <li key={item.id}>
                  <span>{item.name} × {item.quantity}</span>
                  <span className="shared-cart__by">
                    added by {item.addedByName || item.addedByEmail}
                  </span>
                </li>
              ))}
            </ul>
          )}

          <form className="shared-cart__add" onSubmit={submitAdd}>
            <label htmlFor="shared-item">Add an item to this cart</label>
            <div className="shared-cart__actions">
              <select
                id="shared-item"
                value={pickedId}
                onChange={(e) => setPickedId(e.target.value)}
              >
                <option value="">Choose an item…</option>
                {addableCategories.map((category) => (
                  <optgroup key={category} label={category}>
                    {addable
                      .filter((item) => item.category === category)
                      .map((item) => (
                        <option key={item.id} value={item.id}>{item.name}</option>
                      ))}
                  </optgroup>
                ))}
              </select>
              <button type="submit" disabled={!pickedId || shared.adding}>
                {shared.adding ? 'Adding…' : 'Add'}
              </button>
            </div>
          </form>
          <p className="shared-cart__note">The list refreshes every few seconds.</p>
        </div>
      ) : null}

      {message ? <p className="error" role="alert">{message}</p> : null}
    </section>
  )
}

export default SharedCartPanel
