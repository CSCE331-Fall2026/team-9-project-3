export const API_BASE = (import.meta.env.VITE_API_URL || 'http://localhost:8080').replace(/\/$/, '')

// token is optional. Pass it for routes that need a signed-in user.
export async function apiGet(path, token) {
  const headers = token ? { Authorization: `Bearer ${token}` } : {}
  const res = await fetch(`${API_BASE}${path}`, { headers })
  const data = await res.json().catch(() => ({}))
  if (!res.ok) {
    throw new Error(data.error || `Request to ${path} failed (${res.status})`)
  }
  return data
}

// Sends JSON with the signed-in employee's token. Shows the API's own error message if it fails.
export async function apiPost(path, body, token) {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(body),
  })
  const data = await res.json().catch(() => ({}))
  if (!res.ok) {
    throw new Error(data.error || `Request to ${path} failed (${res.status})`)
  }
  return data
}
