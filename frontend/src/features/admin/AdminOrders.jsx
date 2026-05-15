import { useEffect, useState } from 'react';
import { formatCurrency } from '../../lib/mockData.js';
import { orderApi } from '../../lib/tqsportApi.js';
import { useToast } from '../../components/ui/ToastProvider.jsx';

const statuses = ['PENDING', 'PAID', 'SHIPPING', 'COMPLETED', 'CANCELLED'];

export default function AdminOrders() {
  const [orders, setOrders] = useState([]);
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

  return (
    <section className="admin-panel">
      <p className="kicker">Operations CRUD</p>
      <h1>Order processing</h1>
      <div className="table-wrap"><table><thead><tr><th>Order</th><th>Customer</th><th>Status</th><th>Total</th><th>Action</th></tr></thead><tbody>
        {orders.map((order) => (
          <tr key={order.id}>
            <td>{order.orderCode}</td><td>{order.customer}</td><td><span className="status warning">{order.status}</span></td><td>{formatCurrency(order.totalAmount || 0)}</td>
            <td><select value={order.status} onChange={(event) => updateOrder(order, event.target.value)}>{statuses.map((status) => <option key={status}>{status}</option>)}</select></td>
          </tr>
        ))}
        {orders.length === 0 && <tr><td colSpan="5">Chưa có đơn hàng.</td></tr>}
      </tbody></table></div>
    </section>
  );
}
