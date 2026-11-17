import { useState } from 'react'
import { saveOrder } from '../api/orders'
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

// Cashier order. Meals are priced when the API saves them.
export function useOrder(token) {
  const [ticket, setTicket] = useState(emptyTicket)
  const [saving, setSaving] = useState(false)
  const [savedOrder, setSavedOrder] = useState(null)

  function apply(change) {
    setSavedOrder(null)
    setTicket((current) => change(current))
  }

  async function save() {
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
      const result = await saveOrder(payload, token)
      setSavedOrder(result)
      setTicket(emptyTicket())
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
  }
}
