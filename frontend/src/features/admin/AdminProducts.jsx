import { useEffect, useState } from 'react';
import { Edit3, Plus, Trash2 } from 'lucide-react';
import { formatCurrency } from '../../lib/mockData.js';
import { catalogApi, productApi } from '../../lib/tqsportApi.js';
import { useToast } from '../../components/ui/ToastProvider.jsx';

const emptyForm = { name: '', price: 0, teamId: '', categoryId: '', description: '', imageUrl: '', stockQuantity: 0, status: 'ACTIVE', featured: false };

export default function AdminProducts() {
  const [products, setProducts] = useState([]);
  const [teams, setTeams] = useState([]);
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const toast = useToast();

  async function loadData() {
    const [nextProducts, nextTeams, nextCategories] = await Promise.all([
      productApi.list({ size: 100 }),
      catalogApi.teams(),
      catalogApi.categories(),
    ]);
    setProducts(nextProducts);
    setTeams(nextTeams);
    setCategories(nextCategories);
  }

  useEffect(() => {
    loadData().catch((error) => toast.error('Không tải được sản phẩm', error.message));
  }, []);

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  function editProduct(product) {
    setEditingId(product.id);
    setForm({
      name: product.name,
      price: product.price,
      teamId: product.teamId || '',
      categoryId: product.categoryId || '',
      description: product.description || '',
      imageUrl: product.imageUrl || '',
      stockQuantity: product.stockQuantity || 0,
      status: product.status || 'ACTIVE',
      featured: Boolean(product.featured),
    });
  }

  async function saveProduct(event) {
    event.preventDefault();
    const payload = { ...form, price: Number(form.price), teamId: Number(form.teamId), categoryId: Number(form.categoryId), stockQuantity: Number(form.stockQuantity) };
    try {
      if (editingId) {
        await productApi.update(editingId, payload);
        toast.success('Đã cập nhật sản phẩm', form.name);
      } else {
        await productApi.create(payload);
        toast.success('Đã tạo sản phẩm', form.name);
      }
      setForm(emptyForm);
      setEditingId(null);
      await loadData();
    } catch (error) {
      toast.error('Lưu sản phẩm thất bại', error.message);
    }
  }

  async function deleteProduct(product) {
    try {
      await productApi.remove(product.id);
      toast.success('Đã lưu trữ sản phẩm', product.name);
      await loadData();
    } catch (error) {
      toast.error('Xóa sản phẩm thất bại', error.message);
    }
  }

  return (
    <section className="admin-panel">
      <div className="section-heading"><div><p className="kicker">Inventory CRUD</p><h1>Product management</h1></div><button className="primary small" onClick={() => { setEditingId(null); setForm(emptyForm); }}><Plus size={16} /> New</button></div>
      <form className="crud-form" onSubmit={saveProduct}>
        <label>Name<input value={form.name} onChange={(event) => updateField('name', event.target.value)} required /></label>
        <label>Price<input value={form.price} onChange={(event) => updateField('price', event.target.value)} type="number" min="0" required /></label>
        <label>Team<select value={form.teamId} onChange={(event) => updateField('teamId', event.target.value)} required><option value="">Select</option>{teams.map((team) => <option value={team.id} key={team.id}>{team.name}</option>)}</select></label>
        <label>Category<select value={form.categoryId} onChange={(event) => updateField('categoryId', event.target.value)} required><option value="">Select</option>{categories.map((category) => <option value={category.id} key={category.id}>{category.name}</option>)}</select></label>
        <label>Stock<input value={form.stockQuantity} onChange={(event) => updateField('stockQuantity', event.target.value)} type="number" min="0" /></label>
        <label>Image URL<input value={form.imageUrl} onChange={(event) => updateField('imageUrl', event.target.value)} /></label>
        <button className="primary" type="submit">{editingId ? 'Update product' : 'Create product'}</button>
      </form>
      <div className="table-wrap">
        <table>
          <thead><tr><th>Name</th><th>Team</th><th>Price</th><th>Stock</th><th>Status</th><th></th></tr></thead>
          <tbody>{products.map((product) => (
            <tr key={product.id}>
              <td>{product.name}</td><td>{product.team}</td><td>{formatCurrency(product.price)}</td><td>{product.stockQuantity}</td>
              <td><span className={product.stockQuantity < 20 ? 'status warning' : 'status success'}>{product.status}</span></td>
              <td className="row-actions"><button onClick={() => editProduct(product)}><Edit3 size={16} /></button><button onClick={() => deleteProduct(product)}><Trash2 size={16} /></button></td>
            </tr>
          ))}</tbody>
        </table>
      </div>
    </section>
  );
}
