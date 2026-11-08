import { Link } from 'react-router-dom'
import MenuGrid from '../components/MenuGrid'
import { useMenu } from '../hooks/useMenu'

// Customer story: browse the menu to see what can be ordered. Public and read-only.
function CustomerMenu() {
  const { items, loading, error } = useMenu()

  return (
    <main className="page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express</p>
        <h1>Our Menu</h1>
        <p className="lede">Browse what we offer.</p>
        <div className="actions">
          <Link className="text-link" to="/login">Employee sign in</Link>
        </div>
      </header>

      <section className="menu">
        {loading ? <p>Loading menu…</p> : null}
        {error ? <p className="error">{error}</p> : null}
        {!loading && !error && items.length === 0 ? <p>No menu items available.</p> : null}
        {items.length > 0 ? <MenuGrid items={items} variant="customer" /> : null}
      </section>
    </main>
  )
}

export default CustomerMenu
