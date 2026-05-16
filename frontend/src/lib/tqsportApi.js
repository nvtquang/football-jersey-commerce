import { api, buildQuery } from './api.js';
import { categories as mockCategories, products as mockProducts, teams as mockTeams } from './mockData.js';

const STORE_KEY = 'tqsport_demo_api';
const DEMO_MODE = import.meta.env.VITE_DEMO_MODE === 'true';

const defaultUsers = [
  { id: 1, fullName: 'TQSport Admin', email: 'admin@tqsport.vn', password: '12345678', role: 'ADMIN', active: true },
  { id: 2, fullName: 'TQSport User', email: 'user@tqsport.vn', password: '12345678', role: 'USER', active: true },
  { id: 3, fullName: 'TQSport Super Admin', email: 'superadmin@tqsport.vn', password: '12345678', role: 'ADMIN', active: true },
];

const loginAliases = {
  admin: 'admin@tqsport.vn',
  'admin@': 'admin@tqsport.vn',
  superadmin: 'superadmin@tqsport.vn',
  'superadmin@': 'superadmin@tqsport.vn',
  user: 'user@tqsport.vn',
  'user@': 'user@tqsport.vn',
};

function seedState() {
  const teams = mockTeams.map((name, index) => ({
    id: index + 1,
    name,
    slug: slugify(name),
    type: ['Vietnam', 'France', 'Argentina'].includes(name) ? 'NATIONAL' : 'CLUB',
    country: 'Global',
    logoUrl: '',
  }));
  const categories = mockCategories.map((name, index) => ({ id: index + 1, name, slug: slugify(name), parentId: null }));
  const products = mockProducts.map((product) => {
    const team = teams.find((item) => item.name === product.team) || teams[0];
    const category = categories.find((item) => item.name === product.category) || categories[0];
    return {
      ...product,
      teamId: team.id,
      categoryId: category.id,
      imageUrl: product.image,
      stockQuantity: product.stock,
      status: 'ACTIVE',
      featured: product.badges?.includes('Bán chạy') || false,
    };
  });
  return {
    users: defaultUsers,
    teams,
    categories,
    products,
    banners: [{
      id: 1,
      title: 'TQSport',
      subtitle: 'Áo đấu, bộ tập và thời trang bóng đá cho câu lạc bộ và đội tuyển quốc gia.',
      imageUrl: 'https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?auto=format&fit=crop&w=1800&q=82',
      linkUrl: '/products',
      position: 'HOME_HERO',
      active: true,
      sortOrder: 1,
    }],
    orders: [],
  };
}

function readState() {
  const current = JSON.parse(localStorage.getItem(STORE_KEY) || 'null');
  if (current) {
    const mergedUsers = [
      ...defaultUsers,
      ...(current.users || []).filter((user) => !defaultUsers.some((item) => item.email.toLowerCase() === user.email?.toLowerCase())),
    ];
    const nextState = { ...seedState(), ...current, users: mergedUsers };
    writeState(nextState);
    return nextState;
  }
  const seeded = seedState();
  writeState(seeded);
  return seeded;
}

function writeState(state) {
  localStorage.setItem(STORE_KEY, JSON.stringify(state));
}

async function apiFirst(remoteCall, fallbackCall) {
  if (!DEMO_MODE) return remoteCall();
  try {
    return await remoteCall();
  } catch {
    return fallbackCall();
  }
}

function nextId(rows) {
  return Math.max(0, ...rows.map((row) => Number(row.id) || 0)) + 1;
}

function slugify(value) {
  return value.normalize('NFD').replace(/\p{Diacritic}/gu, '').toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '');
}

function normalizeEmail(value) {
  const normalized = value.trim().toLowerCase();
  return loginAliases[normalized] || normalized;
}

function findDefaultUser(email, password) {
  const normalizedEmail = normalizeEmail(email);
  const normalizedPassword = String(password).trim();
  return defaultUsers.find((user) => user.email === normalizedEmail && user.password === normalizedPassword && user.active);
}

function normalizeProduct(payload, state, existing = {}) {
  const team = state.teams.find((item) => Number(item.id) === Number(payload.teamId)) || state.teams[0];
  const category = state.categories.find((item) => Number(item.id) === Number(payload.categoryId)) || state.categories[0];
  return {
    ...existing,
    ...payload,
    slug: slugify(payload.name),
    price: Number(payload.price),
    teamId: team.id,
    team: team.name,
    categoryId: category.id,
    category: category.name,
    imageUrl: payload.imageUrl || existing.imageUrl || existing.image,
    stockQuantity: Number(payload.stockQuantity || 0),
    status: payload.status || 'ACTIVE',
    featured: Boolean(payload.featured),
  };
}

