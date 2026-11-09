import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function ManagerInventory() {
  const { apiBase, token, logout } = useAuth()
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refresh, setRefresh] = useState(0)

  useEffect(() => {
    const controller = new AbortController()
    async function load() {
      setLoading(true)
      setError('')
      setItems([])
      try {
        const res = await fetch(`${apiBase}/api/manager/inventory`, {
          headers: { Authorization: `Bearer ${token}` },
          cache: 'no-store',
          signal: controller.signal,
        })
        const body = await res.json()
        if (!res.ok) throw new Error(body.error || 'Could not load inventory.')
        if (!controller.signal.aborted) setItems(body)
      } catch (err) {
        if (!controller.signal.aborted) setError(err.message || 'Could not load inventory.')
      } finally {
        if (!controller.signal.aborted) setLoading(false)
      }
    }
    load()
    return () => controller.abort()
  }, [apiBase, token, refresh])

  return (
    <main className="page inventory-page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express POS</p>
        <h1>Current inventory</h1>
        <p className="lede">Review stock quantities and reorder levels. Refresh to load the latest inventory.</p>
        <div className="actions">
          <Link className="text-link" to="/manager">Manager home</Link>
          <button type="button" className="secondary" disabled={loading} onClick={() => setRefresh(value => value + 1)}>Refresh inventory</button>
          <button type="button" className="secondary" onClick={logout}>Sign out</button>
        </div>
      </header>
      <section aria-label="Inventory" aria-busy={loading}>
        {loading && <p role="status">Loading inventory…</p>}
        {error && <p className="error" role="alert">{error} Use Refresh inventory to retry.</p>}
        {!loading && !error && (items.length === 0 ? <p role="status">No inventory items found.</p> : (
          <div className="inventory-table-scroll">
            <table className="inventory-table">
              <caption>Inventory quantities from the live database</caption>
              <thead><tr>
                <th scope="col">Item</th><th scope="col">Store</th><th scope="col">Location</th>
                <th scope="col">Quantity on hand</th><th scope="col">Unit</th>
                <th scope="col">Reorder level</th><th scope="col">Stock status</th>
              </tr></thead>
              <tbody>{items.map(item => (
                <tr key={item.id}>
                  <th scope="row">{item.name}</th><td>{item.storeId}</td><td>{item.storageLocation}</td>
                  <td>{item.qtyOnHand.toLocaleString()}</td><td>{item.unitOfMeasure}</td>
                  <td>{item.reorderThreshold.toLocaleString()}</td>
                  <td><span className={`pill ${item.qtyOnHand <= item.reorderThreshold ? 'bad' : 'ok'}`}>
                    {item.qtyOnHand <= item.reorderThreshold ? 'Reorder' : 'In stock'}
                  </span></td>
                </tr>
              ))}</tbody>
            </table>
          </div>
        ))}
      </section>
    </main>
  )
}
