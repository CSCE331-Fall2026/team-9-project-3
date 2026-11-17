import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import MenuGrid from '../components/MenuGrid'
import TicketPanel from '../components/TicketPanel'
import { useMenu } from '../hooks/useMenu'
import { useOrder } from '../hooks/useOrder'

// Cashier rings up meals the same way a customer builds them, then saves the order.
function CashierMenu() {
  const { token } = useAuth()
  const { items, loading, error } = useMenu()
  const order = useOrder(token)
  const [query, setQuery] = useState('')
  const actions = {
    removeSide: order.removeSide,
    removeEntree: order.removeEntree,
    removeMeal: order.removeMeal,
    setActiveMeal: order.setActiveMeal,
    changeExtra: order.changeExtra,
  }

  const needle = query.trim().toLowerCase()
  const matches = needle
    ? items.filter((item) => item.name.toLowerCase().includes(needle))
    : items

  return (
    <main className="page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express POS</p>
        <h1>Menu</h1>
        <p className="lede">Start with a size, then add its side and entrees. Apps and drinks are extra.</p>
        <div className="actions">
          <Link className="text-link" to="/cashier">Back to cashier home</Link>
        </div>
      </header>

      <TicketPanel
        ticket={order.ticket}
        actions={actions}
        title="Current order"
        emptyHint="Choose a size, then add its side and entrees. Apps and drinks are extra."
        onSave={order.save}
        saving={order.saving}
        savedOrder={order.savedOrder}
      />

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
        {matches.length > 0 ? (
          <MenuGrid items={matches} variant="cashier" onAdd={order.addItem} />
        ) : null}
      </section>
    </main>
  )
}

export default CashierMenu
