import { api, buildQuery } from './api.js';

export const authApi = {
  login(payload) {
    return api.post('/auth/login', payload);
  },
  register(payload) {
    return api.post('/auth/register', payload);
  },
};

export const productApi = {
  list(params = {}) {
    return api.get(`/products${buildQuery(params)}`);
  },
  detail(slug) {
    return api.get(`/products/${slug}`);
  },
  create(payload) {
    return api.post('/products', payload);
  },
  update(id, payload) {
    return api.put(`/products/${id}`, payload);
  },
  remove(id) {
    return api.delete(`/products/${id}`);
  },
};

export const catalogApi = {
  teams() {
    return api.get('/catalog/teams');
  },
  categories() {
    return api.get('/catalog/categories');
  },
  createTeam(payload) {
    return api.post('/admin/catalog/teams', payload);
  },
  updateTeam(id, payload) {
    return api.put(`/admin/catalog/teams/${id}`, payload);
  },
  deleteTeam(id) {
    return api.delete(`/admin/catalog/teams/${id}`);
  },
  createCategory(payload) {
    return api.post('/admin/catalog/categories', payload);
  },
  updateCategory(id, payload) {
    return api.put(`/admin/catalog/categories/${id}`, payload);
  },
  deleteCategory(id) {
    return api.delete(`/admin/catalog/categories/${id}`);
  },
};

export const bannerApi = {
  list() {
    return api.get('/banners');
  },
  create(payload) {
    return api.post('/banners', payload);
  },
  update(id, payload) {
    return api.put(`/banners/${id}`, payload);
  },
  remove(id) {
    return api.delete(`/banners/${id}`);
  },
};

export const orderApi = {
  history() {
    return api.get('/orders');
  },
  checkout(payload) {
    return api.post('/orders', payload);
  },
  adminList() {
    return api.get('/orders/admin');
  },
  updateStatus(id, status) {
    return api.patch(`/orders/${id}/status`, { status });
  },
};

export const userApi = {
  list() {
    return api.get('/admin/users');
  },
  create(payload) {
    return api.post('/admin/users', payload);
  },
  updateRole(id, payload) {
    return api.patch(`/admin/users/${id}/role`, payload);
  },
  remove(id) {
    return api.delete(`/admin/users/${id}`);
  },
};

export const statsApi = {
  admin() {
    return api.get('/admin/stats');
  },
};
