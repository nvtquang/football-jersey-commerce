import { useEffect, useState } from 'react';
import { Eye } from 'lucide-react';
import { formatCurrency } from '../../lib/mockData.js';
import { orderApi } from '../../lib/tqsportApi.js';
import { useToast } from '../../components/ui/ToastProvider.jsx';

const statuses = ['PENDING', 'PAID', 'SHIPPING', 'COMPLETED', 'CANCELLED'];

export default function AdminOrders() {
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [detailLoading, setDetailLoading] = useState(false);
  const toast = useToast();

  async function loadOrders() {
    setOrders(await orderApi.adminList());
  }

  useEffect(() => {
    loadOrders().catch((error) => toast.error('Không tải được đơn hàng', error.message));
  }, []);

  async function updateOrder(order, status) {
    try {
      await orderApi.updateStatus(order.id, status);
      toast.success('Đã cập nhật đơn hàng', `${order.orderCode} → ${status}`);
      await loadOrders();
    } catch (error) {
      toast.error('Cập nhật đơn hàng thất bại', error.message);
    }
  }

  async function viewOrder(order) {
    setDetailLoading(true);
    try {
      setSelectedOrder(await orderApi.detail(order.id));
    } catch (error) {
      toast.error('Không tải được chi tiết đơn hàng', error.message);
    } finally {
      setDetailLoading(false);
    }
  }

  return (
    <section className="admin-panel">
      <p className="kicker">Operations CRUD</p>
      <h1>Order processing</h1>
      <div className="table-wrap"><table><thead><tr><th>Order</th><th>Customer</th><th>Status</th><th>Total</th><th>Action</th></tr></thead><tbody>
        {orders.map((order) => (
          <tr key={order.id}>
            <td>{order.orderCode}</td><td>{order.customer}</td><td><span className="status warning">{order.status}</span></td><td>{formatCurrency(order.totalAmount || 0)}</td>
            <td className="row-actions">
              <button onClick={() => viewOrder(order)} title="Xem chi tiết đơn hàng"><Eye size={16} /></button>
              <select value={order.status} onChange={(event) => updateOrder(order, event.target.value)}>{statuses.map((status) => <option key={status}>{status}</option>)}</select>
            </td>
          </tr>
        ))}
        {orders.length === 0 && <tr><td colSpan="5">Chưa có đơn hàng.</td></tr>}
      </tbody></table></div>

      {detailLoading && <div className="loading-panel order-detail-loading">Đang tải chi tiết đơn hàng...</div>}

      {selectedOrder && (
        <div className="modal-backdrop" role="presentation" onClick={() => setSelectedOrder(null)}>
          <section className="order-detail-modal" role="dialog" aria-modal="true" aria-label="Chi tiết đơn hàng" onClick={(event) => event.stopPropagation()}>
            <div className="section-heading">
              <div>
                <p className="kicker">Order detail</p>
                <h2>{selectedOrder.orderCode}</h2>
              </div>
              <button className="ghost" onClick={() => setSelectedOrder(null)}>Đóng</button>
            </div>

            <div className="order-detail-grid">
              <div><span>Khách hàng</span><strong>{selectedOrder.customer}</strong></div>
              <div><span>Trạng thái</span><strong>{selectedOrder.status}</strong></div>
              <div><span>Tổng tiền</span><strong>{formatCurrency(selectedOrder.totalAmount || 0)}</strong></div>
              <div><span>Ngày tạo</span><strong>{selectedOrder.createdAt ? new Date(selectedOrder.createdAt).toLocaleString('vi-VN') : '-'}</strong></div>
              <div><span>Người nhận</span><strong>{selectedOrder.recipientName || '-'}</strong></div>
              <div><span>Số điện thoại</span><strong>{selectedOrder.recipientPhone || '-'}</strong></div>
              <div className="wide"><span>Địa chỉ</span><strong>{selectedOrder.shippingAddress || '-'}</strong></div>
              <div className="wide"><span>Ghi chú</span><strong>{selectedOrder.note || '-'}</strong></div>
            </div>

            <div className="table-wrap">
              <table>
                <thead><tr><th>Sản phẩm</th><th>Size</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th></tr></thead>
                <tbody>
                  {(selectedOrder.items || []).map((item) => (
                    <tr key={item.id || `${item.productName}-${item.size}`}>
                      <td>{item.productName}</td>
                      <td>{item.size || '-'}</td>
                      <td>{formatCurrency(item.unitPrice || 0)}</td>
                      <td>{item.quantity}</td>
                      <td>{formatCurrency(item.lineTotal || Number(item.unitPrice || 0) * Number(item.quantity || 0))}</td>
                    </tr>
                  ))}
                  {(!selectedOrder.items || selectedOrder.items.length === 0) && <tr><td colSpan="5">Đơn hàng chưa có dòng sản phẩm.</td></tr>}
                </tbody>
              </table>
            </div>
          </section>
        </div>
      )}
    </section>
  );
}
