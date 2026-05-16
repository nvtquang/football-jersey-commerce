import { CheckCircle2 } from 'lucide-react';
import { useState } from 'react';
import { formatCurrency } from '../../lib/mockData.js';
import { orderApi } from '../../lib/tqsportApi.js';
import { useCart } from './CartContext.jsx';
import { useToast } from '../../components/ui/ToastProvider.jsx';

export default function CheckoutPage() {
  const [form, setForm] = useState({
    recipientName: '',
    recipientPhone: '',
    shippingAddress: '',
    note: '',
  });
  const [errors, setErrors] = useState({});
  const { items, total, clear } = useCart();
  const toast = useToast();

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
    if (errors[field] && value.trim()) {
      setErrors((current) => {
        const next = { ...current };
        delete next[field];
        return next;
      });
    }
  }

  function validateForm() {
    const nextErrors = {};
    if (!form.recipientName.trim()) nextErrors.recipientName = 'Vui lòng nhập họ tên.';
    if (!form.recipientPhone.trim()) nextErrors.recipientPhone = 'Vui lòng nhập số điện thoại.';
    if (!form.shippingAddress.trim()) nextErrors.shippingAddress = 'Vui lòng nhập địa chỉ giao hàng.';
    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  async function placeOrder(event) {
    event.preventDefault();
    if (!validateForm()) {
      toast.error('Thiếu thông tin thanh toán', 'Vui lòng nhập đầy đủ các trường bắt buộc.');
      return;
    }

    try {
      await orderApi.checkout({
        recipientName: form.recipientName.trim(),
        recipientPhone: form.recipientPhone.trim(),
        shippingAddress: form.shippingAddress.trim(),
        note: form.note.trim(),
        items: items.map((item) => ({
          productId: item.id,
          size: item.size,
          quantity: item.quantity,
        })),
      });
      clear();
      toast.success('Đặt hàng thành công', 'Đơn hàng đã chuyển sang trạng thái chờ xử lý.');
    } catch (error) {
      toast.error('Không thể đặt hàng', error.message);
    }
  }

  return (
    <main className="page checkout-grid">
      <form id="checkout-form" className="checkout-form" noValidate onSubmit={placeOrder}>
        <h1>Thanh toán</h1>
        <label>
          <span className="field-label">Họ tên <span className="required-mark">*</span></span>
          <input required value={form.recipientName} onChange={(event) => updateField('recipientName', event.target.value)} aria-invalid={Boolean(errors.recipientName)} aria-describedby="recipientName-error" />
          {errors.recipientName && <small className="field-error" id="recipientName-error">{errors.recipientName}</small>}
        </label>
        <label>
          <span className="field-label">Số điện thoại <span className="required-mark">*</span></span>
          <input required value={form.recipientPhone} onChange={(event) => updateField('recipientPhone', event.target.value)} aria-invalid={Boolean(errors.recipientPhone)} aria-describedby="recipientPhone-error" />
          {errors.recipientPhone && <small className="field-error" id="recipientPhone-error">{errors.recipientPhone}</small>}
        </label>
        <label>
          <span className="field-label">Địa chỉ giao hàng <span className="required-mark">*</span></span>
          <textarea required value={form.shippingAddress} onChange={(event) => updateField('shippingAddress', event.target.value)} aria-invalid={Boolean(errors.shippingAddress)} aria-describedby="shippingAddress-error" />
          {errors.shippingAddress && <small className="field-error" id="shippingAddress-error">{errors.shippingAddress}</small>}
        </label>
        <label>
          <span className="field-label">Ghi chú <span className="optional-mark">Không bắt buộc</span></span>
          <textarea placeholder="In tên, số áo, yêu cầu gói quà..." value={form.note} onChange={(event) => updateField('note', event.target.value)} />
        </label>
      </form>
      <aside className="summary-panel">
        <CheckCircle2 size={30} />
        <h2>Hoàn tất đặt hàng</h2>
        <p className="muted">Đơn hàng sẽ được lưu vào lịch sử và chuyển sang trạng thái chờ xử lý.</p>
        <div className="summary-row"><span>Tổng đơn</span><strong>{formatCurrency(total)}</strong></div>
        <button className="primary block" type="submit" form="checkout-form" disabled={items.length === 0}>Đặt hàng</button>
      </aside>
    </main>
  );
}
