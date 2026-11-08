import './menu.css'

const ALL = 'All'

// Row of filter buttons. `active` is the selected category (or "All").
function CategoryTabs({ categories, active, onChange }) {
  return (
    <div className="category-tabs" role="tablist" aria-label="Menu categories">
      {[ALL, ...categories].map((category) => (
        <button
          key={category}
          type="button"
          role="tab"
          aria-selected={active === category}
          className={active === category ? 'category-tab active' : 'category-tab'}
          onClick={() => onChange(category)}
        >
          {category}
        </button>
      ))}
    </div>
  )
}

export { ALL }
export default CategoryTabs