export const authApi = {
  login(payload) {
    const email = normalizeEmail(payload.email);
    const password = String(payload.password).trim();
    if (!DEMO_MODE) return api.post('/auth/login', { email, password });
    const defaultUser = findDefaultUser(email, password);
    if (defaultUser) {
      return Promise.resolve({
        token: `demo-jwt-${defaultUser.role.toLowerCase()}`,
        email: defaultUser.email,
        fullName: defaultUser.fullName,
        role: defaultUser.role,
      });
    }
    const state = readState();
    const user = state.users.find((item) => item.email.toLowerCase() === email && String(item.password).trim() === password && item.active);
    if (!user) throw new Error('Email hoặc mật khẩu không đúng.');
    return Promise.resolve({ token: `demo-jwt-${user.role.toLowerCase()}`, email: user.email, fullName: user.fullName, role: user.role });
  },
  register(payload) {
    if (!DEMO_MODE) return api.post('/auth/register', payload);
    const state = readState();
    const email = normalizeEmail(payload.email);
    if (state.users.some((item) => item.email.toLowerCase() === email)) {
      throw new Error('Email này đã được sử dụng.');
    }
    const user = { id: nextId(state.users), fullName: payload.fullName, email, password: payload.password, role: 'USER', active: true };
    state.users.push(user);
    writeState(state);
    return Promise.resolve({ token: 'demo-jwt-user', email: user.email, fullName: user.fullName, role: user.role });
  },
};

export const productApi = {
  list(params = {}) {
    return apiFirst(
      () => api.get(`/products${buildQuery(params)}`),
      () => {
        const state = readState();
        return state.products.filter((product) => {
          const matchesText = !params.q || product.name.toLowerCase().includes(params.q.toLowerCase()) || product.team.toLowerCase().includes(params.q.toLowerCase());
          const matchesTeam = !params.teamId || Number(product.teamId) === Number(params.teamId);
          const matchesCategory = !params.categoryId || Number(product.categoryId) === Number(params.categoryId);
          return product.status !== 'ARCHIVED' && matchesText && matchesTeam && matchesCategory;
        });
      },
    );
  },
  detail(slug) {
    return apiFirst(
      () => api.get(`/products/${slug}`),
      () => readState().products.find((product) => product.slug === slug) || readState().products[0],
    );
  },
  create(payload) {
    return apiFirst(
      () => api.post('/products', payload),
      () => {
        const state = readState();
        const product = normalizeProduct({ ...payload, id: nextId(state.products) }, state);
        state.products.push(product);
        writeState(state);
        return product;
      },
    );
  },
  update(id, payload) {
    return apiFirst(
      () => api.put(`/products/${id}`, payload),
      () => {
        const state = readState();
        state.products = state.products.map((product) => Number(product.id) === Number(id) ? normalizeProduct(payload, state, product) : product);
        writeState(state);
        return state.products.find((product) => Number(product.id) === Number(id));
      },
    );
  },
  remove(id) {
    return apiFirst(
      () => api.delete(`/products/${id}`),
      () => {
        const state = readState();
        state.products = state.products.map((product) => Number(product.id) === Number(id) ? { ...product, status: 'ARCHIVED' } : product);
        writeState(state);
        return null;
      },
    );
  },
};

export const catalogApi = {
  teams: () => apiFirst(() => api.get('/catalog/teams'), () => readState().teams),
  categories: () => apiFirst(() => api.get('/catalog/categories'), () => readState().categories),
  createTeam(payload) {
    return apiFirst(() => api.post('/admin/catalog/teams', payload), () => {
      const state = readState();
      const team = { id: nextId(state.teams), ...payload };
      state.teams.push(team);
      writeState(state);
      return team;
    });
  },
  updateTeam(id, payload) {
    return apiFirst(() => api.put(`/admin/catalog/teams/${id}`, payload), () => {
      const state = readState();
      state.teams = state.teams.map((team) => Number(team.id) === Number(id) ? { ...team, ...payload } : team);
      writeState(state);
      return state.teams.find((team) => Number(team.id) === Number(id));
    });
  },
  deleteTeam(id) {
    return apiFirst(() => api.delete(`/admin/catalog/teams/${id}`), () => {
      const state = readState();
      state.teams = state.teams.filter((team) => Number(team.id) !== Number(id));
      writeState(state);
      return null;
    });
  },
  createCategory(payload) {
    return apiFirst(() => api.post('/admin/catalog/categories', payload), () => {
      const state = readState();
      const category = { id: nextId(state.categories), ...payload };
      state.categories.push(category);
      writeState(state);
      return category;
    });
  },
  updateCategory(id, payload) {
    return apiFirst(() => api.put(`/admin/catalog/categories/${id}`, payload), () => {
      const state = readState();
      state.categories = state.categories.map((category) => Number(category.id) === Number(id) ? { ...category, ...payload } : category);
      writeState(state);
      return state.categories.find((category) => Number(category.id) === Number(id));
    });
  },
  deleteCategory(id) {
    return apiFirst(() => api.delete(`/admin/catalog/categories/${id}`), () => {
      const state = readState();
      state.categories = state.categories.filter((category) => Number(category.id) !== Number(id));
      writeState(state);
      return null;
    });
  },
};

