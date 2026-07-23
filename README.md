# 📅 Appointment Booking System

A RESTful backend API for managing salon / beauty-service appointment bookings, built with **Spring Boot** following a **CQRS + Hexagonal Architecture** pattern.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Database | MySQL 8.4 |
| Cache / Session | Redis 7 |
| Auth | JWT (access + refresh + reset tokens) |
| OAuth | Google OAuth 2.0 |
| Payment | MoMo e-Wallet |
| File Storage | Local file system |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven |
| Containerization | Docker / Docker Compose |
| CI/CD | GitHub Actions |

---

## 🏗️ Architecture

The project follows **CQRS (Command Query Responsibility Segregation)** inside a **Hexagonal Architecture**:

```
presentation/       ← HTTP controllers (REST endpoints)
application/        ← Command & Query handlers (use cases)
domain/             ← Entities, Repository interfaces, Domain services
infrastructure/     ← JPA implementations, File storage, Security, Mappers
common/             ← Shared constants, exceptions, response wrappers, utils
```

Each role (user, staff, admin) has its own set of commands, queries, and controllers, keeping responsibilities clearly separated.

---

## 📦 Module Overview

### Controllers by role

| Role | Base Path | Responsibilities |
|---|---|---|
| **User** | `/api/appointments` | Hold slot, create appointment, booking history, create review |
| **User** | `/api/invoices` | View invoice, apply promotion |
| **User** | `/api/payments` | Initiate MoMo payment, check status |
| **User** | `/api/users` | View & update profile, upload avatar |
| **User** | `/api/services` | Browse services, available staff |
| **User** | `/api/categories` | List categories |
| **User** | `/api/promotions` | Lookup promotion code |
| **Public** | `/api/public/reviews` | View service reviews, stats, top-rated |
| **Staff** | `/api/staff/appointments` | View & complete appointments |
| **Staff** | `/api/staff/shifts` | Manage own shifts, view schedule |
| **Admin** | `/api/admin/dashboard` | Overview, alerts, charts |
| **Admin** | `/api/admin/services` | Manage services & images |
| **Admin** | `/api/admin/categories` | Manage categories |
| **Admin** | `/api/admin/promotions` | Manage promotions |
| **Admin** | `/api/admin/reviews` | Moderate reviews |
| **Admin** | `/api/admin/users` | Manage users |
| **Admin** | `/api/admin/staff/shifts` | Manage staff shifts |
| **Admin** | `/api/admin/staff/services` | Assign services to staff |
| **Admin** | `/api/admin/blocked-slots` | Manage blocked time slots |
| **Auth** | `/api/auth` | Register, login, OAuth, refresh, OTP |

### Domain Entities

`User` · `Role` · `Appointment` · `AppointmentDetail` · `Invoice` · `Payment` · `ServiceEntity` · `ServiceImage` · `Category` · `StaffService` · `StaffShift` · `BlockedSlot` · `Promotion` · `Reviews` · `UserLogin`

---

## ⚙️ Getting Started

### Prerequisites

- **Java 21+**
- **Docker & Docker Compose** (for local infrastructure)
- **Maven** (wrapper included — `mvnw`)

### 1. Start local infrastructure (MySQL + Redis)

```bash
docker compose up -d mysql redis
```

### 2. Configure environment

Create `app/.env.local` (or set environment variables) based on `.env.prod.example`:

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/appointment_booking
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root

# Redis
SPRING_DATA_REDIS_HOST=localhost
SPRING_DATA_REDIS_PORT=6379

# JWT
SECURITY_JWT_SECRET=your-secret-at-least-32-bytes-long
SECURITY_JWT_RESET_SECRET=your-reset-secret-at-least-32-bytes

# Mail
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password

# Google OAuth
GOOGLE_OAUTH_CLIENT_ID=your-google-client-id
GOOGLE_OAUTH_CLIENT_SECRET=your-google-client-secret
GOOGLE_OAUTH_REDIRECT_URI=http://localhost:8083/api/auth/google/callback

# MoMo
MOMO_ENDPOINT=https://test-payment.momo.vn/v2/gateway/api/create
MOMO_PARTNER_CODE=MOMO
MOMO_ACCESS_KEY=your-access-key
MOMO_SECRET_KEY=your-secret-key
MOMO_REDIRECT_URL=http://localhost:5173/payment/result
MOMO_IPN_URL=https://your-ngrok-url/api/payments/momo/ipn
```

### 3. Run the application

```bash
cd app
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The server starts on **`http://localhost:8083/api`**.

### 4. Access Swagger UI

