import { formatMoney } from '../lib/order'
import {
  mealPrice,
  mealStatus,
  ticketSubtotal,
} from '../lib/ticket'
import './order.css'

function TicketPanel({
  ticket,
  actions,
  title = 'Your order',
  emptyHint = 'Choose a size, then add its side and entrees.',
  onSave,
  onClear,
  saving,
  savedOrder,
}) {
  const { meals, extras, activeMealId, error } = ticket
  const empty = meals.length === 0 && extras.length === 0

  return (
    <section className="order-panel" aria-label={title}>
      <div className="order-panel__head">
        <h2>{title}</h2>
        {onClear && !empty ? (
          <button type="button" className="secondary" onClick={onClear}>
            Clear cart
          </button>
        ) : null}
      </div>

      {savedOrder ? (
        <p className="order-saved" role="status">
          Order #{savedOrder.orderId} saved. Subtotal {formatMoney(savedOrder.subtotal)}.
        </p>
      ) : null}

      {empty ? (
        <p className="order-empty">{emptyHint}</p>
      ) : null}

      {meals.map((meal) => {
        const active = meal.id === activeMealId
        return (
          <article
            key={meal.id}
            className={active ? 'meal meal--active' : 'meal'}
          >
            <header className="meal__head">
              <button type="button" className="meal__title" onClick={() => actions.setActiveMeal(meal.id)}>
                {meal.size.name}
                {active ? ' · adding here' : ''}
              </button>
              <span>{formatMoney(mealPrice(meal))}</span>
            </header>
            <p className="meal__status">{mealStatus(meal)}</p>
            <ul className="meal__parts">
              {meal.sides.map((item, index) => (
                <li key={`side-${item.id}-${index}`}>
                  <span>Side: {item.name}</span>
                  <button type="button" className="secondary" onClick={() => actions.removeSide(meal.id, index)}>
                    Remove
                  </button>
                </li>
              ))}
              {meal.entrees.map((item, index) => (
                <li key={`entree-${item.id}-${index}`}>
                  <span>
                    Entree: {item.name}
                    {Number(item.basePrice) > 0 ? ` (+${formatMoney(item.basePrice)})` : ''}
                  </span>
                  <button type="button" className="secondary" onClick={() => actions.removeEntree(meal.id, index)}>
                    Remove
                  </button>
                </li>
              ))}
            </ul>
            <button type="button" className="secondary" onClick={() => actions.removeMeal(meal.id)}>
              Remove {meal.size.name}
            </button>
          </article>
        )
      })}

      {extras.length > 0 ? (
        <ul className="order-lines">
          {extras.map((line) => (
            <li key={line.item.id} className="order-line">
              <span className="order-line__name">{line.item.name}</span>
              <span className="order-line__qty">
                <button
                  type="button"
                  className="secondary"
                  onClick={() => actions.changeExtra(line.item.id, line.quantity - 1)}
                  aria-label={`Decrease ${line.item.name}`}
                >
                  -
                </button>
                {line.quantity}
                <button
                  type="button"
                  className="secondary"
                  onClick={() => actions.changeExtra(line.item.id, line.quantity + 1)}
                  aria-label={`Increase ${line.item.name}`}
                >
                  +
                </button>
              </span>
              <span className="order-line__price">
                {formatMoney(Number(line.item.basePrice) * line.quantity)}
              </span>
              <button
                type="button"
                className="secondary"
                onClick={() => actions.changeExtra(line.item.id, 0)}
              >
                Remove
              </button>
            </li>
          ))}
        </ul>
      ) : null}

      <p className="order-subtotal" aria-live="polite">
        Subtotal (before tax): <strong>{formatMoney(ticketSubtotal(ticket))}</strong>
      </p>

      {onSave ? (
        <button type="button" onClick={onSave} disabled={saving || empty}>
          {saving ? 'Saving…' : 'Save order'}
        </button>
      ) : null}

      {error ? <p className="error" role="alert">{error}</p> : null}
    </section>
  )
}

export default TicketPanel
