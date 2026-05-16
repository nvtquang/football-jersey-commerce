import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ShieldCheck } from 'lucide-react';
import { useAuth } from './AuthContext.jsx';
import { useToast } from '../../components/ui/ToastProvider.jsx';

export default function RegisterPage() {
  const [form, setForm] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
  });
  const [error, setError] = useState('');
  const { register } = useAuth();
  const toast = useToast();
  const navigate = useNavigate();

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function submit(event) {
    event.preventDefault();
    setError('');
    if (form.password !== form.confirmPassword) {
      setError('Mật khẩu xác nhận không khớp.');
      toast.error('Đăng ký thất bại', 'Mật khẩu xác nhận không khớp.');
      return;
    }
    try {
      const nextUser = await register({ name: form.name, email: form.email, password: form.password });
      toast.success('Đăng ký thành công', `Tài khoản ${nextUser.email} đã được tạo với quyền USER.`);
      navigate('/');
    } catch (err) {
      setError(err.message);
      toast.error('Đăng ký thất bại', err.message);
    }
  }

  return (
    <main className="auth-page">
      <form className="auth-panel" onSubmit={submit}>
        <ShieldCheck size={30} />
        <h1>Đăng ký tài khoản</h1>
        <p className="muted">Đăng ký tài khoản để có thể đặt hàng và theo dõi đơn hàng của bạn.</p>
        {error && <div className="form-alert">{error}</div>}
        <label>Họ tên<input value={form.name} onChange={(e) => updateField('name', e.target.value)} required /></label>
        <label>Email<input value={form.email} onChange={(e) => updateField('email', e.target.value)} type="email" required /></label>
        <label>Mật khẩu<input value={form.password} onChange={(e) => updateField('password', e.target.value)} type="password" minLength={8} required /></label>
        <label>Xác nhận mật khẩu<input value={form.confirmPassword} onChange={(e) => updateField('confirmPassword', e.target.value)} type="password" minLength={8} required /></label>
        <div style={{ display: 'flex', justifyContent: 'center' }} ><button className="primary" type="submit">Đăng ký</button></div>
        <Link className="auth-switch" to="/login">Đã có tài khoản? Đăng nhập</Link>
      </form>
    </main>
  );
}
