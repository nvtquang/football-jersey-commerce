export const teams = ['Manchester United', 'Real Madrid', 'Barcelona', 'Vietnam', 'France', 'Argentina'];

export const categories = ['Áo sân nhà', 'Áo sân khách', 'Bộ tập', 'Áo khoác', 'Phụ kiện'];

export const products = [
  {
    id: 1,
    slug: 'vietnam-home-2026',
    name: 'Áo tuyển Việt Nam sân nhà 2026',
    team: 'Vietnam',
    category: 'Áo sân nhà',
    price: 890000,
    rating: 4.9,
    stock: 42,
    image: 'https://images.unsplash.com/photo-1517466787929-bc90951d0974?auto=format&fit=crop&w=900&q=80',
    badges: ['Mới', 'Bán chạy'],
  },
  {
    id: 2,
    slug: 'real-madrid-training-black',
    name: 'Bộ tập Real Madrid Pro Black',
    team: 'Real Madrid',
    category: 'Bộ tập',
    price: 1190000,
    rating: 4.8,
    stock: 18,
    image: 'https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=900&q=80',
    badges: ['Pro Fit'],
  },
  {
    id: 3,
    slug: 'argentina-away-heritage',
    name: 'Áo Argentina sân khách Heritage',
    team: 'Argentina',
    category: 'Áo sân khách',
    price: 990000,
    rating: 4.7,
    stock: 26,
    image: 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?auto=format&fit=crop&w=900&q=80',
    badges: ['Limited'],
  },
  {
    id: 4,
    slug: 'barcelona-windbreaker',
    name: 'Áo khoác Barcelona Travel',
    team: 'Barcelona',
    category: 'Áo khoác',
    price: 1490000,
    rating: 4.6,
    stock: 12,
    image: 'https://images.unsplash.com/photo-1579952363873-27f3bade9f55?auto=format&fit=crop&w=900&q=80',
    badges: ['Travel'],
  },
];

export const formatCurrency = (value) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);

