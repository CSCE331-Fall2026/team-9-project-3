import { useEffect, useState } from 'react'
import { fetchMenu } from '../api/menu'

// Loads the menu once on mount.
// Returns { items, loading, error } so any page can render the three states.
export function useMenu() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    let cancelled = false

    async function load() {
      try {
        const data = await fetchMenu()
        if (!cancelled) {
          setItems(data)
          setError('')
        }
      } catch (err) {
        if (!cancelled) {
          setItems([])
          setError(String(err.message || err))
        }
      } finally {
        if (!cancelled) {
          setLoading(false)
        }
      }
    }

    load()
    // If the component unmounts mid-request, ignore the late response.
    return () => {
      cancelled = true
    }
  }, [])

  return { items, loading, error }
}
