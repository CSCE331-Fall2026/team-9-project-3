// Price of one order line: the item's price times how many.
export function lineTotal(line) {
  return Number(line.item.basePrice) * line.quantity
}

// Running subtotal before tax for the whole order.
export function orderSubtotal(lines) {
  let total = 0
  for (const line of lines) {
    total = total + lineTotal(line)
  }
  return total
}

export function formatMoney(amount) {
  return `$${Number(amount).toFixed(2)}`
}
