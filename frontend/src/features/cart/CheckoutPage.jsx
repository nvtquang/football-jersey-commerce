import { CheckCircle2 } from 'lucide-react';
import { useState } from 'react';
import { orderApi } from '../../lib/tqsportApi.js';
import { useCart } from './CartContext.jsx';
import { useToast } from '../../components/ui/ToastProvider.jsx';

export default function CheckoutPage() {
  const [form, setForm] = useState({
    recipientName: 'Nguyễn Văn TQ',
    recipientPhone: '0900000000',
    shippingAddress: 'Quận 1, TP. Hồ Chí Minh',
    note: '',
  });
  const { clear } = useCart();
  const toast = useToast();

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function placeOrder() {
    try {
      await orderApi.checkout(form);
      clear();
      toast.success('Đặt hàng thành công', 'Đơn hàng đã chuyển sang trạng thái chờ xử lý.');
    } catch (error) {
      toast.error('Không thể đặt hàng', error.message);
    }
  }

  return (
    <main className="page checkout-grid">
      <form className="checkout-form">
        <h1>Thanh toán</h1>
        <label>Họ tên<input required value={form.recipientName} onChange={(event) => updateField('recipientName', event.target.value)} /></label>
        <label>Số điện thoại<input required value={form.recipientPhone} onChange={(event) => updateField('recipientPhone', event.target.value)} /></label>
        <label>Địa chỉ giao hàng<textarea required value={form.shippingAddress} onChange={(event) => updateField('shippingAddress', event.target.value)} /></label>
        <label>Ghi chú<textarea placeholder="In tên, số áo, yêu cầu gói quà..." value={form.note} onChange={(event) => updateField('note', event.target.value)} /></label>
      </form>
      <aside className="summary-panel">
        <CheckCircle2 size={30} />
        <h2>Hoàn tất đặt hàng</h2>
        <p className="muted">Đơn hàng sẽ được lưu vào lịch sử và chuyển sang trạng thái chờ xử lý.</p>
        <button className="primary block" onClick={placeOrder}>Đặt hàng</button>
      </aside>
    </main>
  );
}
