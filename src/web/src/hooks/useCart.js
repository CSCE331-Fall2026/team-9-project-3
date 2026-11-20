import { useEffect, useState } from 'react'
import { savePickupOrder } from '../api/orders'
import {
  addItem,
  changeExtra,
  emptyTicket,
  removeEntree,
  removeMeal,
  removeSide,
  setActiveMeal,
  toOrderPayload,
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
export function useCart(email, token) {
  const [ticket, setTicket] = useState(() => readTicket(email))
  const [saving, setSaving] = useState(false)
  const [savedOrder, setSavedOrder] = useState(null)

  useEffect(() => {
    setTicket(readTicket(email))
    setSavedOrder(null)
  }, [email])

  function apply(change) {
    setSavedOrder(null)
    setTicket((current) => {
      const next = change(current)
      writeTicket(email, next)
      return next
    })
  }

  async function save() {
    if (saving) return
    if (!token) {
      setTicket((current) => ({
        ...current,
        error: 'Sign in again to place a pickup order.',
      }))
      return
    }

    let payload
    try {
      payload = toOrderPayload(ticket)
    } catch (err) {
      setTicket((current) => ({ ...current, error: String(err.message || err) }))
      return
    }

    setSaving(true)
    setTicket((current) => ({ ...current, error: '' }))
    try {
      const order = await savePickupOrder(payload, token)
      setSavedOrder(order)
      setTicket(emptyTicket())
      try {
        sessionStorage.removeItem(storageKey(email))
      } catch {
        // The order is already placed even if browser storage is unavailable.
      }
    } catch (err) {
      setTicket((current) => ({ ...current, error: String(err.message || err) }))
    } finally {
      setSaving(false)
    }
  }

  return {
    ticket,
    saving,
    savedOrder,
    save,
    addItem: (item) => apply((current) => addItem(current, item)),
    removeSide: (mealId, index) => apply((current) => removeSide(current, mealId, index)),
    removeEntree: (mealId, index) => apply((current) => removeEntree(current, mealId, index)),
    removeMeal: (mealId) => apply((current) => removeMeal(current, mealId)),
    setActiveMeal: (mealId) => apply((current) => setActiveMeal(current, mealId)),
    changeExtra: (itemId, quantity) => apply((current) => changeExtra(current, itemId, quantity)),
    clearCart: () => apply(() => emptyTicket()),
  }
}
