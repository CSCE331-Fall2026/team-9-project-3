import { useState } from 'react'
import { getCategories, visibleItems } from '../lib/menu'
import CategoryTabs, { ALL } from './CategoryTabs'
import MenuItemCard from './MenuItemCard'
import './menu.css'

// Tabs + list of cards. Owns which category is selected.
// onAdd and addDisabled are passed to each card when the page can add items.
function MenuGrid({ items, variant = 'customer', onAdd, addDisabled = false }) {
  const [active, setActive] = useState(null)

  const shown = visibleItems(items)
  const categories = getCategories(shown)
  const preferred = active || (categories.includes('Size') ? 'Size' : ALL)
  const selected = preferred === ALL || categories.includes(preferred) ? preferred : ALL
  const filtered = selected === ALL ? shown : shown.filter((item) => item.category === selected)

  return (
    <div>
      <CategoryTabs categories={categories} active={selected} onChange={setActive} />
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
