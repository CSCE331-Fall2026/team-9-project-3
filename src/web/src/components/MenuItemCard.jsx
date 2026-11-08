import { formatPrice } from '../lib/menu'
import './menu.css'

// One menu item. `variant` lets the customer and cashier pages share this component.
function MenuItemCard({ item, variant = 'customer' }) {
  return (
    <li className={`menu-card menu-card--${variant}`}>
      <span className="menu-card__name">{item.name}</span>
      <span className="menu-card__category">{item.category}</span>
      <span className="menu-card__price">{formatPrice(item)}</span>
    </li>
  )
}

export default MenuItemCard