```
http://localhost:8083/api/swagger-ui.html
```

---

## 🐳 Docker Deployment (Production)

### 1. Configure environment

```bash
cp .env.prod.example .env
# Edit .env with your production values
```

### 2. Pull and start all services

```bash
docker compose -f docker-compose.prod.yml up -d
```

This starts:
- **MySQL 8.4** with health check
- **Redis 7** with password and AOF persistence
- **App** (pulled from GitHub Container Registry) — waits for DB & Redis to be healthy

---

## 🔐 Authentication Flow

```
POST /api/auth/register         → Register with email
POST /api/auth/login            → Login → access token (cookie) + refresh token (cookie)
POST /api/auth/refresh          → Renew access token using refresh token
POST /api/auth/google           → Initiate Google OAuth login
GET  /api/auth/google/callback  → Google OAuth callback
POST /api/auth/forgot-password  → Send reset OTP to email
POST /api/auth/reset-password   → Reset password using OTP
POST /api/auth/logout           → Invalidate tokens
```

See [`docs/AUTH_API.md`](docs/AUTH_API.md) for full details.

---

## 💳 Payment Flow

```
POST /api/payments/momo         → Create MoMo payment → receive payment URL
GET  /api/payments/status/{id}  → Check latest payment status for an invoice
POST /api/payments/momo/ipn     → MoMo IPN callback (webhook, called by MoMo server)
```

A scheduled job runs every 60 seconds to automatically cancel invoices that have been unpaid for more than 15 minutes.

See [`docs/PAYMENT_FLOW.md`](docs/PAYMENT_FLOW.md) for full details.

---

## 📋 Key Business Rules

- **Slot holding**: A time slot must be held (via `POST /appointments/holds`) before creating an appointment. Hold expires in **10 minutes** and is stored in Redis.
- **Appointment lifecycle**: `PENDING` → `CONFIRMED` → `COMPLETED` (or `CANCELLED`)
- **Invoice lifecycle**: `UNPAID` → `PAID` (via MoMo) or cancelled after 15 minutes unpaid.
- **Reviews**: Only the customer who owns the appointment can review it. The appointment must be in `COMPLETED` status. Each appointment can only be reviewed once. Reviews start with `PENDING` status and must be approved by admin.
- **Staff shifts**: Appointments can only be booked on approved staff shifts with no blocking conflicts.

---

## 🗂️ Project Structure

```
Appointment_Booking_System/
├── app/                                  # Main Spring Boot module
│   ├── src/main/java/com/abs/app/
│   │   ├── application/                  # CQRS handlers (commands & queries)
│   │   │   ├── admin/
│   │   │   ├── auth/
│   │   │   ├── staff/
│   │   │   └── user/
│   │   ├── common/                       # Shared utilities
│   │   │   ├── constant/
│   │   │   ├── exception/
│   │   │   ├── response/
│   │   │   └── util/
│   │   ├── config/                       # Spring Security config
│   │   ├── domain/                       # Core domain
│   │   │   ├── entity/
│   │   │   ├── repository/               # Port interfaces
│   │   │   └── service/                  # Domain services
│   │   ├── infrastructure/               # Adapters & implementations
│   │   │   ├── file/                     # File storage service
│   │   │   ├── mapper/                   # Entity → DTO mappers
│   │   │   ├── persistence/              # JPA repos & adapters
│   │   │   ├── scheduler/                # Scheduled jobs
│   │   │   └── security/                 # JWT, SecurityUtils
│   │   └── presentation/
│   │       └── controller/               # REST controllers
│   └── src/main/resources/
│       ├── application.yaml
│       ├── application-local.yaml
│       └── application-prod.yaml
├── docs/                                 # API & flow documentation
│   ├── AUTH_API.md
│   ├── PAYMENT_FLOW.md
│   └── STAFF_API.md
├── docker-compose.yml                    # Local dev infrastructure
├── docker-compose.prod.yml               # Production deployment
├── .env.prod.example                     # Environment variable template
└── PROJECT_STRUCTURE.md                  # Detailed project structure guide
```

---

## 🧪 Testing

```bash
cd app
./mvnw test
```

Unit tests are located in `app/src/test/java/com/abs/app/unit/`.

---

## 🤝 Contributing

1. Branch from `dev`: `git checkout -b feat/<feature-name>`
2. Follow the commit convention: `feat|fix|chore|docs|refactor|test(<scope>): <message>`
3. Open a Pull Request to `dev`
4. At least 1 review approval required before merge

---

## 📄 License

This project is for educational purposes under **YoEdu**.
