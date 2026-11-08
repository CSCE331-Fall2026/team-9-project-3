import { useState } from 'react'
import { Link } from 'react-router-dom'
import MenuGrid from '../components/MenuGrid'
import { useMenu } from '../hooks/useMenu'

// Cashier story: view the menu to find items and prices. Read-only, with a quick search.
function CashierMenu() {
  const { items, loading, error } = useMenu()
  const [query, setQuery] = useState('')

  const needle = query.trim().toLowerCase()
  const matches = needle
    ? items.filter((item) => item.name.toLowerCase().includes(needle))
    : items

  return (
    <main className="page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express POS</p>
        <h1>Menu</h1>
        <div className="actions">
          <Link className="text-link" to="/cashier">Back to cashier home</Link>
        </div>
      </header>

      <section className="menu">
        <input
          type="search"
          className="menu-search"
          placeholder="Search items…"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          aria-label="Search menu items"
        />
        {loading ? <p>Loading menu…</p> : null}
        {error ? <p className="error">{error}</p> : null}
        {!loading && !error && matches.length === 0 ? <p>No matching items.</p> : null}
        {matches.length > 0 ? <MenuGrid items={matches} variant="cashier" /> : null}
      </section>
    </main>
  )
}

export default CashierMenu
