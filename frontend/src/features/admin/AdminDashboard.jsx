import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { formatCurrency } from '../../lib/mockData.js';
import { statsApi } from '../../lib/tqsportApi.js';

export default function AdminDashboard() {
  const { t } = useTranslation();
  const [stats, setStats] = useState({ products: 0, users: 0, orders: 0, revenue: 0 });

  useEffect(() => {
    statsApi.admin().then(setStats).catch(() => setStats({ products: 0, users: 0, orders: 0, revenue: 0 }));
  }, []);

  const cards = [
    [t('revenue'), formatCurrency(stats.revenue || 0), 'API'],
    [t('orders'), stats.orders, 'API'],
    [t('products'), stats.products, 'API'],
    [t('users'), stats.users, 'API'],
  ];

  return (
    <>
      <p className="kicker">Admin analytics</p>
      <h1>{t('adminTitle')}</h1>
      <div className="stat-grid">{cards.map(([label, value, meta]) => <article className="stat-card" key={label}><span>{label}</span><strong>{value}</strong><small>{meta}</small></article>)}</div>
      <section className="admin-panel">
        <h2>Hiệu suất bán hàng</h2>
        <div className="chart-bars">{[40, 68, 52, 86, 75, 92, 64].map((height, index) => <span key={index} style={{ height: `${height}%` }} />)}</div>
      </section>
    </>
  );
}
