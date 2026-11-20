import { useCallback, useEffect, useState } from 'react'
import { addSharedCartItem, fetchSharedCart, joinSharedCart } from '../api/sharedCarts'

const STORAGE_KEY = 'team9.pos.sharedCartCode'
const POLL_MS = 5000

function readSavedCode() {
  try {
    return localStorage.getItem(STORAGE_KEY) || ''
  } catch {
    return ''
  }
}

function saveCode(code) {
  try {
    if (code) {
      localStorage.setItem(STORAGE_KEY, code)
    } else {
      localStorage.removeItem(STORAGE_KEY)
    }
  } catch {
    // Storage can be blocked. The cart still works until the page is refreshed.
  }
}

// The items in the shared cart this customer is working in.
// hostCode is the code of the cart they host (if any). A code they joined with wins over it.
// The cart lives on the server. The joined code is remembered in this browser so a refresh
// keeps you in the cart, and the items are re-fetched every few seconds so friends' items show up.
export function useSharedCartItems(token, hostCode) {
  const [joinedCode, setJoinedCode] = useState(readSavedCode)
  const [cart, setCart] = useState(null)
  const [items, setItems] = useState([])
  const [joining, setJoining] = useState(false)
  const [adding, setAdding] = useState(false)
  const [error, setError] = useState('')

  const code = joinedCode || hostCode || ''

  const applyReply = useCallback((reply) => {
    setCart(reply.cart)
    setItems(reply.items || [])
  }, [])

  // Load the cart now and every few seconds. Late answers from an old code are ignored.
  useEffect(() => {
    if (!token || !code) {
      return undefined
    }

    let cancelled = false

    async function refresh() {
      try {
        const reply = await fetchSharedCart(code, token)
        if (!cancelled) {
          applyReply(reply)
          setError('')
        }
      } catch (err) {
        if (!cancelled) {
          const message = String(err.message || err)
          setError(message)
          // Drop a stale saved join code so sign-in / hosting can recover.
          if (joinedCode && /did not match an open shared cart/i.test(message)) {
            saveCode('')
            setJoinedCode('')
            setCart(null)
            setItems([])
          }
        }
      }
    }

    refresh()
    const timer = setInterval(refresh, POLL_MS)
    return () => {
      cancelled = true
      clearInterval(timer)
    }
  }, [token, code, applyReply])

  async function join(enteredCode) {
    const trimmed = String(enteredCode || '').trim()
    if (!trimmed) {
      setError('Enter the code your friend gave you.')
      return false
    }
    setJoining(true)
    setError('')
    try {
      const reply = await joinSharedCart(trimmed, token)
      saveCode(reply.cart.joinCode)
      setJoinedCode(reply.cart.joinCode)
      applyReply(reply)
      return true
    } catch (err) {
      setError(String(err.message || err))
      return false
    } finally {
      setJoining(false)
    }
  }

  async function addItem(menuItemId) {
    setAdding(true)
    setError('')
    try {
      applyReply(await addSharedCartItem(code, menuItemId, token))
    } catch (err) {
      setError(String(err.message || err))
    } finally {
      setAdding(false)
    }
  }

  // Stop working in a joined cart. This does not delete the cart for the others.
  function leave() {
    saveCode('')
    setJoinedCode('')
    setCart(null)
    setItems([])
    setError('')
  }

  // With no code there is no cart, whatever was loaded before.
  return {
    code,
    cart: code ? cart : null,
    items: code ? items : [],
    joined: Boolean(joinedCode),
    joining,
    adding,
    error,
    join,
    addItem,
    leave,
  }
}
