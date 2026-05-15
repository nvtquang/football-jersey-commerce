import { useEffect, useState } from 'react';
import { Trash2 } from 'lucide-react';
import { catalogApi, userApi } from '../../lib/tqsportApi.js';
import { useToast } from '../../components/ui/ToastProvider.jsx';

export default function AdminManagement() {
  const [teams, setTeams] = useState([]);
  const [categories, setCategories] = useState([]);
  const [users, setUsers] = useState([]);
  const [teamName, setTeamName] = useState('');
  const [categoryName, setCategoryName] = useState('');
  const [editingTeamId, setEditingTeamId] = useState(null);
  const [editingCategoryId, setEditingCategoryId] = useState(null);
  const [userForm, setUserForm] = useState({ fullName: '', email: '', password: '12345678', role: 'USER', active: true });
  const toast = useToast();

  async function loadData() {
    const [nextTeams, nextCategories, nextUsers] = await Promise.all([
      catalogApi.teams(),
      catalogApi.categories(),
      userApi.list(),
    ]);
    setTeams(nextTeams);
    setCategories(nextCategories);
    setUsers(nextUsers);
  }

  useEffect(() => {
    loadData().catch((error) => toast.error('Không tải được dữ liệu quản trị', error.message));
  }, []);

  function slugify(value) {
    return value.normalize('NFD').replace(/\p{Diacritic}/gu, '').toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '');
  }

  async function createTeam(event) {
    event.preventDefault();
    try {
      const payload = { name: teamName, slug: slugify(teamName), type: 'CLUB', country: 'Global', logoUrl: '' };
      if (editingTeamId) {
        await catalogApi.updateTeam(editingTeamId, payload);
        toast.success('Đã cập nhật team', teamName);
      } else {
        await catalogApi.createTeam(payload);
        toast.success('Đã tạo team', teamName);
      }
      setTeamName('');
      setEditingTeamId(null);
      await loadData();
    } catch (error) {
      toast.error('Tạo team thất bại', error.message);
    }
  }

  async function createCategory(event) {
    event.preventDefault();
    try {
      const payload = { name: categoryName, slug: slugify(categoryName), parentId: null };
      if (editingCategoryId) {
        await catalogApi.updateCategory(editingCategoryId, payload);
        toast.success('Đã cập nhật category', categoryName);
      } else {
        await catalogApi.createCategory(payload);
        toast.success('Đã tạo category', categoryName);
      }
      setCategoryName('');
      setEditingCategoryId(null);
      await loadData();
    } catch (error) {
      toast.error('Tạo category thất bại', error.message);
    }
  }

  async function toggleUser(user) {
    try {
      await userApi.updateRole(user.id, { role: user.role === 'ADMIN' ? 'USER' : 'ADMIN', active: user.active });
      toast.success('Đã cập nhật quyền', user.email);
      await loadData();
    } catch (error) {
      toast.error('Cập nhật quyền thất bại', error.message);
    }
  }

  async function createUser(event) {
    event.preventDefault();
    try {
      await userApi.create(userForm);
      toast.success('Đã tạo user', userForm.email);
      setUserForm({ fullName: '', email: '', password: '12345678', role: 'USER', active: true });
      await loadData();
    } catch (error) {
      toast.error('Tạo user thất bại', error.message);
    }
  }

  async function deleteUser(user) {
    try {
      await userApi.remove(user.id);
      toast.success('Đã xóa user', user.email);
      await loadData();
    } catch (error) {
      toast.error('Xóa user thất bại', error.message);
    }
  }

  async function deleteTeam(team) {
    await catalogApi.deleteTeam(team.id);
    toast.success('Đã xóa team', team.name);
    await loadData();
  }

  async function deleteCategory(category) {
    await catalogApi.deleteCategory(category.id);
    toast.success('Đã xóa category', category.name);
    await loadData();
  }

  return (
    <section className="admin-panel">
      <p className="kicker">Master data CRUD and RBAC</p>
      <h1>Teams, categories and users</h1>
      <div className="management-grid">
        <article>
          <h2>Teams</h2>
          <form className="mini-form" onSubmit={createTeam}><input value={teamName} onChange={(event) => setTeamName(event.target.value)} placeholder="New team" required /><button className="primary small">{editingTeamId ? 'Update' : 'Add'}</button></form>
          {teams.map((team) => <div className="mini-row" key={team.id}><span>{team.name}</span><div className="row-actions"><button className="ghost" onClick={() => { setEditingTeamId(team.id); setTeamName(team.name); }}>Edit</button><button onClick={() => deleteTeam(team)}><Trash2 size={16} /></button></div></div>)}
        </article>
        <article>
          <h2>Categories</h2>
          <form className="mini-form" onSubmit={createCategory}><input value={categoryName} onChange={(event) => setCategoryName(event.target.value)} placeholder="New category" required /><button className="primary small">{editingCategoryId ? 'Update' : 'Add'}</button></form>
          {categories.map((category) => <div className="mini-row" key={category.id}><span>{category.name}</span><div className="row-actions"><button className="ghost" onClick={() => { setEditingCategoryId(category.id); setCategoryName(category.name); }}>Edit</button><button onClick={() => deleteCategory(category)}><Trash2 size={16} /></button></div></div>)}
        </article>
        <article>
          <h2>Users</h2>
          <form className="stack-form" onSubmit={createUser}>
            <input value={userForm.fullName} onChange={(event) => setUserForm((current) => ({ ...current, fullName: event.target.value }))} placeholder="Full name" required />
            <input value={userForm.email} onChange={(event) => setUserForm((current) => ({ ...current, email: event.target.value }))} placeholder="Email" type="email" required />
            <select value={userForm.role} onChange={(event) => setUserForm((current) => ({ ...current, role: event.target.value }))}><option>USER</option><option>ADMIN</option></select>
            <button className="primary small">Create user</button>
          </form>
          {users.map((user) => <div className="mini-row" key={user.id}><span>{user.email}</span><div className="row-actions"><button className="ghost" onClick={() => toggleUser(user)}>{user.role}</button><button onClick={() => deleteUser(user)}><Trash2 size={16} /></button></div></div>)}
        </article>
        <article>
          <h2>Inventory alerts</h2>
          <p className="muted">Dữ liệu tồn kho được quản lý trong Product management qua API `/api/products`.</p>
        </article>
      </div>
    </section>
  );
}
