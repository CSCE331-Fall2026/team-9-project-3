import { useAuth } from '../auth/AuthContext'
import MenuGrid from '../components/MenuGrid'
import { useMenu } from '../hooks/useMenu'

// Signed-in customers browse live menu data without order controls.
function CustomerMenu() {
  const { user, logout } = useAuth()
  const { items, loading, error } = useMenu()

  return (
    <main className="page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express</p>
        <h1>Our Menu</h1>
        <p className="lede">Browse available items and prices. Signed in as {user?.name}.</p>
        <div className="actions">
          <button type="button" className="secondary" onClick={logout}>Sign out</button>
        </div>
      </header>

      <section className="menu">
        {loading ? <p role="status">Loading menu…</p> : null}
        {error ? <p className="error" role="alert">{error}</p> : null}
        {!loading && !error && items.length === 0 ? <p>No menu items available.</p> : null}
        {items.length > 0 ? <MenuGrid items={items} variant="customer" /> : null}
      </section>
    </main>
  )
}

export default CustomerMenu
