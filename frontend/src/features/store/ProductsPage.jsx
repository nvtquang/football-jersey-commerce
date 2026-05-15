import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { SlidersHorizontal } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import ProductCard from '../../components/ui/ProductCard.jsx';
import { categories as fallbackCategories, products as fallbackProducts, teams as fallbackTeams } from '../../lib/mockData.js';
import { catalogApi, productApi } from '../../lib/tqsportApi.js';

export default function ProductsPage() {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();
  const [query, setQuery] = useState('');
  const [teamId, setTeamId] = useState(searchParams.get('teamId') || '');
  const [categoryId, setCategoryId] = useState('');
  const [products, setProducts] = useState(fallbackProducts);
  const [teams, setTeams] = useState(fallbackTeams.map((name) => ({ name })));
  const [categories, setCategories] = useState(fallbackCategories.map((name) => ({ name })));

  useEffect(() => {
    catalogApi.teams().then(setTeams).catch(() => setTeams(fallbackTeams.map((name) => ({ name }))));
    catalogApi.categories().then(setCategories).catch(() => setCategories(fallbackCategories.map((name) => ({ name }))));
  }, []);

  useEffect(() => {
    productApi.list({ q: query, teamId, categoryId, size: 24 })
      .then(setProducts)
      .catch(() => setProducts(fallbackProducts));
  }, [query, teamId, categoryId]);

  return (
    <main className="page catalog-layout">
      <aside className="filters">
        <h2><SlidersHorizontal size={18} /> {t('filters')}</h2>
        <label>{t('search')}<input value={query} onChange={(e) => setQuery(e.target.value)} /></label>
        <label>Team<select value={teamId} onChange={(e) => setTeamId(e.target.value)}><option value="">All</option>{teams.map((item) => <option value={item.id || ''} key={item.id || item.name}>{item.name}</option>)}</select></label>
        <label>Category<select value={categoryId} onChange={(e) => setCategoryId(e.target.value)}><option value="">All</option>{categories.map((item) => <option value={item.id || ''} key={item.id || item.name}>{item.name}</option>)}</select></label>
      </aside>
      <section>
        <div className="section-heading">
          <div><p className="kicker">Catalog</p><h1>Football store</h1></div>
          <span>{products.length} sản phẩm</span>
        </div>
        <div className="product-grid">{products.map((product) => <ProductCard key={product.id} product={product} />)}</div>
      </section>
    </main>
  );
}
