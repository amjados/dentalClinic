-- Switch to the new database
USE dentalclinic;
GO

-- Enable error handling
SET XACT_ABORT ON;

BEGIN TRY
    PRINT 'Dropping And Creating database tables...';

    -- =============================================================================
    -- STEP 1: TABLE DROPPING
    -- =============================================================================
    -- Drop tables if they exist (in correct order to avoid FK constraints)
    IF OBJECT_ID('user_activity_summaries', 'U') IS NOT NULL DROP TABLE user_activity_summaries;
    IF OBJECT_ID('api_usage_summaries', 'U') IS NOT NULL DROP TABLE api_usage_summaries;
    IF OBJECT_ID('api_usage_logs', 'U') IS NOT NULL DROP TABLE api_usage_logs;
    IF OBJECT_ID('prescriptions', 'U') IS NOT NULL DROP TABLE prescriptions;
    IF OBJECT_ID('treatments', 'U') IS NOT NULL DROP TABLE treatments;
    IF OBJECT_ID('appointments', 'U') IS NOT NULL DROP TABLE appointments;
    IF OBJECT_ID('user_claims', 'U') IS NOT NULL DROP TABLE user_claims;
    IF OBJECT_ID('user_roles', 'U') IS NOT NULL DROP TABLE user_roles;
    IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;
    IF OBJECT_ID('employee_clinic', 'U') IS NOT NULL DROP TABLE employee_clinic;
    IF OBJECT_ID('patients', 'U') IS NOT NULL DROP TABLE patients;
    IF OBJECT_ID('employees', 'U') IS NOT NULL DROP TABLE employees;
    IF OBJECT_ID('clinics', 'U') IS NOT NULL DROP TABLE clinics;
    IF OBJECT_ID('persons', 'U') IS NOT NULL DROP TABLE persons;
    IF OBJECT_ID('cities', 'U') IS NOT NULL DROP TABLE cities;
    IF OBJECT_ID('cg_ref_code', 'U') IS NOT NULL DROP TABLE cg_ref_code;

    PRINT 'Existing tables dropped successfully!';

    -- =============================================================================
    -- STEP 2: TABLE CREATION
    -- =============================================================================
    PRINT 'Creating tables...'; 
    -- Reference codes table
    CREATE TABLE cg_ref_code (
        id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        code_id NVARCHAR(50) NOT NULL,
        code_name NVARCHAR(100) NOT NULL,
        code_display_value NVARCHAR(255) NOT NULL,
        code_lng NVARCHAR(10) NOT NULL DEFAULT 'en',
        code_desc NVARCHAR(500),
        code_value NVARCHAR(100),
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE()
    );

    CREATE INDEX IX_cg_ref_code_name ON cg_ref_code (code_name);
    CREATE INDEX IX_cg_ref_code_id ON cg_ref_code (code_id);
    CREATE INDEX IX_cg_ref_code_value ON cg_ref_code (code_value);

    PRINT 'Table cg_ref_code created successfully!';

    -- Cities table
    CREATE TABLE cities (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        name NVARCHAR(100) NOT NULL,
        state NVARCHAR(100),
        country NVARCHAR(100) NOT NULL,
        timezone NVARCHAR(50),
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE()
    );

    PRINT 'Table cities created successfully!';

    -- Clinics table
    CREATE TABLE clinics (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        address NVARCHAR(500),
        phone NVARCHAR(20),
        email NVARCHAR(255),
        city_id BIGINT,
        operating_hours NVARCHAR(255),
        license_number NVARCHAR(50),
        timezone_id NVARCHAR(50) DEFAULT 'UTC',
        active BIT NOT NULL DEFAULT 1, -- Indicates if the clinic is active
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_clinics_city_id FOREIGN KEY (city_id) REFERENCES cities (id)
    );

    PRINT 'Table clinics created successfully!';

    -- Persons table (central registry)
    CREATE TABLE persons (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        first_name NVARCHAR(100) NOT NULL,
        last_name NVARCHAR(100) NOT NULL,
        middle_name NVARCHAR(100),
        email NVARCHAR(255),
        phone NVARCHAR(20),
        mobile_phone NVARCHAR(20),
        date_of_birth DATE,
        gender NVARCHAR(10) CHECK (
            gender IN ('MALE', 'FEMALE', 'OTHER')
        ),
        nationality NVARCHAR(100),
        address NVARCHAR(500),
        city NVARCHAR(100),
        state NVARCHAR(100),
        postal_code NVARCHAR(20),
        country NVARCHAR(100),
        emergency_contact_name NVARCHAR(255),
        emergency_contact_phone NVARCHAR(20),
        emergency_contact_relationship NVARCHAR(100),
        profile_photo VARBINARY(MAX), -- Stores the profile photo for the person
        is_active BIT NOT NULL DEFAULT 1,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE()
    );

    CREATE INDEX IX_persons_email ON persons (email);
    CREATE INDEX IX_persons_phone ON persons (phone);
    CREATE INDEX IX_persons_name ON persons (last_name, first_name);

    PRINT 'Table persons created successfully!';

    -- Employees table
    CREATE TABLE employees (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        prs_id BIGINT NOT NULL,
        employee_number NVARCHAR(50) UNIQUE,
        employee_type NVARCHAR(50) NOT NULL,
        department NVARCHAR(100),
        position_title NVARCHAR(255),
        license_number NVARCHAR(100),
        specialization NVARCHAR(255),
        qualification NVARCHAR(500),
        experience_years INT,
        hire_date DATE,
        termination_date DATE,
        employment_status NVARCHAR(50) DEFAULT 'ACTIVE',
        salary DECIMAL(12, 2),
        hourly_rate DECIMAL(8, 2),
        work_schedule NVARCHAR(255),
        supervisor_id BIGINT,
        clinic_id BIGINT,
        can_work_multiple_clinics BIT DEFAULT 0,
        notes NVARCHAR(MAX),
        matrix_room_id NVARCHAR(255),
        matrix_user_id NVARCHAR(255),
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_employees_prs_id FOREIGN KEY (prs_id) REFERENCES persons (id),
        CONSTRAINT FK_employees_clinic_id FOREIGN KEY (clinic_id) REFERENCES clinics (id),
        CONSTRAINT FK_employees_supervisor_id FOREIGN KEY (supervisor_id) REFERENCES employees (id)
    );

    CREATE INDEX IX_employees_type ON employees (employee_type);
    CREATE INDEX IX_employees_clinic ON employees (clinic_id);

    PRINT 'Table employees created successfully!';

    -- Employee clinic access
    CREATE TABLE employee_clinic (
        employee_id BIGINT NOT NULL,
        clinic_id BIGINT NOT NULL,
        access_level NVARCHAR(50) DEFAULT 'FULL',
        start_date DATE,
        end_date DATE,
        is_active BIT NOT NULL DEFAULT 1,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        PRIMARY KEY (employee_id, clinic_id),
        CONSTRAINT FK_employee_clinic_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
        CONSTRAINT FK_employee_clinic_clinic FOREIGN KEY (clinic_id) REFERENCES clinics (id)
    );

    PRINT 'Table employee_clinic created successfully!';

    -- Patients table
    CREATE TABLE patients (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        prs_id BIGINT NOT NULL,
        patient_number NVARCHAR(50) UNIQUE,
        medical_history NVARCHAR(MAX),
        current_medications NVARCHAR(MAX),
        allergies NVARCHAR(MAX),
        blood_type NVARCHAR(10),
        insurance_provider NVARCHAR(255),
        insurance_policy_number NVARCHAR(100),
        primary_clinic_id BIGINT,
        patient_status NVARCHAR(50) DEFAULT 'ACTIVE',
        notes NVARCHAR(MAX),
        matrix_room_id NVARCHAR(255),
        matrix_user_id NVARCHAR(255),
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_patients_prs_id FOREIGN KEY (prs_id) REFERENCES persons (id),
        CONSTRAINT FK_patients_clinic_id FOREIGN KEY (primary_clinic_id) REFERENCES clinics (id)
    );

    CREATE INDEX IX_patients_status ON patients (patient_status);
    CREATE INDEX IX_patients_clinic ON patients (primary_clinic_id);

    PRINT 'Table patients created successfully!';

    -- Users table
    CREATE TABLE users (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        prs_id BIGINT,
        username NVARCHAR(50) UNIQUE NOT NULL,
        password NVARCHAR(255) NOT NULL,
        role NVARCHAR(50) NOT NULL,
        access_scope NVARCHAR(50),
        enabled BIT NOT NULL DEFAULT 1,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_users_prs_id FOREIGN KEY (prs_id) REFERENCES persons (id)
    );

    CREATE INDEX IX_users_username ON users (username);
    CREATE INDEX IX_users_role ON users (role);

    PRINT 'Table users created successfully!';

    -- User roles table
    CREATE TABLE user_roles (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        role_name NVARCHAR(100) NOT NULL,
        role_json_details NVARCHAR(MAX),
        granted_date DATE NOT NULL,
        expiry_date DATE,
        granted_by BIGINT,
        is_active BIT NOT NULL DEFAULT 1,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id),
        CONSTRAINT FK_user_roles_granted_by FOREIGN KEY (granted_by) REFERENCES users (id)
    );

    PRINT 'Table user_roles created successfully!';

    -- User claims table
    CREATE TABLE user_claims (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        claim_name NVARCHAR(100) NOT NULL,
        claim_json_details NVARCHAR(MAX),
        scope NVARCHAR(50),
        granted_date DATE NOT NULL DEFAULT GETDATE(),
        expiry_date DATE,
        granted_by BIGINT,
        is_active BIT NOT NULL DEFAULT 1,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_user_claims_user_id FOREIGN KEY (user_id) REFERENCES users (id),
        CONSTRAINT FK_user_claims_granted_by FOREIGN KEY (granted_by) REFERENCES users (id)
    );

    PRINT 'Table user_claims created successfully!';

    -- Appointments table
    CREATE TABLE appointments (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        patient_id BIGINT NOT NULL,
        request_by_employee_id BIGINT NOT NULL,
        served_by_employee_id BIGINT,
        clinic_id BIGINT NOT NULL,
        appointment_datetime DATETIME2 NOT NULL,
        estimated_duration_minutes INT,
        actual_duration_minutes INT,
        status NVARCHAR(50) DEFAULT 'SCHEDULED',
        appointment_type NVARCHAR(100),
        reason NVARCHAR(MAX),
        notes NVARCHAR(MAX),
        cancellation_reason NVARCHAR(500),
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_appointments_patient_id FOREIGN KEY (patient_id) REFERENCES patients (id),
        CONSTRAINT FK_appointments_request_by_employee_id FOREIGN KEY (request_by_employee_id) REFERENCES employees (id),
        CONSTRAINT FK_appointments_served_by_employee_id FOREIGN KEY (served_by_employee_id) REFERENCES employees (id),
        CONSTRAINT FK_appointments_clinic_id FOREIGN KEY (clinic_id) REFERENCES clinics (id)
    );

    CREATE INDEX IX_appointments_datetime ON appointments (appointment_datetime);
    CREATE INDEX IX_appointments_patient ON appointments (patient_id);
    CREATE INDEX IX_appointments_request_by_employee ON appointments (request_by_employee_id);
    CREATE INDEX IX_appointments_served_by_employee ON appointments (served_by_employee_id);

    PRINT 'Table appointments created successfully!';

    -- Treatments table
    CREATE TABLE treatments (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        patient_id BIGINT NOT NULL,
        request_by_employee_id BIGINT NOT NULL,
        served_by_employee_id BIGINT,
        appointment_id BIGINT,
        clinic_id BIGINT NOT NULL,
        treatment_code NVARCHAR(50),
        treatment_name NVARCHAR(255) NOT NULL,
        description NVARCHAR(MAX),
        treatment_category NVARCHAR(100),
        treatment_date DATE,
        estimated_cost DECIMAL(10, 2),
        actual_cost DECIMAL(10, 2),
        anesthesia_used NVARCHAR(200), -- Added for anesthesia tracking
        status NVARCHAR(50) DEFAULT 'PLANNED',
        tooth_numbers NVARCHAR(100),
        treatment_notes NVARCHAR(MAX),
        follow_up_required BIT DEFAULT 0,
        follow_up_date DATE,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_treatments_patient_id FOREIGN KEY (patient_id) REFERENCES patients (id),
        CONSTRAINT FK_treatments_request_by_employee_id FOREIGN KEY (request_by_employee_id) REFERENCES employees (id),
        CONSTRAINT FK_treatments_served_by_employee_id FOREIGN KEY (served_by_employee_id) REFERENCES employees (id),
        CONSTRAINT FK_treatments_appointment_id FOREIGN KEY (appointment_id) REFERENCES appointments (id),
        CONSTRAINT FK_treatments_clinic_id FOREIGN KEY (clinic_id) REFERENCES clinics (id)
    );

    CREATE INDEX IX_treatments_patient ON treatments (patient_id);
    CREATE INDEX IX_treatments_request_by_employee ON treatments (request_by_employee_id);
    CREATE INDEX IX_treatments_served_by_employee ON treatments (served_by_employee_id);
    CREATE INDEX IX_treatments_date ON treatments (treatment_date);

    PRINT 'Table treatments created successfully!';

    -- Prescriptions table
    CREATE TABLE prescriptions (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        patient_id BIGINT NOT NULL,
        request_by_employee_id BIGINT NOT NULL,
        served_by_employee_id BIGINT,
        treatment_id BIGINT,
        clinic_id BIGINT NOT NULL,
        prescription_number NVARCHAR(50) UNIQUE,
        medication_name NVARCHAR(255) NOT NULL,
        medication_type NVARCHAR(100),
        dosage NVARCHAR(100),
        frequency NVARCHAR(100),
        duration NVARCHAR(100),
        quantity_prescribed INT,
        instructions NVARCHAR(MAX),
        status NVARCHAR(50) DEFAULT 'PENDING',
        prescribed_date DATE NOT NULL,
        dispensed_date DATE,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_prescriptions_patient_id FOREIGN KEY (patient_id) REFERENCES patients (id),
        CONSTRAINT FK_prescriptions_request_by_employee FOREIGN KEY (request_by_employee_id) REFERENCES employees (id),
        CONSTRAINT FK_prescriptions_served_by_employee FOREIGN KEY (served_by_employee_id) REFERENCES employees (id),
        CONSTRAINT FK_prescriptions_treatment_id FOREIGN KEY (treatment_id) REFERENCES treatments (id),
        CONSTRAINT FK_prescriptions_clinic_id FOREIGN KEY (clinic_id) REFERENCES clinics (id)
    );

    CREATE INDEX IX_prescriptions_patient ON prescriptions (patient_id);
    CREATE INDEX IX_prescriptions_status ON prescriptions (status);

    PRINT 'Table prescriptions created successfully!';

    -- API usage logs table
    CREATE TABLE api_usage_logs (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        user_id BIGINT,
        username NVARCHAR(50),
        user_role NVARCHAR(50),
        clinic_id BIGINT,
        city_id BIGINT,
        http_method NVARCHAR(10) NOT NULL,
        request_url NVARCHAR(1000),
        request_uri NVARCHAR(500),
        endpoint_pattern NVARCHAR(255),
        response_status INT,
        processing_time_ms BIGINT,
        client_ip NVARCHAR(45),
        user_agent NVARCHAR(1000),
        request_timestamp DATETIME2 NOT NULL DEFAULT GETDATE(),
        is_authenticated BIT DEFAULT 0
    );

    CREATE INDEX IX_api_usage_logs_timestamp ON api_usage_logs (request_timestamp);
    CREATE INDEX IX_api_usage_logs_user ON api_usage_logs (user_id);
    CREATE INDEX IX_api_usage_logs_endpoint ON api_usage_logs (endpoint_pattern);

    PRINT 'Table api_usage_logs created successfully!';

    -- API usage summaries table
    CREATE TABLE api_usage_summaries (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        summary_date DATE NOT NULL,
        endpoint_pattern NVARCHAR(255) NOT NULL,
        http_method NVARCHAR(10) NOT NULL,
        total_requests BIGINT NOT NULL DEFAULT 0,
        successful_requests BIGINT NOT NULL DEFAULT 0,
        failed_requests BIGINT NOT NULL DEFAULT 0,
        avg_response_time_ms DECIMAL(10, 2),
        min_response_time_ms BIGINT,
        max_response_time_ms BIGINT,
        unique_users INT,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE()
    );

    CREATE UNIQUE INDEX IX_api_usage_summaries_unique ON api_usage_summaries (
        summary_date,
        endpoint_pattern,
        http_method
    );

    PRINT 'Table api_usage_summaries created successfully!';

    -- User activity summaries table
    CREATE TABLE user_activity_summaries (
        id BIGINT IDENTITY(1, 1) NOT NULL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        activity_date DATE NOT NULL,
        total_requests BIGINT NOT NULL DEFAULT 0,
        successful_requests BIGINT NOT NULL DEFAULT 0,
        failed_requests BIGINT NOT NULL DEFAULT 0,
        avg_response_time_ms DECIMAL(10, 2),
        total_session_time_minutes INT,
        unique_endpoints_accessed INT,
        first_activity_at DATETIME2,
        last_activity_at DATETIME2,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_user_activity_summaries_user_id FOREIGN KEY (user_id) REFERENCES users (id)
    );

    CREATE UNIQUE INDEX IX_user_activity_summaries_unique ON user_activity_summaries (user_id, activity_date);

    PRINT 'Table user_activity_summaries created successfully!';

    PRINT '=============================================================================';
    PRINT 'All 16 tables created successfully!';
    PRINT '=============================================================================';

END TRY
BEGIN CATCH
    PRINT 'Error occurred during table creation:';
    PRINT ERROR_MESSAGE();
    THROW;
END CATCH;
