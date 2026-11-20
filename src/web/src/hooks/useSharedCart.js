import { useEffect, useState } from 'react'
import { createSharedCart, fetchMySharedCart } from '../api/sharedCarts'

// The shared cart this customer hosts. It is loaded from the server when the page opens,
// so it is still there after a refresh.
export function useSharedCart(token) {
  const [cart, setCart] = useState(null)
  const [loading, setLoading] = useState(true)
  const [creating, setCreating] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    let cancelled = false

    async function load() {
      if (!token) {
        setLoading(false)
        return
      }
      try {
        const found = await fetchMySharedCart(token)
        if (!cancelled) {
          setCart(found)
          setError('')
        }
      } catch (err) {
        if (!cancelled) {
          setError(String(err.message || err))
        }
      } finally {
        if (!cancelled) {
          setLoading(false)
        }
      }
    }

    load()
    // If the page closes mid-request, ignore the late answer.
    return () => {
      cancelled = true
    }
  }, [token])

  async function create() {
    setCreating(true)
    setError('')
    try {
      setCart(await createSharedCart(token))
    } catch (err) {
      setError(String(err.message || err))
    } finally {
      setCreating(false)
    }
  }

  return { cart, loading, creating, error, create }
}
