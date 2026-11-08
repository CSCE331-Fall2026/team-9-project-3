import { useState } from 'react'
import { getCategories, visibleItems } from '../lib/menu'
import CategoryTabs, { ALL } from './CategoryTabs'
import MenuItemCard from './MenuItemCard'
import './menu.css'

// Tabs + list of cards. Owns which category is selected.
// onAdd and addDisabled are passed to each card for cashier ordering.
function MenuGrid({ items, variant = 'customer', onAdd, addDisabled = false }) {
  const [active, setActive] = useState(ALL)

  const shown = visibleItems(items)
  const categories = getCategories(shown)
  const filtered = active === ALL ? shown : shown.filter((item) => item.category === active)

  return (
    <div>
      <CategoryTabs categories={categories} active={active} onChange={setActive} />
      <ul className={`menu-grid menu-grid--${variant}`}>
        {filtered.map((item) => (
          <MenuItemCard
            key={item.id}
            item={item}
            variant={variant}
            onAdd={onAdd}
            addDisabled={addDisabled}
          />
        ))}
      </ul>
    </div>
  )
}

export default MenuGrid
