import { useEffect, useState } from 'react'
import './App.css'
import { API_BASE } from './api/client'
import { fetchHealth } from './api/health'
import { fetchMenu } from './api/menu'

function App() {
  const [health, setHealth] = useState({ status: 'loading' })
  const [menu, setMenu] = useState([])
  const [menuError, setMenuError] = useState('')

  useEffect(() => {
    let cancelled = false

    async function load() {
      try {
        const healthJson = await fetchHealth()
        if (!cancelled) {
          setHealth(healthJson)
        }
      } catch (err) {
        if (!cancelled) {
          setHealth({ status: 'error', message: String(err.message || err) })
        }
      }

      try {
        const items = await fetchMenu()
        if (!cancelled) {
          setMenu(items)
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
  }, [])

  const apiOk = health.status === 'ok' && health.database === 'connected'

  return (
    <main className="page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express POS</p>
        <h1>Sprint 1 deploy check</h1>
        <p className="lede">
          React on Vercel talking to the Java API on Render, backed by Neon Postgres.
        </p>
      </header>

      <section className="status" aria-live="polite">
        <h2>API status</h2>
        <p>
          <span className={apiOk ? 'pill ok' : 'pill bad'}>
            {apiOk ? 'Connected' : 'Not connected'}
          </span>
        </p>
        <p className="meta">Endpoint: <code>{API_BASE}/api/health</code></p>
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

export default App
