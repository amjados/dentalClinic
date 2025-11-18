# Dental Clinic Management System - Project Instructions

## Overview
This is a comprehensive Spring Boot application for managing a dental clinic. It provides REST APIs for managing patients, dentists, appointments, and treatments with a SQL Server database backend.

## Architecture
- **Framework**: Spring Boot 3.1.5
- **Database**: SQL Server (running in Docker on localhost:50506)
- **ORM**: JPA/Hibernate with SQL Server dialect
- **Security**: Spring Security with basic configuration
- **Documentation**: Swagger/OpenAPI 3
- **Build**: Maven

## Database Configuration
The application connects to a SQL Server database with the following configuration:
- **Host**: localhost:50506
- **Database**: dentalclinic
- **Username**: amjad
- **Password**: amjadOmar1!A

### Database Initialization
The application features robust database initialization that:

1. **Checks Database Setup**: On startup, verifies if all required tables exist
2. **Creates Missing Components**: Automatically creates database and tables if they don't exist
3. **Smart Seeding**: Only seeds sample data in development profile and when tables are empty
4. **Error Handling**: Graceful handling of database connection issues with detailed logging

#### SQL Scripts
- `database/create_database.sql`: Database and table creation with existence checks
- `database/seed_data.sql`: Sample data for development and testing

#### Profile-Based Configuration
- **Development Profile** (`dev`): Automatic database setup and seeding
- **Production Profile** (`prod`): No automatic seeding, minimal logging
- **Default**: Development profile for easy local development

## Core Entities

### Patient
- Personal information (name, email, phone, address)
- Medical history and allergies
- Emergency contact information
- Relationships to appointments and treatments

### Dentist
- Professional information (license number, specialization)
- Experience tracking
- Contact details
- Relationships to appointments and treatments

### Appointment
- Scheduling with date/time
- Status tracking (SCHEDULED, CONFIRMED, COMPLETED, CANCELLED)
- Duration and reason
- Patient-dentist relationships

### Treatment
- Medical procedures and treatments
- Cost tracking
- Prescription management
- Follow-up requirements
- Status tracking (PLANNED, IN_PROGRESS, COMPLETED, CANCELLED)

## REST API Endpoints

### Patients (`/api/patients`)
- `GET /` - List all patients
- `GET /{id}` - Get patient by ID
- `POST /` - Create new patient
- `PUT /{id}` - Update patient
- `DELETE /{id}` - Delete patient
- `GET /search` - Search patients

### Dentists (`/api/dentists`)
- `GET /` - List all dentists
- `GET /{id}` - Get dentist by ID
- `POST /` - Create new dentist
- `PUT /{id}` - Update dentist
- `DELETE /{id}` - Delete dentist
- `GET /specialization/{specialization}` - Get dentists by specialization

### Appointments (`/api/appointments`)
- `GET /` - List all appointments
- `GET /{id}` - Get appointment by ID
- `POST /` - Create new appointment
- `PUT /{id}` - Update appointment
- `DELETE /{id}` - Delete appointment
- `GET /patient/{patientId}` - Get appointments by patient
- `GET /dentist/{dentistId}` - Get appointments by dentist
- `GET /date-range` - Get appointments in date range

### Treatments (`/api/treatments`)
- `GET /` - List all treatments
- `GET /{id}` - Get treatment by ID
- `POST /` - Create new treatment
- `PUT /{id}` - Update treatment
- `DELETE /{id}` - Delete treatment
- `GET /patient/{patientId}` - Get treatments by patient
- `GET /dentist/{dentistId}` - Get treatments by dentist

## Running the Application

### Prerequisites
1. Java 17 or higher
2. Maven 3.6+
3. SQL Server database running on localhost:50506
4. Database named 'dentalclinic' with credentials: amjad/amjadOmar1!A

### Development Mode
```bash
# Run with development profile (automatic database setup and seeding)
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Production Mode
```bash
# Run with production profile (no automatic seeding)
mvn spring-boot:run -Dspring.profiles.active=prod
```

### Default Mode
```bash
# Run with default configuration (development profile)
mvn spring-boot:run
```

## Testing
- Run unit tests: `mvn test`
- Integration tests available for all major components
- Swagger UI available at: http://localhost:8080/swagger-ui.html

## API Documentation
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Sample Data
In development mode, the application automatically creates sample data including:
- 5 sample patients with diverse medical histories
- 5 dentists with different specializations
- 7 appointments with various statuses
- 7 treatments covering different procedures

## Security
- Basic Spring Security configuration
- API endpoints are secured
- SQL injection protection through JPA
- Input validation on all entities

## Database Schema
The application automatically creates the following tables:
- `patients` - Patient information
- `dentists` - Dentist information  
- `appointments` - Appointment scheduling
- `treatments` - Treatment records

All tables include:
- Auto-generated primary keys
- Created/updated timestamps
- Proper foreign key relationships
- Appropriate indexes for performance

## Configuration Files
- `application.properties` - Main configuration (defaults to dev profile)
- `application-dev.properties` - Development profile settings
- `application-prod.properties` - Production profile settings

## Logging
- Development: DEBUG level for database operations
- Production: INFO/WARN level for performance
- Detailed SQL logging available in development mode

## Error Handling
- Comprehensive exception handling
- Graceful database connection error recovery
- Detailed error logging for troubleshooting
- User-friendly error responses

## Future Enhancements
- Authentication and authorization
- Email notifications
- Reporting and analytics
- Mobile app integration
- Payment processing integration