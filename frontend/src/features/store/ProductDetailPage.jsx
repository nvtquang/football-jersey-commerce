import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { RotateCcw, Truck } from 'lucide-react';
import { formatCurrency, products as fallbackProducts } from '../../lib/mockData.js';
import { productApi } from '../../lib/tqsportApi.js';
import { useCart } from '../cart/CartContext.jsx';
import { useToast } from '../../components/ui/ToastProvider.jsx';

export default function ProductDetailPage() {
  const { slug } = useParams();
  const [product, setProduct] = useState(() => fallbackProducts.find((item) => item.slug === slug) ?? fallbackProducts[0]);
  const [selectedSize, setSelectedSize] = useState('M');
  const { addItem } = useCart();
  const toast = useToast();

  useEffect(() => {
    productApi.detail(slug).then(setProduct).catch(() => {
      setProduct(fallbackProducts.find((item) => item.slug === slug) ?? fallbackProducts[0]);
    });
  }, [slug]);

  function handleAddToCart() {
    addItem(product, selectedSize);
    toast.success('Đã thêm vào giỏ hàng', `${product.name} · Size ${selectedSize}`);
  }

  return (
    <main className="page detail-layout">
      <div className="gallery">
        <img src={product.imageUrl || product.image} alt={product.name} />
      </div>
      <section className="detail-info">
        <p className="kicker">{product.team} · {product.category}</p>
        <h1>{product.name}</h1>
        <strong className="price">{formatCurrency(product.price)}</strong>
        <p className="muted">Chất liệu thoáng khí, form thể thao, hỗ trợ in tên và số áo. Hiển thị tiếng Việt chuẩn UTF-8 trên toàn hệ thống.</p>
        <div className="size-grid">
          {['S', 'M', 'L', 'XL', '2XL'].map((size) => (
            <button className={selectedSize === size ? 'selected' : ''} key={size} onClick={() => setSelectedSize(size)} type="button" aria-pressed={selectedSize === size}>
              {size}
            </button>
          ))}
        </div>
        <button className="primary" onClick={handleAddToCart}>Thêm vào giỏ</button>
        <div className="service-list">
          <span><Truck size={18} /> Giao nhanh toàn quốc</span>
          <span><RotateCcw size={18} /> Đổi size trong 7 ngày</span>
        </div>
      </section>
    </main>
  );
}
