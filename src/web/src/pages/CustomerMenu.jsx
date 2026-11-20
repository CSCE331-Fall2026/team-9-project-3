import { useAuth } from '../auth/AuthContext'
import MenuGrid from '../components/MenuGrid'
import SharedCartPanel from '../components/SharedCartPanel'
import TicketPanel from '../components/TicketPanel'
import { useCart } from '../hooks/useCart'
import { useMenu } from '../hooks/useMenu'
import { useSharedCart } from '../hooks/useSharedCart'

// Customer builds meals here. Sending the order is a later story.
function CustomerMenu() {
  const { user, token, logout } = useAuth()
  const { items, loading, error } = useMenu()
  const cart = useCart(user?.email)
  const sharedCart = useSharedCart(token)
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

      <SharedCartPanel sharedCart={sharedCart} />

      <TicketPanel
        ticket={cart.ticket}
        actions={actions}
        title="My cart"
        emptyHint="Your personal cart is empty. Browse below, pick a size, then add its side and entrees."
        onClear={cart.clearCart}
      />

      <section className="menu" aria-labelledby="browse-heading">
        <h2 id="browse-heading" className="menu-section__title">Browse</h2>
        <p className="menu-section__hint">
          Pictures of each item. This is the menu — add food here to fill My cart above.
        </p>
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
