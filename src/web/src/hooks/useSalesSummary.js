import { useEffect, useState } from 'react'
import { fetchSalesSummary } from '../api/sales'

// Loads the sales summary for the chosen window. Changing days or calling refresh()
// asks the API again, so the numbers always come from the database.
export function useSalesSummary(days, token) {
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [refreshCount, setRefreshCount] = useState(0)

  useEffect(() => {
    let cancelled = false

    async function load() {
      setLoading(true)
      setError('')
      try {
        const data = await fetchSalesSummary(days, token)
        if (!cancelled) {
          setSummary(data)
        }
      } catch (err) {
        if (!cancelled) {
          setSummary(null)
          setError(String(err.message || err))
        }
      } finally {
        if (!cancelled) {
          setLoading(false)
        }
      }
    }

    load()
    // If the window changes mid-request, ignore the old answer.
    return () => {
      cancelled = true
    }
  }, [days, token, refreshCount])

  function refresh() {
    setRefreshCount((count) => count + 1)
  }

  return { summary, loading, error, refresh }
}
