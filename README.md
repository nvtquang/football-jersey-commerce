# TQSport

TQSport là nền tảng e-commerce full-stack bán áo bóng đá, bộ tập và apparel cho câu lạc bộ/đội tuyển quốc gia.

## Công nghệ

- Frontend: React 18, Vite, React Router, i18n, responsive UI
- Backend: Quarkus 3, RESTEasy Reactive, Hibernate ORM Panache
- Database: MySQL, charset `utf8mb4`
- Chức năng: storefront, đăng nhập/đăng ký, giỏ hàng, checkout, lịch sử đơn hàng, dashboard admin, CRUD sản phẩm/danh mục/team/banner/order/user


## Chạy backend

Mở terminal tại:

```powershell
cd D:\Java\TQSport\backend
mvn quarkus:dev
```

## Chạy frontend

Mở terminal khác:

```powershell
cd D:\Java\TQSport\frontend
npm install
npm run dev
```

## Tài khoản mẫu

Các tài khoản này được backend tự tạo/cập nhật khi chạy:

```text
ADMIN: admin@tqsport.vn / 12345678
USER:  user@tqsport.vn / 12345678
```

Chỉ tài khoản `ADMIN` mới được thấy và vào dashboard trên frontend. Tài khoản `USER` không hiển thị dashboard.
