import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight, Flame, Medal } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import ProductCard from '../../components/ui/ProductCard.jsx';
import { products as fallbackProducts, teams as fallbackTeams } from '../../lib/mockData.js';
import { bannerApi, catalogApi, productApi } from '../../lib/tqsportApi.js';

export default function HomePage() {
  const { t } = useTranslation();
  const [products, setProducts] = useState([]);
  const [teams, setTeams] = useState([]);
  const [banner, setBanner] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadHome() {
      const [productResult, teamResult, bannerResult] = await Promise.allSettled([
        productApi.list({ size: 8 }),
        catalogApi.teams(),
        bannerApi.list(),
      ]);
      setProducts(productResult.status === 'fulfilled' ? productResult.value : fallbackProducts);
      setTeams(teamResult.status === 'fulfilled' ? teamResult.value : fallbackTeams.map((name) => ({ name })));
      setBanner(bannerResult.status === 'fulfilled' ? bannerResult.value.find((item) => item.active) || null : null);
      setLoading(false);
    }

    loadHome();
  }, []);

  if (loading) {
    return <main className="page"><div className="loading-panel">Đang tải dữ liệu...</div></main>;
  }

  return (
    <main>
      <section className="hero" style={banner?.imageUrl ? { backgroundImage: `linear-gradient(90deg, rgba(8,17,31,.94), rgba(8,17,31,.34)), url("${banner.imageUrl}")` } : undefined}>
        <div className="hero-content">
          <p className="kicker">Football apparel · Club & national team</p>
          <h1>{banner?.title || t('heroTitle')}</h1>
          <p>{banner?.subtitle || t('heroCopy')}</p>
          <Link to={banner?.linkUrl || '/products'} className="primary">{t('shopNow')} <ArrowRight size={18} /></Link>
        </div>
      </section>
      <section className="promo-band">
        <span><Flame size={18} /> Flash sale áo đấu cuối mùa</span>
        <span><Medal size={18} /> Miễn phí in tên cho đơn từ 1.500.000đ</span>
      </section>
      <section className="page">
        <div className="section-heading">
          <div>
            <p className="kicker">{t('featured')}</p>
            <h2>{t('trending')}</h2>
          </div>
          <Link to="/products">Xem tất cả</Link>
        </div>
        <div className="product-grid">{products.map((product) => <ProductCard key={product.id} product={product} />)}</div>
      </section>
      <section className="page">
        <h2>{t('browseClub')}</h2>
        <div className="team-grid">{teams.map((team) => <Link to={`/products?teamId=${team.id || ''}`} key={team.id || team.name}>{team.name}</Link>)}</div>
      </section>
    </main>
  );
}
