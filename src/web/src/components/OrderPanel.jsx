import { formatMoney, lineTotal, orderSubtotal } from '../lib/order'
import './order.css'

// The cashier's current order: start it, see each item and the running subtotal, then save.
function OrderPanel({ order }) {
  const { started, lines, saving, error, savedOrder, startOrder, removeItem, save } = order

  return (
    <section className="order-panel" aria-label="Current order">
      <h2>Current order</h2>

      {savedOrder ? (
        <p className="order-saved" role="status">
          Order #{savedOrder.orderId} saved. Subtotal {formatMoney(savedOrder.subtotal)}.
        </p>
      ) : null}

      {!started ? (
        <button type="button" onClick={startOrder}>New order</button>
      ) : (
        <>
          {lines.length === 0 ? (
            <p className="order-empty">No items yet. Press Add on a menu item.</p>
          ) : (
            <ul className="order-lines">
              {lines.map((line) => (
                <li key={line.item.id} className="order-line">
                  <span className="order-line__name">{line.item.name}</span>
                  <span className="order-line__qty">x{line.quantity}</span>
                  <span className="order-line__price">{formatMoney(lineTotal(line))}</span>
                  <button
                    type="button"
                    className="secondary"
                    onClick={() => removeItem(line.item.id)}
                    aria-label={`Remove one ${line.item.name}`}
                  >
                    -
                  </button>
                </li>
              ))}
            </ul>
          )}

          <p className="order-subtotal" aria-live="polite">
            Subtotal (before tax): <strong>{formatMoney(orderSubtotal(lines))}</strong>
          </p>

          <button type="button" onClick={save} disabled={saving || lines.length === 0}>
            {saving ? 'Saving…' : 'Save order'}
          </button>
        </>
      )}

      {error ? <p className="error">{error}</p> : null}
    </section>
  )
}

export default OrderPanel
