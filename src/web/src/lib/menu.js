// Seed data has a placeholder row in category "Category" (the Appetizer header). Not a real item.
const HIDDEN_CATEGORIES = ['Category']

// Entrees are priced by the plate/bowl size, so their base price is 0.00.
export function formatPrice(item) {
  const price = Number(item.basePrice)
  if (item.category === 'Entree' && price === 0) {
    return 'Included with plate/bowl'
  }
  return `$${price.toFixed(2)}`
}

// Short readable detail line, e.g. "520 cal · 25g protein · Contains: soy".
// Size/drink rows are seeded with 0 calories, so 0 is treated as "no nutrition info".
export function describeItem(item) {
  const parts = []
  if (item.calorie) {
    parts.push(`${item.calorie} cal`)
    if (item.protein) {
      parts.push(`${item.protein}g protein`)
    }
  }
  const allergens = (item.allergens || '')
    .split(',')
    .map((a) => a.trim())
    .filter(Boolean)
  if (allergens.length > 0) {
    parts.push(`Contains: ${allergens.join(', ')}`)
  }
  return parts.join(' · ')
}

// Drops hidden categories so they never reach the UI.
export function visibleItems(items) {
  return items.filter((item) => !HIDDEN_CATEGORIES.includes(item.category))
}

// Unique category names in the order they first appear (API already sorts by category).
export function getCategories(items) {
  return [...new Set(items.map((item) => item.category))]
}
