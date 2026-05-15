import { createContext, useCallback, useContext, useMemo, useState } from 'react';
import { CheckCircle2, Info, X, XCircle } from 'lucide-react';

const ToastContext = createContext(null);

const icons = {
  success: CheckCircle2,
  error: XCircle,
  info: Info,
};

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([]);

  const removeToast = useCallback((id) => {
    setToasts((current) => current.filter((toast) => toast.id !== id));
  }, []);

  const showToast = useCallback(({ title, message, type = 'success' }) => {
    const id = crypto.randomUUID?.() ?? `${Date.now()}-${Math.random()}`;
    setToasts((current) => [...current, { id, title, message, type }]);
    window.setTimeout(() => removeToast(id), 3200);
  }, [removeToast]);

  const value = useMemo(() => ({
    success(title, message) {
      showToast({ title, message, type: 'success' });
    },
    error(title, message) {
      showToast({ title, message, type: 'error' });
    },
    info(title, message) {
      showToast({ title, message, type: 'info' });
    },
  }), [showToast]);

  return (
    <ToastContext.Provider value={value}>
      {children}
      <div className="toast-stack" aria-live="polite" aria-atomic="true">
        {toasts.map((toast) => {
          const Icon = icons[toast.type] ?? Info;
          return (
            <article className={`toast toast-${toast.type}`} key={toast.id}>
              <Icon size={20} />
              <div>
                <strong>{toast.title}</strong>
                {toast.message && <p>{toast.message}</p>}
              </div>
              <button onClick={() => removeToast(toast.id)} aria-label="Đóng thông báo">
                <X size={16} />
              </button>
            </article>
          );
        })}
      </div>
    </ToastContext.Provider>
  );
}

export function useToast() {
  return useContext(ToastContext);
}
