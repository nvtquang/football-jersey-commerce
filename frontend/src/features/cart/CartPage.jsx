import { Link } from 'react-router-dom';
import { formatCurrency } from '../../lib/mockData.js';
import { useCart } from './CartContext.jsx';
import { useToast } from '../../components/ui/ToastProvider.jsx';

export default function CartPage() {
  const { items, total, updateQuantity } = useCart();
  const toast = useToast();

  function handleQuantityChange(item, quantity) {
    updateQuantity(item.id, quantity);
    toast.info(quantity > 0 ? 'Đã cập nhật giỏ hàng' : 'Đã xóa khỏi giỏ hàng', item.name);
  }

  return (
    <main className="page two-column">
      <section>
        <p className="kicker">TQSport checkout</p>
        <h1>Giỏ hàng</h1>
        {items.length === 0 && <p className="muted">Giỏ hàng của bạn đang trống.</p>}
        <div className="cart-list">
          {items.map((item) => (
            <article className="cart-item" key={item.id}>
              <img src={item.imageUrl || item.image} alt={item.name} />
              <div>
                <h3>{item.name}</h3>
                <p>{item.size} · {formatCurrency(item.price)}</p>
              </div>
              <input type="number" min="0" value={item.quantity} onChange={(e) => handleQuantityChange(item, Number(e.target.value))} />
            </article>
          ))}
        </div>
      </section>
      <aside className="summary-panel">
        <h2>Tóm tắt</h2>
        <div className="summary-row"><span>Tạm tính</span><strong>{formatCurrency(total)}</strong></div>
        <div className="summary-row"><span>Vận chuyển</span><strong>Miễn phí</strong></div>
        <Link className="primary block" to="/checkout">Thanh toán</Link>
      </aside>
    </main>
  );
}
