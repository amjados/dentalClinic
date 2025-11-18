# 🚀 Dental Clinic Management System - Startup Guide

## ✅ Prerequisites Check

Before running the application, ensure you have:

- ✅ **Java 21 LTS** installed (`java -version`)
- ✅ **Maven 3.9+** installed (`mvn -version`)
- ✅ **Node.js 18+** and npm installed (`node -version`)
- ✅ **Database** (MySQL/SQL Server/H2) configured
- ✅ **Redis** (optional, for caching)
- ✅ **Matrix Synapse Server** (optional, for chat features)

---

## 🎯 Quick Start (Development Mode)

### Option 1: Run Both Backend & Frontend Separately

#### 1️⃣ Start the Backend (Spring Boot)

```powershell
# Navigate to backend directory
cd dentalClinic

# Run with Maven (will use H2 in-memory database for testing)
mvn spring-boot:run

# OR run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Backend will start at:** `http://localhost:8080`

**Check endpoints:**
- Health: http://localhost:8080/actuator/health
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

#### 2️⃣ Start the Frontend (React + Vite)

```powershell
# Open a NEW terminal/PowerShell window
cd dentalClinicUI

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

**Frontend will start at:** `http://localhost:5173`

---

## 🔧 Configuration

### Backend Configuration (dentalClinic)

**File:** `dentalClinic/src/main/resources/application.properties`

Key settings:
```properties
# Server Port
server.port=8080

# Database (uses H2 by default for testing)
# For production, configure MySQL/SQL Server

# Matrix Chat (optional)
matrix.enabled=true
matrix.homeserver.url=http://localhost:8008
```

### Frontend Configuration (dentalClinicUI)

**File:** `dentalClinicUI/.env`

```properties
# Backend API URL
VITE_API_URL=http://localhost:8080

# Matrix Homeserver (for chat)
VITE_MATRIX_HOMESERVER_URL=http://localhost:8008
```

---

## 📊 Database Setup

### Development (H2 In-Memory)
- No setup needed
- Automatically creates tables on startup
- Data is lost when application stops

### Production (MySQL)

1. Create database:
```sql
CREATE DATABASE dental_clinic CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Configure in `application-prod.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dental_clinic
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Production (SQL Server)

Configure in `application-prod.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=dental_clinic
spring.datasource.username=sa
spring.datasource.password=your_password
```

---

## 💬 Matrix Chat Setup (Optional)

### Quick Setup with Docker

1. Create a `docker-compose.yml` for Matrix Synapse:

```yaml
version: '3.8'
services:
  synapse:
    image: matrixdotorg/synapse:latest
    container_name: matrix-synapse
    ports:
      - "8008:8008"
    volumes:
      - ./synapse-data:/data
    environment:
      - SYNAPSE_SERVER_NAME=dentalclinic.local
      - SYNAPSE_REPORT_STATS=no
```

2. Generate config:
```powershell
docker run -it --rm -v ${PWD}/synapse-data:/data matrixdotorg/synapse:latest generate
```

3. Start Matrix:
```powershell
docker-compose up -d
```

4. Create bot user:
```powershell
# Register bot account
docker exec -it matrix-synapse register_new_matrix_user -c /data/homeserver.yaml http://localhost:8008
```

**Without Matrix:** The app will work fine, chat features will just be disabled.

---

## 🧪 Testing

### Backend Tests
```powershell
cd dentalClinic
mvn test
```

### Frontend Tests
```powershell
cd dentalClinicUI
npm test
```

---

## 🔐 Default Users & Authentication

### Test Users (after first run)

The application needs user accounts. You can:

