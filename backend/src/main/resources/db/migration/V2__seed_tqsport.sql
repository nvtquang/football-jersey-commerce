INSERT INTO users (full_name, email, password_hash, role, phone)
VALUES
  ('TQSport Admin', 'admin@tqsport.vn', '{bcrypt-placeholder}12345678', 'ADMIN', '0900000000'),
  ('Khách hàng TQSport', 'member@tqsport.vn', '{bcrypt-placeholder}12345678', 'USER', '0911111111'),
  ('TQSport User', 'user@tqsport.vn', '{bcrypt-placeholder}12345678', 'USER', '0922222222');

INSERT INTO teams (name, slug, type, country, logo_url)
VALUES
  ('Vietnam', 'vietnam', 'NATIONAL', 'Việt Nam', '/uploads/teams/vietnam.png'),
  ('Real Madrid', 'real-madrid', 'CLUB', 'Spain', '/uploads/teams/real-madrid.png'),
  ('Barcelona', 'barcelona', 'CLUB', 'Spain', '/uploads/teams/barcelona.png'),
  ('Argentina', 'argentina', 'NATIONAL', 'Argentina', '/uploads/teams/argentina.png');

INSERT INTO categories (name, slug)
VALUES
  ('Áo sân nhà', 'ao-san-nha'),
  ('Áo sân khách', 'ao-san-khach'),
  ('Bộ tập', 'bo-tap'),
  ('Áo khoác', 'ao-khoac');

INSERT INTO products (name, slug, description, price, team_id, category_id, status, featured)
VALUES
  ('Áo tuyển Việt Nam sân nhà 2026', 'vietnam-home-2026', 'Áo đấu sân nhà đội tuyển Việt Nam, chất liệu thoáng khí.', 890000, 1, 1, 'ACTIVE', true),
  ('Bộ tập Real Madrid Pro Black', 'real-madrid-training-black', 'Bộ tập hiệu suất cao dành cho luyện tập hằng ngày.', 1190000, 2, 3, 'ACTIVE', true),
  ('Áo Argentina sân khách Heritage', 'argentina-away-heritage', 'Thiết kế di sản với form thể thao hiện đại.', 990000, 4, 2, 'ACTIVE', true),
  ('Áo khoác Barcelona Travel', 'barcelona-windbreaker', 'Áo khoác di chuyển nhẹ, chống gió.', 1490000, 3, 4, 'ACTIVE', false);

INSERT INTO product_variants (product_id, sku, size, color, stock_quantity)
VALUES
  (1, 'VN-HOME-26-M', 'M', 'Red', 42),
  (1, 'VN-HOME-26-L', 'L', 'Red', 35),
  (2, 'RM-TRAIN-BLK-M', 'M', 'Black', 18),
  (3, 'ARG-AWAY-L', 'L', 'Blue', 26),
  (4, 'BAR-TRAVEL-XL', 'XL', 'Navy', 12);

INSERT INTO product_images (product_id, image_url, alt_text, sort_order)
VALUES
  (1, 'https://images.unsplash.com/photo-1517466787929-bc90951d0974?auto=format&fit=crop&w=900&q=80', 'Áo tuyển Việt Nam sân nhà', 1),
  (2, 'https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=900&q=80', 'Bộ tập Real Madrid', 1),
  (3, 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?auto=format&fit=crop&w=900&q=80', 'Áo Argentina sân khách', 1),
  (4, 'https://images.unsplash.com/photo-1579952363873-27f3bade9f55?auto=format&fit=crop&w=900&q=80', 'Áo khoác Barcelona', 1);

INSERT INTO banners (title, subtitle, image_url, link_url, position, active, sort_order)
VALUES
  ('Drop mới cho mùa giải 2026', 'Áo đấu câu lạc bộ và đội tuyển quốc gia đã có mặt tại TQSport.', 'https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?auto=format&fit=crop&w=1800&q=82', '/products', 'HOME_HERO', true, 1);
