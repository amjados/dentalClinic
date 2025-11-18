# Dental Clinic Management System

A comprehensive Spring Boot application for managing a dental clinic's operations including patient management, dentist management, appointment scheduling, and treatment records.

## Features

### Core Functionality
- **Patient Management**: Complete patient records with personal information, medical history, and allergies
- **Dentist Management**: Dentist profiles with specializations and experience
- **Appointment Scheduling**: Book, update, and manage appointments with conflict detection
- **Treatment Records**: Track treatments, costs, prescriptions, and follow-ups

### Technical Features
- **REST APIs**: Full CRUD operations for all entities
- **Database Integration**: JPA/Hibernate with SQL Server support
- **API Documentation**: Swagger/OpenAPI integration
- **Data Validation**: Input validation with proper error handling
- **Security**: Spring Security configuration
- **Sample Data**: Pre-populated test data for development

## Technology Stack

- **Backend**: Spring Boot 3.1.5
- **Database**: SQL Server (production)
- **Security**: Spring Security
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Build Tool**: Maven
- **Java Version**: 17

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd dental-clinic-management
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the application:
   - **API Documentation**: http://localhost:8080/swagger-ui.html
   - **Dashboard Stats**: http://localhost:8080/api/dashboard/stats

## API Endpoints

### Patients
- `GET /api/patients` - Get all patients
- `GET /api/patients/{id}` - Get patient by ID
- `POST /api/patients` - Create new patient
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient
- `GET /api/patients/search?searchTerm={term}` - Search patients

### Dentists
- `GET /api/dentists` - Get all dentists
- `GET /api/dentists/{id}` - Get dentist by ID
- `POST /api/dentists` - Create new dentist
- `PUT /api/dentists/{id}` - Update dentist
- `DELETE /api/dentists/{id}` - Delete dentist
- `GET /api/dentists/specialization/{specialization}` - Find by specialization

### Appointments
- `GET /api/appointments` - Get all appointments
- `GET /api/appointments/{id}` - Get appointment by ID
- `POST /api/appointments` - Create new appointment
- `PUT /api/appointments/{id}` - Update appointment
- `DELETE /api/appointments/{id}` - Delete appointment
- `GET /api/appointments/patient/{patientId}` - Get patient appointments
- `GET /api/appointments/dentist/{dentistId}` - Get dentist appointments

### Treatments
- `GET /api/treatments` - Get all treatments
- `GET /api/treatments/{id}` - Get treatment by ID
- `POST /api/treatments` - Create new treatment
- `PUT /api/treatments/{id}` - Update treatment
- `DELETE /api/treatments/{id}` - Delete treatment
- `GET /api/treatments/patient/{patientId}` - Get patient treatments
- `GET /api/treatments/follow-up` - Get treatments requiring follow-up

### Dashboard
- `GET /api/dashboard/stats` - Get clinic statistics

## Sample Data

The application comes with pre-populated sample data including:
- 3 sample patients
- 3 sample dentists
- 3 sample appointments
- 3 sample treatments

## Configuration

### Database Configuration (application.properties)
```properties
# SQL Server Database Configuration
spring.datasource.url=jdbc:sqlserver://localhost:50506;databaseName=dentalclinic;encrypt=false;trustServerCertificate=true
spring.datasource.username=amjad
spring.datasource.password=amjadOmar1!A

# JPA Configuration for SQL Server
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
```

### Key Configuration Files
- `application.properties` - Main configuration
- `SecurityConfig.java` - Security settings
- `DataInitializer.java` - Sample data setup

## Data Model

### Patient Entity
- Personal information (name, email, phone, address)
- Medical history and allergies
- Date of birth and emergency contact
- Relationships with appointments and treatments

### Dentist Entity
- Professional information (name, license number, specialization)
- Experience and contact details
- Relationships with appointments and treatments

### Appointment Entity
- Patient and dentist references
- Date/time and duration
- Status tracking (SCHEDULED, CONFIRMED, COMPLETED, etc.)
- Reason and notes

### Treatment Entity
- Patient and dentist references
- Treatment details and cost
- Status tracking and follow-up requirements
- Prescriptions and notes

## Development

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/dentalclinic/dentalclinicmanagement/
│   │       ├── config/          # Configuration classes
│   │       ├── controller/      # REST controllers
│   │       ├── entity/          # JPA entities
│   │       ├── repository/      # Data repositories
│   │       └── service/         # Business logic
│   └── resources/
│       └── application.properties
└── test/
    └── java/                    # Test classes
```

### Building for Production

1. Update database configuration in `application.properties`
2. Build the JAR file:
   ```bash
   mvn clean package
   ```
3. Run the JAR:
   ```bash
   java -jar target/dental-clinic-management-0.0.1-SNAPSHOT.jar
   ```

## Future Enhancements

- User authentication and role-based access
- Email notifications for appointments
- Payment tracking and billing
- Reporting and analytics
- Mobile application support
- Integration with dental equipment

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.