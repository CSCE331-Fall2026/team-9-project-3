import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { useSalesSummary } from '../hooks/useSalesSummary'
import { formatMoney } from '../lib/order'
import { SALES_WINDOWS, timeAgo, windowLabel } from '../lib/sales'
import '../components/menu.css'
import '../components/sales.css'

// Manager sales summary: order count and sales total for a chosen window, from the database.
export default function ManagerSales() {
  const { token, logout } = useAuth()
  const [days, setDays] = useState(1)
  const { summary, loading, error, refresh } = useSalesSummary(days, token)

  return (
    <main className="page sales-page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express POS</p>
        <h1>Sales summary</h1>
        <p className="lede">How the store is doing. Pick a time window to see orders and sales.</p>
        <div className="actions">
          <Link className="text-link" to="/manager">Manager home</Link>
          <Link className="text-link" to="/manager/inventory">Inventory</Link>
          <button type="button" className="secondary" disabled={loading} onClick={refresh}>
            Refresh
          </button>
          <button type="button" className="secondary" onClick={logout}>Sign out</button>
        </div>
      </header>

      <div className="category-tabs" role="group" aria-label="Time window">
        {SALES_WINDOWS.map((window) => (
          <button
            key={window.days}
            type="button"
            className={days === window.days ? 'category-tab active' : 'category-tab'}
            aria-pressed={days === window.days}
            onClick={() => setDays(window.days)}
          >
            {window.label}
          </button>
        ))}
      </div>

      <section aria-label="Sales summary" aria-busy={loading}>
        {loading ? <p role="status">Loading sales…</p> : null}
        {error ? <p className="error" role="alert">{error} Use Refresh to try again.</p> : null}

        {!loading && summary ? (
          <>
            <div className="sales-stats">
              <div className="sales-stat">
                <span className="sales-stat__label">Orders</span>
                <span className="sales-stat__value">{summary.orderCount}</span>
              </div>
              <div className="sales-stat">
                <span className="sales-stat__label">Sales total</span>
                <span className="sales-stat__value">{formatMoney(summary.salesTotal)}</span>
              </div>
              <div className="sales-stat">
                <span className="sales-stat__label">Average order</span>
                <span className="sales-stat__value">{formatMoney(summary.averageOrder)}</span>
              </div>
            </div>

            <h2 className="sales-heading">Most recent orders</h2>
            {summary.recentOrders.length === 0 ? (
              <p role="status">No orders in the {windowLabel(summary.days).toLowerCase()}.</p>
            ) : (
              <div className="inventory-table-scroll">
                <table className="inventory-table">
                  <caption>
                    Latest {summary.recentOrders.length} orders in the {windowLabel(summary.days).toLowerCase()}
                  </caption>
                  <thead>
                    <tr>
                      <th scope="col">Order</th>
                      <th scope="col">Placed</th>
                      <th scope="col">Type</th>
                      <th scope="col">Status</th>
                      <th scope="col">Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {summary.recentOrders.map((order) => (
                      <tr key={order.id}>
                        <th scope="row">#{order.id}</th>
                        <td>{timeAgo(order.minutesAgo)}</td>
                        <td>{order.orderType}</td>
                        <td>{order.status}</td>
                        <td>{formatMoney(order.total)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </>
        ) : null}
      </section>
    </main>
  )
}
