import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ShieldCheck, UserPlus } from 'lucide-react';
import { useAuth } from './AuthContext.jsx';
import { useToast } from '../../components/ui/ToastProvider.jsx';

export default function LoginPage() {
  const [email, setEmail] = useState('admin@tqsport.vn');
  const [password, setPassword] = useState('12345678');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const toast = useToast();
  const navigate = useNavigate();

  async function submit(event) {
    event.preventDefault();
    setError('');
    try {
      const nextUser = await login(email, password);
      toast.success('Đăng nhập thành công', `Xin chào ${nextUser.name}`);
      navigate(nextUser.role === 'ADMIN' ? '/admin' : '/');
    } catch (err) {
      setError(err.message);
      toast.error('Đăng nhập thất bại', err.message);
    }
  }

  return (
    <main className="auth-page">
      <form className="auth-panel" onSubmit={submit}>
        <ShieldCheck size={30} />
        <h1>Đăng nhập TQSport</h1>
        <p className="muted">Admin mẫu: admin@tqsport.vn / 12345678. User mẫu: user@tqsport.vn / 12345678.</p>
        {error && <div className="form-alert">{error}</div>}
        <label>Email<input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required /></label>
        <label>Mật khẩu<input value={password} onChange={(e) => setPassword(e.target.value)} type="password" minLength={8} required /></label>
        <button className="primary" type="submit">Đăng nhập</button>
        <Link className="auth-switch" to="/register"><UserPlus size={18} /> Tạo tài khoản </Link>
      </form>
    </main>
  );
}
