import { Link, NavLink, Outlet } from 'react-router-dom';
import { Globe2, LogOut, Search, ShoppingBag, UserRound } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import { useCart } from '../features/cart/CartContext.jsx';
import { useAuth } from '../features/auth/AuthContext.jsx';
import { useToast } from './ui/ToastProvider.jsx';

export default function PublicLayout() {
  const { t, i18n } = useTranslation();
  const { itemCount } = useCart();
  const { user, logout } = useAuth();
  const toast = useToast();

  function changeLanguage() {
    const nextLanguage = i18n.language === 'vi' ? 'en' : 'vi';
    i18n.changeLanguage(nextLanguage);
    toast.info('Đã đổi ngôn ngữ', nextLanguage === 'vi' ? 'Tiếng Việt' : 'English');
  }

  function handleLogout() {
    logout();
    toast.info('Đã đăng xuất', 'Hẹn gặp lại bạn tại TQSport.');
  }

  return (
    <div className="site-shell">
      <header className="topbar">
        <Link to="/" className="brand">TQSport</Link>
        <nav className="main-nav">
          <NavLink to="/">{t('nav.home')}</NavLink>
          <NavLink to="/products">{t('nav.products')}</NavLink>
          <NavLink to="/orders">{t('nav.orders')}</NavLink>
          {user?.role === 'ADMIN' && <NavLink to="/admin">{t('nav.admin')}</NavLink>}
        </nav>
        <div className="header-actions">
          <Link to="/products" className="icon-button" aria-label="Search"><Search size={20} /></Link>
          <button className="icon-button" onClick={changeLanguage} aria-label="Change language">
            <Globe2 size={20} />
          </button>
          {user ? (
            <button className="account-chip" onClick={handleLogout} aria-label="Logout">
              <UserRound size={18} />
              <span>{user.role}</span>
              <LogOut size={16} />
            </button>
          ) : (
            <Link to="/login" className="icon-button" aria-label="Account"><UserRound size={20} /></Link>
          )}
          <Link to="/cart" className="cart-link" aria-label="Cart">
            <ShoppingBag size={20} />
            <span>{itemCount}</span>
          </Link>
        </div>
      </header>
      <Outlet />
      <footer className="footer">
        <strong>TQSport</strong>
        <span>UTF-8 ready · Mobile first · Football commerce platform</span>
      </footer>
    </div>
  );
}
