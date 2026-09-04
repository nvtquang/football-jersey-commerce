# TQSport

TQSport là nền tảng e-commerce bán áo bóng đá, bộ tập và football apparel cho câu lạc bộ và đội tuyển quốc gia. Dự án gồm frontend React/Vite và backend Spring Boot REST API theo kiến trúc microservice, dùng MySQL `utf8mb4` để không lỗi tiếng Việt.

## Công nghệ

- Frontend: React 18, Vite, React Router, i18n, responsive UI
- Backend: Spring Boot 3.3, Spring Web MVC, Spring Data JPA, Spring Validation, Spring Security local API
- Database: MySQL, charset/collation `utf8mb4`
- API docs: Springdoc OpenAPI/Swagger UI theo từng service
- Auth: JWT HMAC-SHA256, role `USER` và `ADMIN`
- Kiến trúc: Maven multi-module microservice, chưa dùng Docker

## Kiến trúc backend

Backend đã được chuyển từ Quarkus sang Spring Boot và tách thành các service độc lập. Frontend chỉ gọi API Gateway ở port `8080`; gateway sẽ chuyển request đến đúng service phía sau.

```text
backend/
+-- pom.xml                 Parent Maven project
+-- shared-kernel/          Entity, DTO, repository, lỗi chung
+-- api-gateway/            Cổng API duy nhất cho frontend
+-- auth-service/           Đăng nhập, đăng ký, seed tài khoản mẫu
+-- catalog-service/        Sản phẩm, đội bóng, danh mục, biến thể
+-- content-service/        Banner và upload nội dung
+-- order-service/          Checkout, lịch sử đơn, xử lý đơn admin
+-- admin-service/          Thống kê dashboard, quản lý user
+-- cart-service/           Cart API
```

## Port mặc định

| Service | Port | Vai trò |
| --- | ---: | --- |
| API Gateway | 8080 | Frontend gọi `http://localhost:8080/api` |
| Auth Service | 8081 | `/api/auth/**` |
| Catalog Service | 8082 | `/api/products/**`, `/api/catalog/**`, `/api/admin/catalog/**` |
| Content Service | 8083 | `/api/banners/**`, `/api/admin/uploads/**` |
| Order Service | 8084 | `/api/orders/**` |
| Admin Service | 8085 | `/api/admin/stats`, `/api/admin/users/**` |
| Cart Service | 8086 | `/api/cart/**` |

## Yêu cầu

- Java 21
- Maven 3.9+
- Node.js 18+
- MySQL đang chạy local

Tạo database nếu chưa có:

```sql
CREATE DATABASE tqsport CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Mỗi service đang dùng cùng cấu hình MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tqsport?useUnicode=true&characterEncoding=utf8&connectionCollation=utf8mb4_unicode_ci&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
```

Nếu MySQL của bạn dùng mật khẩu khác, sửa file `application.properties` trong từng service:

```text
backend/auth-service/src/main/resources/application.properties
backend/catalog-service/src/main/resources/application.properties
backend/content-service/src/main/resources/application.properties
backend/order-service/src/main/resources/application.properties
backend/admin-service/src/main/resources/application.properties
backend/cart-service/src/main/resources/application.properties
```

## Build backend

Mở PowerShell tại backend:

```powershell
cd D:\Java\TQSport\backend
mvn clean package -DskipTests
```

## Chạy backend microservice

Cách nhanh nhất là chạy script:

```powershell
cd D:\Java\TQSport\backend
.\scripts\start-all.ps1
```

Script sẽ khởi động các service trên port `8081` đến `8086`, sau đó khởi động API Gateway ở `8080`. Log nằm trong `backend/logs/`.

Muốn dừng toàn bộ backend:

```powershell
cd D:\Java\TQSport\backend
.\scripts\stop-all.ps1
```

Cũng có thể chạy thủ công, mỗi lệnh ở một terminal riêng:

```powershell
cd D:\Java\TQSport\backend
java -jar auth-service\target\auth-service-1.0.0.jar
java -jar catalog-service\target\catalog-service-1.0.0.jar
java -jar content-service\target\content-service-1.0.0.jar
java -jar order-service\target\order-service-1.0.0.jar
java -jar admin-service\target\admin-service-1.0.0.jar
java -jar cart-service\target\cart-service-1.0.0.jar
java -jar api-gateway\target\api-gateway-1.0.0.jar
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

Frontend phải gọi API Gateway:

```properties
VITE_API_BASE_URL=http://localhost:8080/api
VITE_DEMO_MODE=false
```

`VITE_DEMO_MODE=false` là bắt buộc nếu muốn thao tác frontend lưu vào MySQL và không mất dữ liệu sau khi tải lại trang.

Khi đăng nhập thành công, frontend lưu JWT vào `localStorage` và tự gửi header:

```text
Authorization: Bearer <token>
```

## Tài khoản mẫu

Auth Service tự tạo/cập nhật các tài khoản này khi khởi động:

```text
ADMIN: admin@tqsport.vn / 12345678
ADMIN: superadmin@tqsport.vn / 12345678
USER:  user@tqsport.vn / 12345678
```

Chỉ tài khoản `ADMIN` mới thấy và truy cập được dashboard. Tài khoản `USER` không hiển thị dashboard và không có quyền vào dashboard. API Gateway cũng kiểm tra JWT và chặn các endpoint quản trị như `/api/admin/**`, thêm/sửa/xóa sản phẩm, cập nhật trạng thái đơn hàng.

## Kiểm tra nhanh

Sau khi backend đã chạy:

```powershell
cd D:\Java\TQSport\backend
.\scripts\check-api.ps1
```

Hoặc kiểm tra từng endpoint:

```powershell
Invoke-RestMethod http://localhost:8080/api/catalog/teams
Invoke-RestMethod "http://localhost:8080/api/products?size=3"
Invoke-RestMethod http://localhost:8080/api/admin/stats
```

Test đăng nhập admin:

```powershell
$body = @{ email='admin@tqsport.vn'; password='12345678' } | ConvertTo-Json
Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -ContentType 'application/json; charset=utf-8' -Body $body
```

Test endpoint admin bằng JWT:

```powershell
$login = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -ContentType 'application/json; charset=utf-8' -Body $body
Invoke-RestMethod -Uri 'http://localhost:8080/api/admin/stats' -Headers @{ Authorization = "Bearer $($login.token)" }
```

## Swagger UI

```text
Auth:    http://localhost:8081/swagger-ui.html
Catalog: http://localhost:8082/swagger-ui.html
Content: http://localhost:8083/swagger-ui.html
Order:   http://localhost:8084/swagger-ui.html
Admin:   http://localhost:8085/swagger-ui.html
Cart:    http://localhost:8086/swagger-ui.html
```

## API chính qua Gateway

```text
POST   /api/auth/login
POST   /api/auth/register
GET    /api/products
GET    /api/products/{slug}
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
GET    /api/catalog/teams
GET    /api/catalog/categories
GET    /api/admin/catalog/teams
POST   /api/admin/catalog/teams
PUT    /api/admin/catalog/teams/{id}
DELETE /api/admin/catalog/teams/{id}
GET    /api/admin/catalog/categories
POST   /api/admin/catalog/categories
PUT    /api/admin/catalog/categories/{id}
DELETE /api/admin/catalog/categories/{id}
GET    /api/banners
POST   /api/banners
PUT    /api/banners/{id}
DELETE /api/banners/{id}
GET    /api/orders
POST   /api/orders
GET    /api/orders/admin
GET    /api/orders/admin/{id}
PATCH  /api/orders/{id}/status
GET    /api/admin/stats
GET    /api/admin/users
POST   /api/admin/users
PATCH  /api/admin/users/{id}/role
DELETE /api/admin/users/{id}
```

## Lỗi thường gặp

Nếu frontend không có dữ liệu, kiểm tra đủ 7 port backend chưa:

```powershell
netstat -ano | Select-String ':808[0-6]'
```

Nếu port bị chiếm:

```powershell
netstat -ano | Select-String ':8080'
Stop-Process -Id <PID> -Force
```

Nếu frontend vẫn giữ session cũ:

```js
localStorage.clear()
```

Sau đó tải lại trang và đăng nhập lại.