1. **Register via API** (Swagger UI at http://localhost:8080/swagger-ui.html)
2. **Create via database insert**
3. **Use default admin** (if configured in data initialization)

### Login Flow

1. Open frontend: http://localhost:5173
2. Click "Login" button
3. Enter credentials:
   - For testing, use the backend API to create a user first
   - Check `src/main/resources/data.sql` for seed data

---

## 🐛 Troubleshooting

### Frontend can't connect to backend

**Problem:** API calls fail with CORS errors

**Solution:** 
- Ensure backend is running on `http://localhost:8080`
- Check `WebConfig.java` has correct CORS origins
- Verify `.env` file has `VITE_API_URL=http://localhost:8080`

### Backend won't start

**Problem:** Port 8080 already in use

**Solution:**
```powershell
# Change port in application.properties
server.port=8081

# Update frontend .env
VITE_API_URL=http://localhost:8081
```

### Database connection errors

**Problem:** Can't connect to MySQL/SQL Server

**Solution:**
```properties
# Use H2 for testing instead
spring.profiles.active=test
```

### Matrix chat not working

**Problem:** Matrix features don't work

**Solution:**
- Set `matrix.enabled=false` in application.properties to disable
- Or setup Matrix Synapse server (see Matrix Setup section)

---

## 📦 Build for Production

### Backend (Spring Boot JAR)
```powershell
cd dentalClinic
mvn clean package -Pprod
# JAR will be in target/dental-clinic-management-0.0.1-SNAPSHOT.jar
```

Run production JAR:
```powershell
java -jar target/dental-clinic-management-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Frontend (Static Build)
```powershell
cd dentalClinicUI
npm run build
# Build files will be in dist/
```

Serve with any static server or integrate with backend.

---

## 🌐 Access URLs

| Service | Development URL | Description |
|---------|----------------|-------------|
| **Frontend** | http://localhost:5173 | React UI (Vite) |
| **Backend API** | http://localhost:8080 | Spring Boot REST API |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | API Documentation |
| **H2 Console** | http://localhost:8080/h2-console | Database Console (dev) |
| **Actuator** | http://localhost:8080/actuator | Health & Metrics |
| **Matrix** | http://localhost:8008 | Matrix Homeserver (if enabled) |

---

## 📝 Development Workflow

### Making Changes

1. **Backend changes:**
   - Edit Java files in `dentalClinic/src/`
   - Spring Boot DevTools auto-reloads (if enabled)
   - Or restart with `mvn spring-boot:run`

2. **Frontend changes:**
   - Edit React/TypeScript files in `dentalClinicUI/src/`
   - Vite auto-reloads instantly
   - See changes at http://localhost:5173

### Adding New Features

1. **New Backend Endpoint:**
   - Create controller in `controller/` package
   - Add service logic in `service/` package
   - Test with Swagger UI

2. **New Frontend Page:**
   - Create component in `src/components/`
   - Add route if needed
   - Import in `App.tsx` or `DentalClinicUI.tsx`

---

## 🎨 Frontend Features

### Current UI Features
- ✅ Multi-language support (Arabic, English, Hindi)
- ✅ Multiple theme colors (Emerald, Sky, Rose, Violet, Amber, Dark)
- ✅ Role-based dashboards (Guest, Patient, Doctor, Admin, Super Admin)
- ✅ Responsive design (mobile, tablet, desktop)
- ✅ Authentication & authorization
- ⏳ Matrix chat integration (in progress)

### Using the UI

1. **Switch Language:** Click globe icon in header
2. **Change Theme:** Click palette icon in header
3. **Login:** Click "Login" button (requires backend user)
4. **View Different Roles:** Login with different user accounts

---

## 📚 API Documentation

Once backend is running, explore the API:

- **Interactive Docs:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

### Key Endpoints

```
POST   /api/auth/login          - Login
POST   /api/auth/register       - Register new user
GET    /api/patients            - List patients
POST   /api/patients            - Create patient
GET    /api/appointments        - List appointments
POST   /api/appointments        - Create appointment
GET    /api/matrix/rooms        - List chat rooms
POST   /api/matrix/rooms        - Create chat room
```

---

## 🔒 Security Notes

### Development
- CORS enabled for localhost
- H2 console accessible (dev profile)
- Swagger UI enabled

### Production
- Change default passwords
- Configure proper database credentials
- Disable H2 console
- Enable HTTPS
- Configure proper CORS origins
- Use environment variables for secrets

---

## 📞 Need Help?

### Common Commands Reference

```powershell
# Backend
cd dentalClinic
mvn clean install          # Build
mvn spring-boot:run        # Run
mvn test                   # Test
mvn package               # Package JAR

# Frontend
cd dentalClinicUI
npm install               # Install dependencies
npm run dev              # Development server
npm run build            # Production build
npm test                 # Run tests
npm run lint             # Check code style

# Both (from root)
# Open two terminals and run both commands
```

---

## ✅ Success Checklist

- [ ] Backend running at http://localhost:8080
- [ ] Frontend running at http://localhost:5173
- [ ] Can access Swagger UI
- [ ] Can see the homepage in browser
- [ ] Can change language and theme
- [ ] Database connection working (check logs)
- [ ] No CORS errors in browser console

---

## 🎉 You're All Set!

The application is now running. Open your browser and visit:

**http://localhost:5173**

Happy coding! 🚀
