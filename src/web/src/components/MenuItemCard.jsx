import { describeItem, formatPrice } from '../lib/menu'
import './menu.css'

// One menu item. `variant` lets the customer and cashier pages share this component.
function MenuItemCard({ item, variant = 'customer' }) {
  const detail = describeItem(item)

  return (
    <li className={`menu-card menu-card--${variant}`}>
      <span className="menu-card__name">{item.name}</span>
      <span className="menu-card__category">{item.category}</span>
      {detail ? <span className="menu-card__detail">{detail}</span> : null}
      <span className="menu-card__price">{formatPrice(item)}</span>
    </li>
  )
}

export default MenuItemCard
