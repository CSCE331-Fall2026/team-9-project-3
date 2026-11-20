// The time windows a manager can pick, in days.
export const SALES_WINDOWS = [
  { days: 1, label: 'Last 24 hours' },
  { days: 7, label: 'Last 7 days' },
  { days: 30, label: 'Last 30 days' },
]

export function windowLabel(days) {
  const match = SALES_WINDOWS.find((window) => window.days === days)
  return match ? match.label : `Last ${days} days`
}

// Turns minutes into short text like "just now", "5 min ago", "3 hr ago", or "2 days ago".
export function timeAgo(minutes) {
  if (minutes < 1) {
    return 'just now'
  }
  if (minutes < 60) {
    return `${minutes} min ago`
  }
  const hours = Math.floor(minutes / 60)
  if (hours < 24) {
    return `${hours} hr ago`
  }
  const days = Math.floor(hours / 24)
  return days === 1 ? '1 day ago' : `${days} days ago`
}
