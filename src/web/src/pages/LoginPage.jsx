import { GoogleLogin } from '@react-oauth/google'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

function homeForRole(role) {
  return role === 'manager' ? '/manager' : '/cashier'
}

export default function LoginPage() {
  const { user, loading, error, setError, loginWithGoogleIdToken } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID

  if (!loading && user) {
    return <Navigate to={homeForRole(user.role)} replace />
  }

  async function handleSuccess(response) {
    try {
      const nextUser = await loginWithGoogleIdToken(response.credential)
      const redirectTo = location.state?.from || homeForRole(nextUser.role)
      navigate(redirectTo, { replace: true })
    } catch (err) {
      setError(String(err.message || err))
    }
  }

  return (
    <main className="page login-page">
      <header className="header">
        <p className="brand">Team 9 · Panda Express POS</p>
        <h1>Employee sign in</h1>
        <p className="lede">
          Use your Google account. Access is limited to emails listed as cashier or manager.
        </p>
      </header>

      <section className="login-card" aria-live="polite">
        {!clientId ? (
          <p className="error">
            Missing <code>VITE_GOOGLE_CLIENT_ID</code>. Add it to your local env and Vercel.
          </p>
        ) : (
          <GoogleLogin
            onSuccess={handleSuccess}
            onError={() => setError('Google sign-in was cancelled or failed.')}
            useOneTap={false}
          />
        )}
        {error ? <p className="error">{error}</p> : null}
      </section>
    </main>
  )
}
