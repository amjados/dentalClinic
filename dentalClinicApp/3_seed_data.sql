-- Seed data for dentalclinic database
USE dentalclinic;
GO

SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    PRINT 'Seeding reference data...';

    -------------------------------------------------------------------------
    -- 1) cg_ref_code (generic reference codes)
    -------------------------------------------------------------------------
    INSERT INTO cg_ref_code (code_id, code_name, code_display_value, code_lng, code_desc, code_value)
    VALUES
        ('EMP_TYPE', 'EMPLOYEE_TYPE', 'Dentist', 'en', 'Employee type - Dentist', 'DENTIST'),
        ('EMP_TYPE', 'EMPLOYEE_TYPE', 'Nurse', 'en', 'Employee type - Nurse', 'NURSE'),
        ('EMP_TYPE', 'EMPLOYEE_TYPE', 'Receptionist', 'en', 'Employee type - Receptionist', 'RECEPTIONIST'),
        ('ROLE', 'USER_ROLE', 'Admin', 'en', 'System administrator', 'ADMIN'),
        ('ROLE', 'USER_ROLE', 'Doctor', 'en', 'Doctor application user', 'DOCTOR'),
        ('ROLE', 'USER_ROLE', 'Reception', 'en', 'Reception application user', 'RECEPTION'),
        ('APPT_STATUS', 'APPOINTMENT_STATUS', 'Scheduled', 'en', 'Appointment scheduled', 'SCHEDULED'),
        ('APPT_STATUS', 'APPOINTMENT_STATUS', 'Completed', 'en', 'Appointment completed', 'COMPLETED'),
        ('APPT_STATUS', 'APPOINTMENT_STATUS', 'Cancelled', 'en', 'Appointment cancelled', 'CANCELLED'),
        ('GENDER', 'PERSON_GENDER', 'Male', 'en', 'Male gender', 'MALE'),
        ('GENDER', 'PERSON_GENDER', 'Female', 'en', 'Female gender', 'FEMALE'),
        ('GENDER', 'PERSON_GENDER', 'Other', 'en', 'Other / not specified', 'OTHER');

    PRINT 'cg_ref_code seeded.';

    -------------------------------------------------------------------------
    -- 2) cities
    -------------------------------------------------------------------------
    INSERT INTO cities (name, state, country, timezone)
    VALUES
        ('Abu Dhabi', 'Abu Dhabi', 'United Arab Emirates', 'Asia/Dubai'),
        ('Dubai', 'Dubai', 'United Arab Emirates', 'Asia/Dubai'),
        ('Al Ain', 'Abu Dhabi', 'United Arab Emirates', 'Asia/Dubai');

    PRINT 'cities seeded.';

    -------------------------------------------------------------------------
    -- 3) clinics
    -- IDs will be:
    -- 1: SmileCare Dental Clinic - Abu Dhabi
    -- 2: SmileCare Dental Clinic - Dubai
    -------------------------------------------------------------------------
    INSERT INTO clinics (name, address, phone, email, city_id, operating_hours, license_number, timezone_id, active)
    VALUES
        ('SmileCare Dental Clinic - Abu Dhabi',
         'Hamdan Street, Abu Dhabi, UAE',
         '+971-2-555-1111',
         'info.ad@smilecare.com',
         1,
         'Sat-Thu 09:00-21:00',
         'AD-DC-001',
         'Asia/Dubai',
         1),
        ('SmileCare Dental Clinic - Dubai',
         'Sheikh Zayed Road, Dubai, UAE',
         '+971-4-555-2222',
         'info.dxb@smilecare.com',
         2,
         'Sat-Thu 10:00-22:00',
         'DXB-DC-001',
         'Asia/Dubai',
         1);

    PRINT 'clinics seeded.';

    -------------------------------------------------------------------------
    -- 4) persons
    -- IDs will be:
    -- 1: Dr. Ahmed (Dentist)
    -- 2: Nurse Fatima
    -- 3: Receptionist Omar
    -- 4: Patient Ali
    -- 5: Patient Sara
    -- 6: System Admin (non-medical user)
    -------------------------------------------------------------------------
    INSERT INTO persons (first_name, last_name, middle_name, email, phone, mobile_phone, date_of_birth,
                         gender, nationality, address, city, state, postal_code, country,
                         emergency_contact_name, emergency_contact_phone, emergency_contact_relationship,
                         is_active)
    VALUES
        ('Ahmed', 'Al Mansoori', NULL,
         'ahmed.mansoori@smilecare.com', '+971-2-555-1111', '+971-50-123-4567', '1980-05-10',
         'MALE', 'Emirati', 'Khalidiya, Abu Dhabi', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Mansoor Al Mansoori', '+971-50-111-2222', 'Brother',
         1),

        ('Fatima', 'Al Suwaidi', NULL,
         'fatima.suwaidi@smilecare.com', '+971-2-555-2222', '+971-50-234-5678', '1988-09-20',
         'FEMALE', 'Emirati', 'Al Reem Island, Abu Dhabi', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Aisha Al Suwaidi', '+971-50-333-4444', 'Sister',
         1),

        ('Omar', 'Hassan', NULL,
         'omar.hassan@smilecare.com', '+971-2-555-3333', '+971-55-345-6789', '1990-02-15',
         'MALE', 'Jordanian', 'Electra Street, Abu Dhabi', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Hassan Ahmad', '+962-79-111-2222', 'Father',
         1),

        ('Ali', 'Khan', NULL,
         'ali.khan@example.com', '+971-2-555-4444', '+971-50-456-7890', '1995-07-01',
         'MALE', 'Pakistani', 'Muroor Road, Abu Dhabi', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Imran Khan', '+92-300-111-2222', 'Father',
         1),

        ('Sara', 'Mohammed', NULL,
         'sara.mohammed@example.com', '+971-2-555-5555', '+971-50-567-8901', '1998-11-12',
         'FEMALE', 'Sudanese', 'Tourist Club Area, Abu Dhabi', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Mohammed Ali', '+249-91-111-3333', 'Father',
         1),

        ('John', 'Doe', NULL,
         'admin@dentalclinic.local', '+971-2-555-6666', '+971-50-678-9012', '1975-03-25',
         'MALE', 'Canadian', 'Somewhere in Abu Dhabi', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Jane Doe', '+1-416-555-0000', 'Spouse',
         1);

    PRINT 'persons seeded.';

    -------------------------------------------------------------------------
    -- 5) employees
    -- IDs will match insert order:
    -- 1: Dr Ahmed (Dentist)  -> prs_id = 1
    -- 2: Nurse Fatima        -> prs_id = 2
    -- 3: Receptionist Omar   -> prs_id = 3
    -------------------------------------------------------------------------
    INSERT INTO employees (prs_id, employee_number, employee_type, department, position_title,
                           license_number, specialization, qualification, experience_years,
                           hire_date, employment_status, salary, hourly_rate, work_schedule,
                           supervisor_id, clinic_id, can_work_multiple_clinics, notes, matrix_room_id, matrix_user_id)
    VALUES
        (1, 'EMP-0001', 'DENTIST', 'Dentistry', 'Senior Dentist',
         'DENT-AD-001', 'General Dentistry', 'BDS, MDS', 15,
         '2010-01-01', 'ACTIVE', 45000.00, 0.00, 'Sat-Thu 09:00-17:00',
         NULL, 1, 1,
         'Clinic owner and lead dentist', '@room:matrix.local', '@dr.ahmed:matrix.local'),

        (2, 'EMP-0002', 'NURSE', 'Nursing', 'Dental Nurse',
         NULL, 'Dental Nursing', 'BSc Nursing', 8,
         '2015-06-01', 'ACTIVE', 18000.00, 0.00, 'Sat-Thu 09:00-17:00',
         1, 1, 0,
         'Assists dentist in all procedures', '@room:matrix.local', '@nurse.fatima:matrix.local'),

        (3, 'EMP-0003', 'RECEPTIONIST', 'Front Desk', 'Receptionist',
         NULL, 'Front Desk', 'Diploma in Business', 5,
         '2018-09-15', 'ACTIVE', 12000.00, 0.00, 'Sat-Thu 09:00-18:00',
         1, 1, 0,
         'Handles appointments and calls', '@room:matrix.local', '@omar.reception:matrix.local');

    PRINT 'employees seeded.';

    -------------------------------------------------------------------------
    -- 6) employee_clinic (access to multiple clinics)
    -------------------------------------------------------------------------
    INSERT INTO employee_clinic (employee_id, clinic_id, access_level, start_date, end_date, is_active)
    VALUES
        (1, 1, 'FULL', '2010-01-01', NULL, 1), -- Dr Ahmed Abu Dhabi
        (1, 2, 'LIMITED', '2020-01-01', NULL, 1), -- Dr Ahmed also works in Dubai
        (2, 1, 'FULL', '2015-06-01', NULL, 1), -- Nurse Fatima Abu Dhabi
        (3, 1, 'FULL', '2018-09-15', NULL, 1); -- Receptionist Omar Abu Dhabi

    PRINT 'employee_clinic seeded.';

    -------------------------------------------------------------------------
    -- 7) patients
    -- IDs will be:
    -- 1: Ali (prs_id 4)
    -- 2: Sara (prs_id 5)
    -------------------------------------------------------------------------
    INSERT INTO patients (prs_id, patient_number, medical_history, current_medications, allergies,
                          blood_type, insurance_provider, insurance_policy_number, primary_clinic_id,
                          patient_status, notes, matrix_room_id, matrix_user_id)
    VALUES
        (4, 'PAT-0001',
         'No chronic diseases. Occasional tooth sensitivity.',
         'Vitamin D supplement',
         'No known allergies',
         'O+', 'Daman', 'DAM-123456', 1,
         'ACTIVE',
         'Prefers evening appointments.',
         '@room:matrix.local', '@ali.khan:matrix.local'),

        (5, 'PAT-0002',
         'Mild asthma. History of orthodontic treatment.',
         'Asthma inhaler as needed',
         'Allergic to penicillin',
         'A-', 'Thiqa', 'THQ-654321', 1,
         'ACTIVE',
         'Nervous about dental procedures, needs reassurance.',
         '@room:matrix.local', '@sara.mohammed:matrix.local');

    PRINT 'patients seeded.';

    -------------------------------------------------------------------------
    -- 8) users (app users)
    -- IDs will be:
    -- 1: admin
    -- 2: dr.ahmed
    -- 3: reception1
    -------------------------------------------------------------------------
    INSERT INTO users (prs_id, username, password, role, access_scope, enabled)
    VALUES
        (6, 'admin', 'admin-hash-placeholder', 'ADMIN', 'GLOBAL', 1),
        (1, 'dr.ahmed', 'dr-ahmed-hash-placeholder', 'DOCTOR', 'CLINIC', 1),
        (3, 'reception1', 'reception-hash-placeholder', 'RECEPTION', 'CLINIC', 1);

    PRINT 'users seeded.';

    -------------------------------------------------------------------------
    -- 9) user_roles
    -------------------------------------------------------------------------
    INSERT INTO user_roles (user_id, role_name, role_json_details, granted_date, expiry_date, granted_by, is_active)
    VALUES
        (1, 'ADMIN', '{"permissions":["ALL"]}', '2020-01-01', NULL, 1, 1),
        (2, 'DOCTOR', '{"permissions":["APPOINTMENTS_VIEW","TREATMENTS_MANAGE","PRESCRIPTIONS_MANAGE"]}', '2020-01-01', NULL, 1, 1),
        (3, 'RECEPTION', '{"permissions":["APPOINTMENTS_MANAGE","PATIENTS_VIEW"]}', '2020-01-01', NULL, 1, 1);

    PRINT 'user_roles seeded.';

    -------------------------------------------------------------------------
    -- 10) user_claims
    -------------------------------------------------------------------------
    INSERT INTO user_claims (user_id, claim_name, claim_json_details, scope, granted_date, expiry_date, granted_by, is_active)
    VALUES
        (1, 'CAN_MANAGE_USERS', '{"allowed":true}', 'GLOBAL', GETDATE(), NULL, 1, 1),
        (2, 'CAN_SIGN_PRESCRIPTIONS', '{"maxDaily":50}', 'CLINIC', GETDATE(), NULL, 1, 1),
        (3, 'CAN_BOOK_APPOINTMENTS', '{"channels":["phone","online"]}', 'CLINIC', GETDATE(), NULL, 1, 1);

    PRINT 'user_claims seeded.';

    -------------------------------------------------------------------------
    -- 11) appointments
    -- IDs will be:
    -- 1: Ali checkup
    -- 2: Sara filling
    -------------------------------------------------------------------------
    INSERT INTO appointments (patient_id, request_by_employee_id, served_by_employee_id, clinic_id,
                              appointment_datetime, estimated_duration_minutes, actual_duration_minutes,
                              status, appointment_type, reason, notes, cancellation_reason)
    VALUES
        (1, 3, 1, 1,
         DATEADD(DAY, -2, DATEADD(HOUR, 16, CAST(CONVERT(date, GETDATE()) AS datetime2))), -- 2 days ago 4 PM
         30, 28,
         'COMPLETED', 'CHECKUP', 'Routine dental checkup',
         'Patient arrived on time. Advised better brushing technique.',
         NULL),

        (2, 3, 1, 1,
         DATEADD(DAY, -1, DATEADD(HOUR, 18, CAST(CONVERT(date, GETDATE()) AS datetime2))), -- 1 day ago 6 PM
         45, 50,
         'COMPLETED', 'FILLING', 'Tooth pain in lower molar',
         'Composite filling placed on tooth #36.',
         NULL),

        (1, 3, NULL, 1,
         DATEADD(DAY, 3, DATEADD(HOUR, 19, CAST(CONVERT(date, GETDATE()) AS datetime2))), -- in 3 days 7 PM
         30, NULL,
         'SCHEDULED', 'FOLLOW_UP', 'Follow-up after filling',
         'Check sensitivity level and healing.',
         NULL);

    PRINT 'appointments seeded.';

    -------------------------------------------------------------------------
    -- 12) treatments
    -- IDs will be:
    -- 1: Cleaning for Ali (appt 1)
    -- 2: Filling for Sara (appt 2)
    -- 3: Planned follow-up for Ali (appt 3)
    -------------------------------------------------------------------------
    INSERT INTO treatments (patient_id, request_by_employee_id, served_by_employee_id, appointment_id, clinic_id,
                            treatment_code, treatment_name, description, treatment_category, treatment_date,
                            estimated_cost, actual_cost, anesthesia_used, status, tooth_numbers,
                            treatment_notes, follow_up_required, follow_up_date)
    VALUES
        (1, 3, 1, 1, 1,
         'CLN-001', 'Scaling & Polishing',
         'Full mouth scaling and polishing.',
         'PREVENTIVE',
         DATEADD(DAY, -2, CAST(CONVERT(date, GETDATE()) AS date)),
         250.00, 250.00, NULL,
         'COMPLETED',
         NULL,
         'Mild calculus removed. Recommended 6-month recall.',
         0, NULL),

        (2, 3, 1, 2, 1,
         'FIL-001', 'Composite Filling',
         'Composite restoration on lower left first molar.',
         'RESTORATIVE',
         DATEADD(DAY, -1, CAST(CONVERT(date, GETDATE()) AS date)),
         450.00, 450.00, 'Articaine 4% with epinephrine',
         'COMPLETED',
         '36',
         'Patient tolerated procedure well.',
         1, DATEADD(DAY, 7, CAST(CONVERT(date, GETDATE()) AS date))),

        (1, 3, 1, 3, 1,
         'REV-001', 'Follow-up Review',
         'Follow-up review after cleaning and sensitivity management.',
         'CONSULTATION',
         DATEADD(DAY, 3, CAST(CONVERT(date, GETDATE()) AS date)),
         150.00, NULL, NULL,
         'PLANNED',
         NULL,
         'Check overall oral hygiene and sensitivity.',
         0, NULL);

    PRINT 'treatments seeded.';

    -------------------------------------------------------------------------
    -- 13) prescriptions
    -------------------------------------------------------------------------
    INSERT INTO prescriptions (patient_id, request_by_employee_id, served_by_employee_id, treatment_id, clinic_id,
                               prescription_number, medication_name, medication_type, dosage, frequency, duration,
                               quantity_prescribed, instructions, status, prescribed_date, dispensed_date)
    VALUES
        (2, 1, 1, 2, 1,
         'RX-2025-0001', 'Ibuprofen 400mg', 'Tablet', '400mg', 'Every 8 hours', '3 days',
         9,
         'Take after food. Stop if stomach pain occurs.',
         'DISPENSED',
         CAST(CONVERT(date, GETDATE()) AS date),
         CAST(CONVERT(date, GETDATE()) AS date)),

        (2, 1, 1, 2, 1,
         'RX-2025-0002', 'Chlorhexidine mouthwash 0.12%', 'Mouthwash', '10ml', 'Twice daily', '7 days',
         1,
         'Rinse for 30 seconds and do not swallow.',
         'PENDING',
         CAST(CONVERT(date, GETDATE()) AS date),
         NULL);

    PRINT 'prescriptions seeded.';

    -------------------------------------------------------------------------
    -- 14) api_usage_logs
    -------------------------------------------------------------------------
    INSERT INTO api_usage_logs (user_id, username, user_role, clinic_id, city_id,
                                http_method, request_url, request_uri, endpoint_pattern,
                                response_status, processing_time_ms, client_ip, user_agent,
                                request_timestamp, is_authenticated)
    VALUES
        (1, 'admin', 'ADMIN', NULL, NULL,
         'GET', 'https://api.dentalclinic.local/admin/users', '/admin/users', '/admin/users',
         200, 45, '192.168.1.10', 'PostmanRuntime/7.39.0',
         DATEADD(MINUTE, -120, SYSDATETIME()), 1),

        (2, 'dr.ahmed', 'DOCTOR', 1, 1,
         'GET', 'https://api.dentalclinic.local/appointments/today', '/appointments/today', '/appointments/*',
         200, 60, '192.168.1.11', 'Mozilla/5.0',
         DATEADD(MINUTE, -90, SYSDATETIME()), 1),

        (3, 'reception1', 'RECEPTION', 1, 1,
         'POST', 'https://api.dentalclinic.local/appointments', '/appointments', '/appointments',
         201, 120, '192.168.1.12', 'Mozilla/5.0',
         DATEADD(MINUTE, -30, SYSDATETIME()), 1),

        (3, 'reception1', 'RECEPTION', 1, 1,
         'POST', 'https://api.dentalclinic.local/patients', '/patients', '/patients',
         500, 350, '192.168.1.12', 'Mozilla/5.0',
         DATEADD(MINUTE, -25, SYSDATETIME()), 1);

    PRINT 'api_usage_logs seeded.';

    -------------------------------------------------------------------------
    -- 15) api_usage_summaries
    -------------------------------------------------------------------------
    DECLARE @today DATE = CAST(GETDATE() AS date);

    INSERT INTO api_usage_summaries (summary_date, endpoint_pattern, http_method,
                                     total_requests, successful_requests, failed_requests,
                                     avg_response_time_ms, min_response_time_ms, max_response_time_ms, unique_users)
    VALUES
        (@today, '/appointments', 'POST',
         2, 1, 1,
         235.00, 120, 350, 1),

        (@today, '/appointments/*', 'GET',
         1, 1, 0,
         60.00, 60, 60, 1),

        (@today, '/admin/users', 'GET',
         1, 1, 0,
         45.00, 45, 45, 1);

    PRINT 'api_usage_summaries seeded.';

    -------------------------------------------------------------------------
    -- 16) user_activity_summaries
    -------------------------------------------------------------------------
    INSERT INTO user_activity_summaries (user_id, activity_date,
                                         total_requests, successful_requests, failed_requests,
                                         avg_response_time_ms, total_session_time_minutes,
                                         unique_endpoints_accessed, first_activity_at, last_activity_at)
    VALUES
        (1, @today,
         1, 1, 0,
         45.00, 30,
         1,
         DATEADD(MINUTE, -130, SYSDATETIME()),
         DATEADD(MINUTE, -120, SYSDATETIME())),

        (2, @today,
         1, 1, 0,
         60.00, 45,
         1,
         DATEADD(MINUTE, -100, SYSDATETIME()),
         DATEADD(MINUTE, -90, SYSDATETIME())),

        (3, @today,
         2, 1, 1,
         235.00, 60,
         2,
         DATEADD(MINUTE, -35, SYSDATETIME()),
         DATEADD(MINUTE, -25, SYSDATETIME()));

    PRINT 'user_activity_summaries seeded.';

    COMMIT TRANSACTION;

    PRINT '=========================================================';
    PRINT 'Seed data inserted successfully for all tables.';
    PRINT '=========================================================';

END TRY
BEGIN CATCH
    ROLLBACK TRANSACTION;
    PRINT 'Error occurred during seeding:';
    PRINT ERROR_MESSAGE();
    THROW;
END CATCH;
