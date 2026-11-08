import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function StatusPage() {
  const { apiBase, user, logout } = useAuth()
  const [health, setHealth] = useState({ status: 'loading' })
  const [menu, setMenu] = useState([])
  const [menuError, setMenuError] = useState('')

  useEffect(() => {
    let cancelled = false

    async function load() {
      try {
        const healthRes = await fetch(`${apiBase}/api/health`)
        const healthJson = await healthRes.json()
        if (!cancelled) {
          setHealth(healthJson)
        }
      } catch (err) {
        if (!cancelled) {
          setHealth({ status: 'error', message: String(err.message || err) })
        }
      }

      try {
        const menuRes = await fetch(`${apiBase}/api/menu`)
        if (!menuRes.ok) {
          throw new Error(`Menu request failed (${menuRes.status})`)
        }
        const menuJson = await menuRes.json()
        if (!cancelled) {
          setMenu(Array.isArray(menuJson) ? menuJson : [])
          setMenuError('')
        }
      } catch (err) {
        if (!cancelled) {
          setMenu([])
          setMenuError(String(err.message || err))
        }
      }
    }

    load()
    return () => {
      cancelled = true
    }
  }, [apiBase])

  const apiOk = health.status === 'ok' && health.database === 'connected'

  return (
    <main className="page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express POS</p>
        <h1>Sprint 1 deploy check</h1>
        <p className="lede">
          React on Vercel talking to the Java API on Render, backed by Neon Postgres.
        </p>
        <div className="actions">
          {user ? (
            <>
              <Link className="text-link" to={user.role === 'manager' ? '/manager' : '/cashier'}>
                Open {user.role} home
              </Link>
              <button type="button" className="secondary" onClick={logout}>Sign out</button>
            </>
          ) : (
            <Link className="text-link" to="/login">Employee sign in</Link>
          )}
        </div>
      </header>

      <section className="status" aria-live="polite">
        <h2>API status</h2>
        <p>
          <span className={apiOk ? 'pill ok' : 'pill bad'}>
            {apiOk ? 'Connected' : 'Not connected'}
          </span>
        </p>
        <p className="meta">Endpoint: <code>{apiBase}/api/health</code></p>
        {health.message ? <p className="error">{health.message}</p> : null}
      </section>

      <section className="menu">
        <h2>Live menu</h2>
        {menuError ? <p className="error">{menuError}</p> : null}
        {!menuError && menu.length === 0 ? <p>Loading menu…</p> : null}
        {menu.length > 0 ? (
          <ul className="menu-list">
            {menu.map((item) => (
              <li key={item.id}>
                <span className="name">{item.name}</span>
                <span className="category">{item.category}</span>
                <span className="price">${Number(item.basePrice).toFixed(2)}</span>
              </li>
            ))}
          </ul>
        ) : null}
      </section>
    </main>
  )
}
