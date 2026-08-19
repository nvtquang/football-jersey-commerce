# TQSport

TQSport là nền tảng e-commerce full-stack bán áo bóng đá, bộ tập và apparel cho câu lạc bộ/đội tuyển quốc gia.

## Công nghệ

- Frontend: React 18, Vite, React Router, i18n, responsive UI
- Backend: Quarkus 3, RESTEasy Reactive, Hibernate ORM Panache
- Database: MySQL, charset `utf8mb4`
- Chức năng: storefront, đăng nhập/đăng ký, giỏ hàng, checkout, lịch sử đơn hàng, dashboard admin, CRUD sản phẩm/danh mục/team/banner/order/user

## Cấu trúc thư mục

```text
frontend/   Giao diện khách hàng và dashboard admin
backend/    REST API Quarkus, entity, service, resource, seed dữ liệu
```

## Yêu cầu trước khi chạy

- Java 21
- Maven
- Node.js 18+
- MySQL đang chạy local

Tạo database:

```sql
CREATE DATABASE tqsport CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Thông tin kết nối mặc định trong `backend/src/main/resources/application.properties`:

```properties
quarkus.datasource.username=root
quarkus.datasource.password=123456
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/tqsport?useUnicode=true&characterEncoding=utf8&connectionCollation=utf8mb4_unicode_ci&serverTimezone=UTC
```

Nếu MySQL của bạn dùng mật khẩu khác, sửa lại `quarkus.datasource.password`.

## Chạy backend

Mở terminal tại:

```powershell
cd D:\Java\TQSport\backend
mvn quarkus:dev
```

Không cần dán key, không cần tạo `privateKey.pem` hoặc `publicKey.pem` khi chạy local.

API chạy tại:

```text
http://localhost:8080/api
```

Swagger UI:

```text
http://localhost:8080/q/swagger-ui
```

## Chạy frontend

Mở terminal khác:

```powershell
cd D:\Java\TQSport\frontend
npm install
npm run dev
```

Frontend mặc định chạy tại:

```text
http://localhost:5173
```

File cấu hình frontend mẫu:

```properties
VITE_API_BASE_URL=http://localhost:8080/api
VITE_DEMO_MODE=false
```

`VITE_DEMO_MODE=false` nghĩa là dữ liệu được lưu qua backend vào MySQL. Không bật demo mode nếu muốn dữ liệu không bị mất khi tải lại trang.

## Tài khoản mẫu

Các tài khoản này được backend tự tạo/cập nhật khi chạy:

```text
ADMIN: admin@tqsport.vn / 12345678
ADMIN: superadmin@tqsport.vn / 12345678
USER:  user@tqsport.vn / 12345678
```

Chỉ tài khoản `ADMIN` mới được thấy và vào dashboard trên frontend. Tài khoản `USER` không hiển thị dashboard.

## Kiểm tra nhanh khi lỗi không tải dữ liệu

1. Kiểm tra backend có chạy chưa:

```powershell
Invoke-RestMethod http://localhost:8080/api/catalog/teams
```

2. Nếu báo không kết nối được, chạy lại backend:

```powershell
cd D:\Java\TQSport\backend
mvn quarkus:dev
```

3. Nếu đăng nhập vẫn dùng session cũ, mở DevTools Console ở trình duyệt và chạy:

```js
localStorage.clear()
```

Sau đó tải lại trang và đăng nhập lại.

## Build kiểm tra

Backend:

```powershell
cd D:\Java\TQSport\backend
mvn -q -DskipTests compile
```

Frontend:

```powershell
cd D:\Java\TQSport\frontend
npm run build
```
