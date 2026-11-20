import { apiGet } from './client'

// Manager sales summary for the last 1, 7, or 30 days.
// Returns { days, orderCount, salesTotal, averageOrder, recentOrders }.
export function fetchSalesSummary(days, token) {
  return apiGet(`/api/manager/sales-summary?days=${days}`, token)
}
