import { useEffect, useState } from 'react';
import { Edit3, Trash2 } from 'lucide-react';
import { bannerApi } from '../../lib/tqsportApi.js';
import { useToast } from '../../components/ui/ToastProvider.jsx';

const emptyBanner = { title: '', subtitle: '', imageUrl: '', linkUrl: '/products', position: 'HOME_HERO', active: true, sortOrder: 1 };

export default function AdminContent() {
  const [banners, setBanners] = useState([]);
  const [form, setForm] = useState(emptyBanner);
  const [editingId, setEditingId] = useState(null);
  const toast = useToast();

  async function loadBanners() {
    setBanners(await bannerApi.list());
  }

  useEffect(() => {
    loadBanners().catch((error) => toast.error('Không tải được banner', error.message));
  }, []);

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function saveBanner(event) {
    event.preventDefault();
    try {
      if (editingId) {
        await bannerApi.update(editingId, form);
        toast.success('Đã cập nhật banner', form.title);
      } else {
        await bannerApi.create(form);
        toast.success('Đã tạo banner', form.title);
      }
      setForm(emptyBanner);
      setEditingId(null);
      await loadBanners();
    } catch (error) {
      toast.error('Lưu banner thất bại', error.message);
    }
  }

  async function deleteBanner(banner) {
    try {
      await bannerApi.remove(banner.id);
      toast.success('Đã xóa banner', banner.title);
      await loadBanners();
    } catch (error) {
      toast.error('Xóa banner thất bại', error.message);
    }
  }

  return (
    <section className="admin-panel">
      <p className="kicker">Storefront CMS CRUD</p>
      <h1>Banner and content management</h1>
      <form className="crud-form" onSubmit={saveBanner}>
        <label>Banner title<input value={form.title} onChange={(event) => updateField('title', event.target.value)} required /></label>
        <label>Subtitle<input value={form.subtitle} onChange={(event) => updateField('subtitle', event.target.value)} /></label>
        <label>Image URL<input value={form.imageUrl} onChange={(event) => updateField('imageUrl', event.target.value)} required /></label>
        <label>Link URL<input value={form.linkUrl} onChange={(event) => updateField('linkUrl', event.target.value)} /></label>
        <label>Position<select value={form.position} onChange={(event) => updateField('position', event.target.value)}><option>HOME_HERO</option><option>HOME_PROMO</option></select></label>
        <button className="primary" type="submit">{editingId ? 'Update banner' : 'Create banner'}</button>
      </form>
      <div className="table-wrap">
        <table><thead><tr><th>Title</th><th>Position</th><th>Active</th><th></th></tr></thead><tbody>
          {banners.map((banner) => <tr key={banner.id}><td>{banner.title}</td><td>{banner.position}</td><td>{banner.active ? 'Yes' : 'No'}</td><td className="row-actions"><button onClick={() => { setEditingId(banner.id); setForm(banner); }}><Edit3 size={16} /></button><button onClick={() => deleteBanner(banner)}><Trash2 size={16} /></button></td></tr>)}
        </tbody></table>
      </div>
    </section>
  );
}
