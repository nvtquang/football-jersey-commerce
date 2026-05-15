import { NavLink, Outlet } from 'react-router-dom';
import { BarChart3, Boxes, Image, ReceiptText, Settings2 } from 'lucide-react';

export default function AdminLayout() {
  return (
    <div className="admin-shell">
      <aside className="admin-sidebar">
        <strong>TQSport Admin</strong>
        <NavLink to="/admin"><BarChart3 size={18} /> Dashboard</NavLink>
        <NavLink to="/admin/products"><Boxes size={18} /> Products</NavLink>
        <NavLink to="/admin/orders"><ReceiptText size={18} /> Orders</NavLink>
        <NavLink to="/admin/content"><Image size={18} /> Content</NavLink>
        <NavLink to="/admin/management"><Settings2 size={18} /> Management</NavLink>
      </aside>
      <main className="admin-main"><Outlet /></main>
    </div>
  );
}
