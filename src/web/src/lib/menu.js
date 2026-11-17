// Seed data has a placeholder row in category "Category" (the Appetizer header). Not a real item.
const HIDDEN_CATEGORIES = ['Category']

// Standard entrees are included with a meal. A higher base price is the upcharge.
export function formatPrice(item) {
  const price = Number(item.basePrice)
  if (item.category === 'Entree' && price === 0) {
    return 'Included'
  }
  if (item.category === 'Entree' && price > 0) {
    return `+$${price.toFixed(2)}`
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

const CATEGORY_ORDER = ['Size', 'Entree', 'Side', 'Appetizer', 'Drink']

// Sizes first, then what goes in the meal, then add-ons.
export function getCategories(items) {
  const present = new Set(items.map((item) => item.category))
  const ordered = CATEGORY_ORDER.filter((category) => present.has(category))
  for (const category of present) {
    if (!ordered.includes(category)) {
      ordered.push(category)
    }
  }
  return ordered
}
