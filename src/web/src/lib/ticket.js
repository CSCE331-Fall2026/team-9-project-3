// Panda meals have a fixed number of sides and entrees.
// A full meal does not grow into extra entrees. Start another size instead.
// The same rules are checked again in OrderDao when a cashier saves.
const MEAL_SLOTS = {
  Bowl: { sides: 1, entrees: 1 },
  Plate: { sides: 1, entrees: 2 },
  'Bigger Plate': { sides: 1, entrees: 3 },
  'Kids Meal': { sides: 1, entrees: 1 },
  'A La Carte': { sides: 0, entrees: 1 },
}

export function emptyTicket() {
  return { meals: [], extras: [], activeMealId: null, error: '' }
}

export function isMealSize(item) {
  return item?.category === 'Size' && Boolean(MEAL_SLOTS[item.name])
}

function snap(item) {
  return {
    id: item.id,
    name: item.name,
    basePrice: Number(item.basePrice),
    category: item.category,
  }
}

function clone(ticket) {
  return {
    meals: ticket.meals.map((meal) => ({
      id: meal.id,
      size: { ...meal.size },
      sides: meal.sides.map((item) => ({ ...item })),
      entrees: meal.entrees.map((item) => ({ ...item })),
    })),
    extras: ticket.extras.map((line) => ({
      item: { ...line.item },
      quantity: line.quantity,
    })),
    activeMealId: ticket.activeMealId,
    error: '',
  }
}

function activeMeal(ticket) {
  return ticket.meals.find((meal) => meal.id === ticket.activeMealId) || null
}

function newMealId() {
  return `meal-${Date.now().toString(36)}-${Math.random().toString(36).slice(2, 7)}`
}

export function slotsFor(sizeName) {
  return MEAL_SLOTS[sizeName] || null
}

// A meal is complete when its side and entree counts exactly match its size.
export function mealComplete(meal) {
  const slots = slotsFor(meal.size.name)
  if (!slots) {
    return false
  }
  return meal.sides.length === slots.sides && meal.entrees.length === slots.entrees
}

// Size price plus any premium entree upcharge. Included sides add nothing.
export function mealPrice(meal) {
  const premiums = meal.entrees.reduce((sum, item) => sum + Number(item.basePrice || 0), 0)
  return Number(meal.size.basePrice) + premiums
}

export function ticketSubtotal(ticket) {
  const meals = ticket.meals.reduce((sum, meal) => sum + mealPrice(meal), 0)
  const extras = ticket.extras.reduce(
    (sum, line) => sum + Number(line.item.basePrice) * line.quantity,
    0,
  )
  return meals + extras
}

export function mealStatus(meal) {
  const slots = slotsFor(meal.size.name)
  if (!slots) {
    return 'Not a meal'
  }
  const needSide = slots.sides - meal.sides.length
  const needEntree = slots.entrees - meal.entrees.length
  if (needSide <= 0 && needEntree <= 0) {
    return 'Ready'
  }
  const parts = []
  if (needSide > 0) {
    parts.push(`${needSide} side`)
  }
  if (needEntree > 0) {
    parts.push(`${needEntree} entree${needEntree === 1 ? '' : 's'}`)
  }
  return `Needs ${parts.join(' and ')}`
}

export function isEmptyTicket(ticket) {
  return ticket.meals.length === 0 && ticket.extras.length === 0
}

function entreeFullMessage(meal, slots) {
  if (meal.size.name === 'Plate') {
    return 'A plate includes 2 entrees. Remove one, or start a Bigger Plate.'
  }
  if (meal.size.name === 'Bigger Plate') {
    return 'A bigger plate includes 3 entrees. Remove one, or start another meal.'
  }
  if (meal.size.name === 'A La Carte') {
    return 'A la carte is one entree. Remove it, or start a plate for a full meal.'
  }
  return `${meal.size.name} includes ${slots.entrees} entree. Remove it, or start a Plate.`
}

function addExtra(ticket, item) {
  const existing = ticket.extras.find((line) => line.item.id === item.id)
  if (existing) {
    existing.quantity += 1
    return ticket
  }
  ticket.extras.push({ item: snap(item), quantity: 1 })
  return ticket
}

export function addItem(ticket, item) {
  const next = clone(ticket)

  if (isMealSize(item)) {
    const current = activeMeal(next)
    if (current && current.sides.length === 0 && current.entrees.length === 0) {
      current.size = snap(item)
      return next
    }
    const meal = { id: newMealId(), size: snap(item), sides: [], entrees: [] }
    next.meals.push(meal)
    next.activeMealId = meal.id
    return next
  }

  if (item.category === 'Entree') {
    const meal = activeMeal(next)
    if (!meal) {
      next.error = 'Choose a size first.'
      return next
    }
    const slots = slotsFor(meal.size.name)
    if (meal.entrees.length >= slots.entrees) {
      next.error = entreeFullMessage(meal, slots)
      return next
    }
    meal.entrees.push(snap(item))
    return next
  }

  if (item.category === 'Side') {
    const meal = activeMeal(next)
    const slots = meal ? slotsFor(meal.size.name) : null
    if (meal && slots && meal.sides.length < slots.sides) {
      meal.sides.push(snap(item))
      return next
    }
    return addExtra(next, item)
  }

  if (item.category === 'Appetizer' || item.category === 'Drink') {
    return addExtra(next, item)
  }

  next.error = 'That item is not on the menu.'
  return next
}

export function removeSide(ticket, mealId, index) {
  const next = clone(ticket)
  const meal = next.meals.find((row) => row.id === mealId)
  if (meal) {
    meal.sides.splice(index, 1)
  }
  return next
}

export function removeEntree(ticket, mealId, index) {
  const next = clone(ticket)
  const meal = next.meals.find((row) => row.id === mealId)
  if (meal) {
    meal.entrees.splice(index, 1)
  }
  return next
}

export function removeMeal(ticket, mealId) {
  const next = clone(ticket)
  next.meals = next.meals.filter((meal) => meal.id !== mealId)
  if (next.activeMealId === mealId) {
    next.activeMealId = next.meals.length > 0 ? next.meals[next.meals.length - 1].id : null
  }
  return next
}

export function setActiveMeal(ticket, mealId) {
  const next = clone(ticket)
  next.activeMealId = mealId
  return next
}

export function changeExtra(ticket, itemId, quantity) {
  const next = clone(ticket)
  const qty = Number(quantity)
  if (!Number.isFinite(qty)) {
    return next
  }
  if (qty <= 0) {
    next.extras = next.extras.filter((line) => line.item.id !== itemId)
    return next
  }
  next.extras = next.extras.map((line) => (
    line.item.id === itemId ? { ...line, quantity: qty } : line
  ))
  return next
}

export function toOrderPayload(ticket) {
  const open = ticket.meals.find((meal) => !mealComplete(meal))
  if (open) {
    throw new Error(`Finish the ${open.size.name} (${mealStatus(open).toLowerCase()}) or remove it.`)
  }
  if (isEmptyTicket(ticket)) {
    throw new Error('Add a meal or an item before saving.')
  }
  return {
    meals: ticket.meals.map((meal) => ({
      sizeId: meal.size.id,
      sideIds: meal.sides.map((item) => item.id),
      entreeIds: meal.entrees.map((item) => item.id),
    })),
    items: ticket.extras.map((line) => ({
      menuItemId: line.item.id,
      quantity: line.quantity,
    })),
  }
}
