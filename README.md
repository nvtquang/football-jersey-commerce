# TQSport

Full-stack football apparel e-commerce platform for club and national team jerseys, training kits, and sportswear.

## Stack

- Frontend: React 18, Vite, React Router, i18next-ready translation architecture
- Backend: Quarkus 3, RESTEasy Reactive, Hibernate ORM Panache, MySQL `utf8mb4`, JWT RBAC
- Domains: storefront, authentication, cart/checkout, order history, admin management

## Structure

```text
frontend/   Customer store and protected admin dashboard
backend/    Quarkus REST API, entities, services, resources, Flyway schema
```

## Run

Frontend:

```bash
cd frontend
npm install
npm run dev
```

Backend:

```bash
cd backend
# Generate JWT keys once for local development, or provide your own deployment keys.
openssl genrsa -out src/main/resources/privateKey.pem 2048
openssl rsa -pubout -in src/main/resources/privateKey.pem -out src/main/resources/publicKey.pem
mvn quarkus:dev
```

Create a MySQL database named `tqsport` using `utf8mb4` before starting the backend.

```sql
CREATE DATABASE tqsport CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Default API docs are exposed at `/q/swagger-ui`.

## Account
Admin mẫu: admin@tqsport.vn / 12345678. User mẫu: user@tqsport.vn / 12345678
# football-jersey-commerce
