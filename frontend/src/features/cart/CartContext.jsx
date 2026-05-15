import { createContext, useContext, useMemo, useState } from 'react';

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const [items, setItems] = useState(() => JSON.parse(localStorage.getItem('tqsport_cart') || '[]'));

  function persist(next) {
    setItems(next);
    localStorage.setItem('tqsport_cart', JSON.stringify(next));
  }

  const value = useMemo(() => ({
    items,
    itemCount: items.reduce((sum, item) => sum + item.quantity, 0),
    total: items.reduce((sum, item) => sum + item.price * item.quantity, 0),
    addItem(product, size = 'M') {
      persist(items.some((item) => item.id === product.id && item.size === size)
        ? items.map((item) => item.id === product.id && item.size === size ? { ...item, quantity: item.quantity + 1 } : item)
        : [...items, { ...product, quantity: 1, size }]);
    },
    updateQuantity(id, quantity) {
      persist(items.map((item) => item.id === id ? { ...item, quantity } : item).filter((item) => item.quantity > 0));
    },
    clear() { persist([]); },
  }), [items]);

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export const useCart = () => useContext(CartContext);
