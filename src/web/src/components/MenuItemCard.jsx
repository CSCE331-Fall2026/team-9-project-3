import { useEffect, useState } from 'react'
import { describeItem, formatPrice } from '../lib/menu'
import { menuImageFallback, menuImageSrc } from '../lib/menuImages'
import './menu.css'
import './order.css'

// One menu item. Customer cards lead with a picture; cashier stays compact.
function MenuItemCard({ item, variant = 'customer', onAdd, addDisabled = false }) {
  const detail = describeItem(item)
  const [src, setSrc] = useState(() => menuImageSrc(item))
  const showImage = variant === 'customer'

  useEffect(() => {
    setSrc(menuImageSrc(item))
  }, [item])

  function handleImageError() {
    const fallback = menuImageFallback(item)
    if (src !== fallback) {
      setSrc(fallback)
    }
  }

  return (
    <li className={`menu-card menu-card--${variant}`}>
      {showImage ? (
        <div className="menu-card__media">
          <img
            className="menu-card__image"
            src={src}
            alt=""
            loading="lazy"
            onError={handleImageError}
          />
        </div>
      ) : null}

      <div className="menu-card__body">
        <span className="menu-card__name">{item.name}</span>
        <span className="menu-card__category">{item.category}</span>
        {detail ? <span className="menu-card__detail">{detail}</span> : null}
        <div className="menu-card__footer">
          <span className="menu-card__price">{formatPrice(item)}</span>
          {onAdd ? (
            <button
              type="button"
              className="add-button"
              onClick={() => onAdd(item)}
              disabled={addDisabled}
              aria-label={`Add ${item.name} to cart`}
            >
              Add
            </button>
          ) : null}
        </div>
      </div>
    </li>
  )
}

export default MenuItemCard
