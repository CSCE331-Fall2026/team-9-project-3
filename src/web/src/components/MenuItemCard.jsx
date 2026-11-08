import { describeItem, formatPrice } from '../lib/menu'
import './menu.css'
import './order.css'

// One menu item. `variant` lets the customer and cashier pages share this component.
// If `onAdd` is given (cashier ordering), an Add button is shown.
function MenuItemCard({ item, variant = 'customer', onAdd, addDisabled = false }) {
  const detail = describeItem(item)

  return (
    <li className={`menu-card menu-card--${variant}`}>
      <span className="menu-card__name">{item.name}</span>
      <span className="menu-card__category">{item.category}</span>
      {detail ? <span className="menu-card__detail">{detail}</span> : null}
      <span className="menu-card__price">{formatPrice(item)}</span>
      {onAdd ? (
        <button
          type="button"
          className="add-button"
          onClick={() => onAdd(item)}
          disabled={addDisabled}
          aria-label={`Add ${item.name} to order`}
        >
          Add
        </button>
      ) : null}
    </li>
  )
}

export default MenuItemCard
