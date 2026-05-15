import { useEffect, useState } from 'react';
import { formatCurrency } from '../../lib/mockData.js';
import { orderApi } from '../../lib/tqsportApi.js';

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    orderApi.history().then(setOrders).catch(() => setOrders([]));
  }, []);

  return (
    <main className="page">
      <p className="kicker">Member area</p>
      <h1>Lịch sử đơn hàng</h1>
      <div className="table-wrap">
        <table>
          <thead><tr><th>Mã đơn</th><th>Khách hàng</th><th>Trạng thái</th><th>Tổng</th></tr></thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order.id || order.orderCode}>
                <td>{order.orderCode}</td>
                <td>{order.customer}</td>
                <td><span className="status warning">{order.status}</span></td>
                <td>{formatCurrency(order.totalAmount || 0)}</td>
              </tr>
            ))}
            {orders.length === 0 && <tr><td colSpan="4">Chưa có đơn hàng.</td></tr>}
          </tbody>
        </table>
      </div>
    </main>
  );
}
