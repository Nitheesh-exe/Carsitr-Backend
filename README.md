# Nash Car Rentals (Brand: Nash)
*A Production-Grade Full-Stack Automotive Rental Platform*

---

## 1. System Architecture Overview

Nash Car Rentals is built using a clean two-tier architecture:
- **Frontend**: Responsive single-page styled customer showroom and executive admin portal built with semantic HTML5, modern CSS3 variables/glassmorphism, and modular ES6+ JavaScript.
- **Backend**: Spring Boot 3.2.x RESTful application powered by Java 17+, Spring Data JPA, Spring Security 6 with stateless JWT Bearer token authentication, and PostgreSQL.

```
nash-car-rental/
├── frontend/
│   ├── index.html              # Customer showroom, fleet showcase, filtering & reservation modal
│   ├── login.html              # Customer/Admin auth portal (login & registration)
│   ├── admin.html              # Executive admin dashboard (fleet CRUD, booking approvals, metrics)
│   ├── css/
│   │   ├── style.css           # Global dark theme luxury styling & modal system
│   │   └── admin.css           # Admin dashboard grid layout, KPI cards & tables
│   └── js/
│       ├── api.js              # Centralized API service & JWT storage helper
│       ├── auth.js             # Sign-in & sign-up forms + automatic role-based redirect
│       ├── main.js             # Fleet catalogue rendering, search, and reservation logic
│       └── admin.js            # KPI metrics, fleet management, and booking status controls
│
└── backend/
    ├── pom.xml                 # Maven build configuration
    └── src/
        └── main/
            ├── java/com/nash/carrental/
            │   ├── config/     # SecurityConfig, CorsConfig, DataInitializer
            │   ├── controller/ # AuthController, CarController, BookingController, AdminController
            │   ├── dto/        # Request & Response DTOs
            │   ├── entity/     # User, Role, Car, Booking, CarStatus, BookingStatus
            │   ├── repository/ # UserRepository, CarRepository, BookingRepository
            │   ├── security/   # JwtUtils, JwtAuthenticationFilter, UserPrincipal, UserDetailsServiceImpl
            │   └── service/    # AuthService, CarService, BookingService
            └── resources/
                ├── application.properties
                ├── schema.sql  # PostgreSQL DDL
                └── data.sql    # Sample fleet seed data with high-res Unsplash links
```

---

## 2. Prerequisites & Quickstart

### Step 1: Database Setup (PostgreSQL)
Ensure PostgreSQL is running locally on port `5432`. Connect to psql or pgAdmin and run:
```sql
CREATE DATABASE nash_db;
```

*(Optional: default credentials configured in `application.properties` are username `postgres` and password `postgres`).*

### Step 2: Running the Spring Boot Backend
Navigate to the backend directory and launch the application:
```bash
cd backend
mvn clean spring-boot:run
```
The server will boot on `http://localhost:8080`.
Upon launch, `DataInitializer` automatically verifies and populates:
1. **Pre-configured Accounts**:
   - **Executive Admin**: `admin@nash.com` | Password: `admin123`
   - **Prestige Customer**: `customer@nash.com` | Password: `customer123`
2. **Initial Fleet**: 8 exotic vehicles (Porsche 911 GT3 RS, Mercedes G63 AMG, BMW M8, Ferrari F8 Tributo, Tesla Model S Plaid, etc.) with high-resolution automotive imagery.

### Step 3: Launching the Frontend
Open `frontend/` using any static file server or your preferred browser:
```bash
cd ../frontend
# Option A: Python HTTP server
python3 -m http.server 3000

# Option B: Node serve / npx
npx serve -l 3000 .
```
Navigate to:
- **Showroom (Customer)**: `http://localhost:3000/index.html`
- **Authentication**: `http://localhost:3000/login.html`
- **Admin Command**: `http://localhost:3000/admin.html`

---

## 3. Pre-Configured Demo Credentials
| Role | Email | Password | Target Portal |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin@nash.com` | `admin123` | `admin.html` |
| **Customer** | `customer@nash.com` | `customer123` | `index.html` |

*(Tip: In `login.html`, click either of the **Quick Demo Credentials** buttons to instantly fill and sign in).*

---

## 4. REST API Endpoint Reference

### Authentication
- `POST /api/auth/register` - Create customer account.
- `POST /api/auth/login` - Authenticate customer or admin and return JWT Bearer token.

### Customer & Public Endpoints
- `GET /api/cars` - Query fleet with optional params `?category=Sports&brand=Porsche&status=AVAILABLE`.
- `GET /api/cars/{id}` - Fetch single vehicle specification.
- `POST /api/bookings` *(Authenticated)* - Create new rental reservation (`carId`, `startDate`, `endDate`).
- `GET /api/bookings/my-bookings` *(Authenticated)* - Retrieve logged-in customer's reservations.

### Executive Admin Endpoints (`ROLE_ADMIN`)
- `GET /api/admin/stats` - Fetch real-time KPI metrics (revenue, active rentals, total fleet, available fleet).
- `GET /api/admin/bookings` - List all reservations with optional status filter `?status=PENDING`.
- `PUT /api/admin/bookings/{id}/status` - Approve (`CONFIRMED`), Reject (`CANCELLED`), or Complete (`COMPLETED`) a booking.
- `POST /api/admin/cars` - Add a new vehicle to the fleet.
- `PUT /api/admin/cars/{id}` - Update vehicle rate, status, or details.
- `DELETE /api/admin/cars/{id}` - Remove a vehicle from fleet inventory.
