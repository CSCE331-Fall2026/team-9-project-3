import { apiGet } from './client'

export async function fetchMenu() {
  const data = await apiGet('/api/menu')
  return Array.isArray(data) ? data : []
}
