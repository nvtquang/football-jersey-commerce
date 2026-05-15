import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

const resources = {
  vi: {
    translation: {
      nav: { home: 'Trang chủ', products: 'Sản phẩm', cart: 'Giỏ hàng', orders: 'Đơn hàng', admin: 'Quản trị' },
      heroTitle: 'TQSport',
      heroCopy: 'Áo đấu, bộ tập và thời trang bóng đá chính hãng cho câu lạc bộ và đội tuyển quốc gia.',
      shopNow: 'Mua ngay',
      featured: 'Bộ sưu tập nổi bật',
      trending: 'Áo đấu đang thịnh hành',
      browseClub: 'Mua theo câu lạc bộ',
      search: 'Tìm áo đấu, đội bóng, mùa giải',
      filters: 'Bộ lọc',
      checkout: 'Thanh toán',
      emptyCart: 'Giỏ hàng của bạn đang trống.',
      adminTitle: 'Bảng điều khiển TQSport',
      products: 'Sản phẩm',
      orders: 'Đơn hàng',
      users: 'Người dùng',
      revenue: 'Doanh thu',
    },
  },
  en: {
    translation: {
      nav: { home: 'Home', products: 'Products', cart: 'Cart', orders: 'Orders', admin: 'Admin' },
      heroTitle: 'TQSport',
      heroCopy: 'Premium football jerseys, training kits, and apparel for clubs and national teams.',
      shopNow: 'Shop now',
      featured: 'Featured collections',
      trending: 'Trending jerseys',
      browseClub: 'Browse by club',
      search: 'Search jerseys, teams, season',
      filters: 'Filters',
      checkout: 'Checkout',
      emptyCart: 'Your cart is empty.',
      adminTitle: 'TQSport Dashboard',
      products: 'Products',
      orders: 'Orders',
      users: 'Users',
      revenue: 'Revenue',
    },
  },
};

i18n.use(initReactI18next).init({
  resources,
  lng: 'vi',
  fallbackLng: 'vi',
  interpolation: { escapeValue: false },
});

export default i18n;

