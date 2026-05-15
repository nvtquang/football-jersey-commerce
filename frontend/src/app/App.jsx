import { Navigate, Route, Routes } from 'react-router-dom';
import ErrorBoundary from '../components/ErrorBoundary.jsx';
import PublicLayout from '../components/PublicLayout.jsx';
import AdminLayout from '../features/admin/AdminLayout.jsx';
import ProtectedRoute from '../features/auth/ProtectedRoute.jsx';
import LoginPage from '../features/auth/LoginPage.jsx';
import RegisterPage from '../features/auth/RegisterPage.jsx';
import CartPage from '../features/cart/CartPage.jsx';
import CheckoutPage from '../features/cart/CheckoutPage.jsx';
import AdminDashboard from '../features/admin/AdminDashboard.jsx';
import AdminProducts from '../features/admin/AdminProducts.jsx';
import AdminOrders from '../features/admin/AdminOrders.jsx';
import AdminContent from '../features/admin/AdminContent.jsx';
import AdminManagement from '../features/admin/AdminManagement.jsx';
import HomePage from '../features/store/HomePage.jsx';
import ProductsPage from '../features/store/ProductsPage.jsx';
import ProductDetailPage from '../features/store/ProductDetailPage.jsx';
import OrdersPage from '../features/store/OrdersPage.jsx';

export default function App() {
  return (
    <ErrorBoundary>
      <Routes>
        <Route element={<PublicLayout />}>
          <Route index element={<HomePage />} />
          <Route path="products" element={<ProductsPage />} />
          <Route path="products/:slug" element={<ProductDetailPage />} />
          <Route path="cart" element={<CartPage />} />
          <Route path="checkout" element={<CheckoutPage />} />
          <Route path="orders" element={<OrdersPage />} />
          <Route path="login" element={<LoginPage />} />
          <Route path="register" element={<RegisterPage />} />
        </Route>
        <Route
          path="admin"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<AdminDashboard />} />
          <Route path="products" element={<AdminProducts />} />
          <Route path="orders" element={<AdminOrders />} />
          <Route path="content" element={<AdminContent />} />
          <Route path="management" element={<AdminManagement />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </ErrorBoundary>
  );
}
