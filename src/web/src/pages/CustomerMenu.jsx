import { useAuth } from '../auth/AuthContext'
import MenuGrid from '../components/MenuGrid'
import TicketPanel from '../components/TicketPanel'
import { useCart } from '../hooks/useCart'
import { useMenu } from '../hooks/useMenu'

// Customer builds meals here. Sending the order is a later story.
function CustomerMenu() {
  const { user, logout } = useAuth()
  const { items, loading, error } = useMenu()
  const cart = useCart(user?.email)
  const actions = {
    removeSide: cart.removeSide,
    removeEntree: cart.removeEntree,
    removeMeal: cart.removeMeal,
    setActiveMeal: cart.setActiveMeal,
    changeExtra: cart.changeExtra,
  }

  return (
    <main className="page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express</p>
        <h1>Our Menu</h1>
        <p className="lede">
          Start with a size. Then add the side and entrees that come with it. Signed in as {user?.name}.
        </p>
        <div className="actions">
          <button type="button" className="secondary" onClick={logout}>Sign out</button>
        </div>
      </header>

      <TicketPanel
        ticket={cart.ticket}
        actions={actions}
        title="My cart"
        emptyHint="Your cart is empty. Choose a size, then add its side and entrees."
        onClear={cart.clearCart}
      />

      <section className="menu">
        {loading ? <p role="status">Loading menu…</p> : null}
        {error ? <p className="error" role="alert">{error}</p> : null}
        {!loading && !error && items.length === 0 ? <p>No menu items available.</p> : null}
        {items.length > 0 ? (
          <MenuGrid items={items} variant="customer" onAdd={cart.addItem} />
        ) : null}
      </section>
    </main>
  )
}

export default CustomerMenu
