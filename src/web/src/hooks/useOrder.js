import { useState } from 'react'
import { saveOrder } from '../api/orders'

// Holds the order the cashier is building. Nothing is saved until save() is called.
// lines look like [{ item, quantity }], where item is a menu item from /api/menu.
export function useOrder(token) {
  const [started, setStarted] = useState(false)
  const [lines, setLines] = useState([])
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [savedOrder, setSavedOrder] = useState(null)

  // Starts a new, empty order.
  function startOrder() {
    setStarted(true)
    setLines([])
    setError('')
    setSavedOrder(null)
  }

  // Adds one of an item. If it is already on the order, its quantity goes up instead.
  function addItem(item) {
    if (!started) {
      return
    }
    const existing = lines.find((line) => line.item.id === item.id)
    if (existing) {
      setLines(lines.map((line) => (
        line.item.id === item.id ? { ...line, quantity: line.quantity + 1 } : line
      )))
    } else {
      setLines([...lines, { item, quantity: 1 }])
    }
  }

  // Takes one away. When the quantity reaches 0 the line is removed.
  function removeItem(itemId) {
    const updated = lines
      .map((line) => (line.item.id === itemId ? { ...line, quantity: line.quantity - 1 } : line))
      .filter((line) => line.quantity > 0)
    setLines(updated)
  }

  async function save() {
    if (lines.length === 0) {
      setError('Add at least one item before saving.')
      return
    }
    setSaving(true)
    setError('')
    try {
      const result = await saveOrder(lines, token)
      setSavedOrder(result)
      setStarted(false)
      setLines([])
    } catch (err) {
      setError(String(err.message || err))
    } finally {
      setSaving(false)
    }
  }

  return { started, lines, saving, error, savedOrder, startOrder, addItem, removeItem, save }
}
