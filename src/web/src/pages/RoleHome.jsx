import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function RoleHome({ role }) {
  const { apiBase, token, user, logout } = useAuth()
  const [message, setMessage] = useState('Loading…')
  const [error, setError] = useState('')

  useEffect(() => {
    let cancelled = false

    async function load() {
      try {
        const res = await fetch(`${apiBase}/api/${role}/home`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        const body = await res.json().catch(() => ({}))
        if (!res.ok) {
          throw new Error(body.error || `Could not open ${role} home`)
        }
        if (!cancelled) {
          setMessage(body.message || `Welcome, ${user?.name}`)
          setError('')
        }
      } catch (err) {
        if (!cancelled) {
          setError(String(err.message || err))
          setMessage('')
        }
      }
    }

    load()
    return () => {
      cancelled = true
    }
  }, [apiBase, role, token, user])

  return (
    <main className="page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express POS</p>
        <h1>{role === 'manager' ? 'Manager' : 'Cashier'} interface</h1>
        <p className="lede">
          Signed in as <strong>{user?.name}</strong> ({user?.email}) · role: <strong>{user?.role}</strong>
        </p>
      </header>

      <section className="status">
        {message ? <p>{message}</p> : null}
        {error ? <p className="error">{error}</p> : null}
        <div className="actions">
          <Link className="text-link" to="/status">Deploy status</Link>
          <button type="button" className="secondary" onClick={logout}>Sign out</button>
        </div>
      </section>
    </main>
  )
}