export const bannerApi = {
  list: () => apiFirst(() => api.get('/banners'), () => readState().banners),
  create(payload) {
    return apiFirst(() => api.post('/banners', payload), () => {
      const state = readState();
      const banner = { id: nextId(state.banners), ...payload };
      state.banners.push(banner);
      writeState(state);
      return banner;
    });
  },
  update(id, payload) {
    return apiFirst(() => api.put(`/banners/${id}`, payload), () => {
      const state = readState();
      state.banners = state.banners.map((banner) => Number(banner.id) === Number(id) ? { ...banner, ...payload } : banner);
      writeState(state);
      return state.banners.find((banner) => Number(banner.id) === Number(id));
    });
  },
  remove(id) {
    return apiFirst(() => api.delete(`/banners/${id}`), () => {
      const state = readState();
      state.banners = state.banners.filter((banner) => Number(banner.id) !== Number(id));
      writeState(state);
      return null;
    });
  },
};

export const orderApi = {
  history: () => apiFirst(() => api.get('/orders'), () => readState().orders),
  checkout(payload) {
    return apiFirst(() => api.post('/orders', payload), () => {
      const state = readState();
      const totalAmount = (payload.items || []).reduce((sum, item) => {
        const product = state.products.find((row) => Number(row.id) === Number(item.productId));
        return sum + Number(product?.price || 0) * Number(item.quantity || 0);
      }, 0);
      const order = { id: nextId(state.orders), orderCode: `TS-${Date.now()}`, customer: payload.recipientName, status: 'PENDING', totalAmount };
      state.orders.push(order);
      writeState(state);
      return order;
    });
  },
  adminList: () => apiFirst(() => api.get('/orders/admin'), () => readState().orders),
  updateStatus(id, status) {
    return apiFirst(() => api.patch(`/orders/${id}/status`, { status }), () => {
      const state = readState();
      state.orders = state.orders.map((order) => Number(order.id) === Number(id) ? { ...order, status } : order);
      writeState(state);
      return state.orders.find((order) => Number(order.id) === Number(id));
    });
  },
};

export const userApi = {
  list: () => apiFirst(() => api.get('/admin/users'), () => readState().users.map(({ password, ...user }) => user)),
  create(payload) {
    return apiFirst(() => api.post('/admin/users', payload), () => {
      const state = readState();
      const user = { id: nextId(state.users), password: payload.password || '12345678', ...payload };
      state.users.push(user);
      writeState(state);
      const { password, ...safeUser } = user;
      return safeUser;
    });
  },
  updateRole(id, payload) {
    return apiFirst(() => api.patch(`/admin/users/${id}/role`, payload), () => {
      const state = readState();
      state.users = state.users.map((user) => Number(user.id) === Number(id) ? { ...user, ...payload } : user);
      writeState(state);
      const { password, ...safeUser } = state.users.find((user) => Number(user.id) === Number(id));
      return safeUser;
    });
  },
  remove(id) {
    return apiFirst(() => api.delete(`/admin/users/${id}`), () => {
      const state = readState();
      state.users = state.users.filter((user) => Number(user.id) !== Number(id));
      writeState(state);
      return null;
    });
  },
};

export const statsApi = {
  admin: () => apiFirst(() => api.get('/admin/stats'), () => {
    const state = readState();
    return {
      products: state.products.filter((product) => product.status !== 'ARCHIVED').length,
      users: state.users.length,
      orders: state.orders.length,
      revenue: state.orders.reduce((sum, order) => sum + Number(order.totalAmount || 0), 0),
    };
  }),
};
