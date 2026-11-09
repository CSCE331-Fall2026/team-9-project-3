import { createContext, useContext, useEffect, useMemo, useState } from 'react'

const API_BASE = (import.meta.env.VITE_API_URL || 'http://localhost:8080').replace(/\/$/, '')
const STORAGE_KEY = 'team9.pos.session'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(STORAGE_KEY) || '')
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(Boolean(localStorage.getItem(STORAGE_KEY)))
  const [error, setError] = useState('')

  useEffect(() => {
    let cancelled = false

    async function restore() {
      if (!token) {
        setUser(null)
        setLoading(false)
        return
      }

      setLoading(true)
      try {
        const res = await fetch(`${API_BASE}/api/auth/me`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        const body = await res.json().catch(() => ({}))
        if (!res.ok) {
          throw new Error(body.error || 'Session expired. Please sign in again.')
        }
        if (!cancelled) {
          setUser(body.user)
          setError('')
        }
      } catch (err) {
        if (!cancelled) {
          localStorage.removeItem(STORAGE_KEY)
          setToken('')
          setUser(null)
          setError(String(err.message || err))
        }
      } finally {
        if (!cancelled) {
          setLoading(false)
        }
      }
    }

    restore()
    return () => {
      cancelled = true
    }
  }, [token])

  const value = useMemo(() => ({
    apiBase: API_BASE,
    token,
    user,
    loading,
    error,
    setError,
    async loginWithGoogleIdToken(idToken, customer = false) {
      setError('')
      const res = await fetch(`${API_BASE}/api/auth/${customer ? 'customer/' : ''}google`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ idToken }),
      })
      const body = await res.json().catch(() => ({}))
      if (!res.ok) {
        throw new Error(body.error || 'Google sign-in failed.')
      }
      localStorage.setItem(STORAGE_KEY, body.token)
      setToken(body.token)
      setUser(body.user)
      return body.user
    },
    logout() {
      localStorage.removeItem(STORAGE_KEY)
      setToken('')
      setUser(null)
      setError('')
    },
  }), [token, user, loading, error])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) {
    throw new Error('useAuth must be used inside AuthProvider')
  }
  return ctx
}
