import { Link } from 'react-router-dom';
import { Plus } from 'lucide-react';
import { formatCurrency } from '../../lib/mockData.js';
import { useCart } from '../../features/cart/CartContext.jsx';
import { useToast } from './ToastProvider.jsx';

export default function ProductCard({ product }) {
  const { addItem } = useCart();
  const toast = useToast();
  const image = product.imageUrl || product.image;

  function handleAddToCart() {
    addItem(product);
    toast.success('Đã thêm vào giỏ hàng', product.name);
  }

  return (
    <article className="product-card">
      <Link to={`/products/${product.slug}`} className="product-image">
        <img src={image} alt={product.name} loading="lazy" />
        <div className="badge-row">{(product.badges || [product.featured ? 'Featured' : product.status || 'ACTIVE']).filter(Boolean).map((badge) => <span key={badge}>{badge}</span>)}</div>
      </Link>
      <div className="product-body">
        <p className="kicker">{product.team} · {product.category}</p>
        <h3>{product.name}</h3>
        <div className="product-meta">
          <strong>{formatCurrency(product.price)}</strong>
          <span>{product.rating} ★</span>
        </div>
        <button className="primary small" onClick={handleAddToCart}>
          <Plus size={16} /> Thêm vào giỏ
        </button>
      </div>
    </article>
  );
}
