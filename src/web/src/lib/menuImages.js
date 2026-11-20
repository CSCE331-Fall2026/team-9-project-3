// Resolve a picture for a menu card.
// Prefer the URL from Neon (imageUrl). If that is missing or fails to load,
// fall back to the local official photo, then a category placeholder.

const CATEGORY_PLACEHOLDER = {
  Size: '/menu/placeholders/size.svg',
  Entree: '/menu/placeholders/entree.svg',
  Side: '/menu/placeholders/side.svg',
  Appetizer: '/menu/placeholders/appetizer.svg',
  Drink: '/menu/placeholders/drink.svg',
}

const DEFAULT_PLACEHOLDER = '/menu/placeholders/default.svg'

export function menuImageSrc(item) {
  if (item?.imageUrl) {
    return item.imageUrl
  }
  if (item?.id) {
    return `/menu/items/${item.id}.jpg`
  }
  return CATEGORY_PLACEHOLDER[item?.category] || DEFAULT_PLACEHOLDER
}

export function menuImageFallback(item) {
  return CATEGORY_PLACEHOLDER[item?.category] || DEFAULT_PLACEHOLDER
}
