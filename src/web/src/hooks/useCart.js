import { useEffect, useState } from 'react'
import {
  addItem,
  changeExtra,
  emptyTicket,
  removeEntree,
  removeMeal,
  removeSide,
  setActiveMeal,
} from '../lib/ticket'

const STORAGE_PREFIX = 'team9.customer.cart.v2.'

function storageKey(email) {
  return `${STORAGE_PREFIX}${email}`
}

function readTicket(email) {
  if (!email) {
    return emptyTicket()
  }
  try {
    const raw = sessionStorage.getItem(storageKey(email))
    const parsed = raw ? JSON.parse(raw) : null
    if (!parsed || !Array.isArray(parsed.meals) || !Array.isArray(parsed.extras)) {
      return emptyTicket()
    }
    return {
      meals: parsed.meals,
      extras: parsed.extras,
      activeMealId: parsed.activeMealId || null,
      error: '',
    }
  } catch {
    return emptyTicket()
  }
}

function writeTicket(email, ticket) {
  if (!email) {
    return
  }
  sessionStorage.setItem(storageKey(email), JSON.stringify({
    meals: ticket.meals,
    extras: ticket.extras,
    activeMealId: ticket.activeMealId,
  }))
}

// Personal cart for one customer. A refresh keeps it for this browser tab.
// It is not submitted from here.
export function useCart(email) {
  const [ticket, setTicket] = useState(() => readTicket(email))

  useEffect(() => {
    setTicket(readTicket(email))
  }, [email])

  function apply(change) {
    setTicket((current) => {
      const next = change(current)
      writeTicket(email, next)
      return next
    })
  }

  return {
    ticket,
    addItem: (item) => apply((current) => addItem(current, item)),
    removeSide: (mealId, index) => apply((current) => removeSide(current, mealId, index)),
    removeEntree: (mealId, index) => apply((current) => removeEntree(current, mealId, index)),
    removeMeal: (mealId) => apply((current) => removeMeal(current, mealId)),
    setActiveMeal: (mealId) => apply((current) => setActiveMeal(current, mealId)),
    changeExtra: (itemId, quantity) => apply((current) => changeExtra(current, itemId, quantity)),
    clearCart: () => apply(() => emptyTicket()),
  }
}
