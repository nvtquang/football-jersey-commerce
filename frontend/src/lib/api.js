const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

export async function apiRequest(path, options = {}) {
  const token = localStorage.getItem('tqsport_token');
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: 'Request failed' }));
    throw new Error(error.message || 'Request failed');
  }

  if (response.status === 204) return null;
  return response.json();
}

export function buildQuery(params) {
  const search = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') search.set(key, value);
  });
  const query = search.toString();
  return query ? `?${query}` : '';
}

export const api = {
  get(path) {
    return apiRequest(path);
  },
  post(path, body) {
    return apiRequest(path, { method: 'POST', body: JSON.stringify(body) });
  },
  put(path, body) {
    return apiRequest(path, { method: 'PUT', body: JSON.stringify(body) });
  },
  patch(path, body) {
    return apiRequest(path, { method: 'PATCH', body: JSON.stringify(body) });
  },
  delete(path) {
    return apiRequest(path, { method: 'DELETE' });
  },
};
