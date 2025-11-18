-- Seed data for dentalclinic_v2 database (multilingual version)
USE dentalclinic_v2;
GO

SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    PRINT 'Seeding multilingual data...';

    -------------------------------------------------------------------------
    -- 1) languages (REQUIRED FIRST - referenced by all translation tables)
    -------------------------------------------------------------------------
    INSERT INTO languages (code, name)
    VALUES
        ('en', 'English'),
        ('ar', 'Arabic'),
        ('hi', 'Hindi');

    PRINT 'languages seeded: en, ar, hi';

    -------------------------------------------------------------------------
    -- 2) cg_ref_code (BASE TABLE - language-neutral data only)
    -------------------------------------------------------------------------
    -- IDs will be:
    -- 1-3: EMP_TYPE (DENTIST, NURSE, RECEPTIONIST)
    -- 4-6: ROLE (ADMIN, DOCTOR, RECEPTION)
    -- 7-9: APPT_STATUS (SCHEDULED, COMPLETED, CANCELLED)
    -- 10-12: GENDER (MALE, FEMALE, OTHER)
    -------------------------------------------------------------------------
    INSERT INTO cg_ref_code (code_id, code_lng, code_value)
    VALUES
        ('EMP_TYPE', 'en', 'DENTIST'),
        ('EMP_TYPE', 'en', 'NURSE'),
        ('EMP_TYPE', 'en', 'RECEPTIONIST'),
        ('ROLE', 'en', 'ADMIN'),
        ('ROLE', 'en', 'DOCTOR'),
        ('ROLE', 'en', 'RECEPTION'),
        ('APPT_STATUS', 'en', 'SCHEDULED'),
        ('APPT_STATUS', 'en', 'COMPLETED'),
        ('APPT_STATUS', 'en', 'CANCELLED'),
        ('GENDER', 'en', 'MALE'),
        ('GENDER', 'en', 'FEMALE'),
        ('GENDER', 'en', 'OTHER');

    PRINT 'cg_ref_code base table seeded.';

    -------------------------------------------------------------------------
    -- 3) cg_ref_code_tr (TRANSLATION TABLE)
    -- Columns: code_name, code_display_value, code_desc
    -------------------------------------------------------------------------
    INSERT INTO cg_ref_code_tr (cg_ref_code_id, language_code, code_name, code_display_value, code_desc)
    VALUES
        -- ID 1: DENTIST
        (1, 'en', 'EMPLOYEE_TYPE', 'Dentist', 'Employee type - Dentist'),
        (1, 'ar', 'EMPLOYEE_TYPE', 'طبيب أسنان', 'نوع الموظف - طبيب أسنان'),
        (1, 'hi', 'EMPLOYEE_TYPE', 'दंत चिकित्सक', 'कर्मचारी प्रकार - दंत चिकित्सक'),

        -- ID 2: NURSE
        (2, 'en', 'EMPLOYEE_TYPE', 'Nurse', 'Employee type - Nurse'),
        (2, 'ar', 'EMPLOYEE_TYPE', 'ممرضة', 'نوع الموظف - ممرضة'),
        (2, 'hi', 'EMPLOYEE_TYPE', 'नर्स', 'कर्मचारी प्रकार - नर्स'),

        -- ID 3: RECEPTIONIST
        (3, 'en', 'EMPLOYEE_TYPE', 'Receptionist', 'Employee type - Receptionist'),
        (3, 'ar', 'EMPLOYEE_TYPE', 'موظف استقبال', 'نوع الموظف - موظف استقبال'),
        (3, 'hi', 'EMPLOYEE_TYPE', 'रिसेप्शनिस्ट', 'कर्मचारी प्रकार - रिसेप्शनिस्ट'),

        -- ID 4: ADMIN
        (4, 'en', 'USER_ROLE', 'Admin', 'System administrator'),
        (4, 'ar', 'USER_ROLE', 'مسؤول', 'مسؤول النظام'),
        (4, 'hi', 'USER_ROLE', 'व्यवस्थापक', 'सिस्टम प्रशासक'),

        -- ID 5: DOCTOR
        (5, 'en', 'USER_ROLE', 'Doctor', 'Doctor application user'),
        (5, 'ar', 'USER_ROLE', 'طبيب', 'مستخدم تطبيق الطبيب'),
        (5, 'hi', 'USER_ROLE', 'डॉक्टर', 'डॉक्टर अनुप्रयोग उपयोगकर्ता'),

        -- ID 6: RECEPTION
        (6, 'en', 'USER_ROLE', 'Reception', 'Reception application user'),
        (6, 'ar', 'USER_ROLE', 'استقبال', 'مستخدم تطبيق الاستقبال'),
        (6, 'hi', 'USER_ROLE', 'रिसेप्शन', 'रिसेप्शन अनुप्रयोग उपयोगकर्ता'),

        -- ID 7: SCHEDULED
        (7, 'en', 'APPOINTMENT_STATUS', 'Scheduled', 'Appointment scheduled'),
        (7, 'ar', 'APPOINTMENT_STATUS', 'مجدول', 'الموعد مجدول'),
        (7, 'hi', 'APPOINTMENT_STATUS', 'निर्धारित', 'नियुक्ति निर्धारित'),

        -- ID 8: COMPLETED
        (8, 'en', 'APPOINTMENT_STATUS', 'Completed', 'Appointment completed'),
        (8, 'ar', 'APPOINTMENT_STATUS', 'مكتمل', 'الموعد مكتمل'),
        (8, 'hi', 'APPOINTMENT_STATUS', 'पूर्ण', 'नियुक्ति पूर्ण'),

        -- ID 9: CANCELLED
        (9, 'en', 'APPOINTMENT_STATUS', 'Cancelled', 'Appointment cancelled'),
        (9, 'ar', 'APPOINTMENT_STATUS', 'ملغى', 'الموعد ملغى'),
        (9, 'hi', 'APPOINTMENT_STATUS', 'रद्द', 'नियुक्ति रद्द'),

        -- ID 10: MALE
        (10, 'en', 'PERSON_GENDER', 'Male', 'Male gender'),
        (10, 'ar', 'PERSON_GENDER', 'ذكر', 'جنس ذكر'),
        (10, 'hi', 'PERSON_GENDER', 'पुरुष', 'पुरुष लिंग'),

        -- ID 11: FEMALE
        (11, 'en', 'PERSON_GENDER', 'Female', 'Female gender'),
        (11, 'ar', 'PERSON_GENDER', 'أنثى', 'جنس أنثى'),
        (11, 'hi', 'PERSON_GENDER', 'महिला', 'महिला लिंग'),

        -- ID 12: OTHER
        (12, 'en', 'PERSON_GENDER', 'Other', 'Other / not specified'),
        (12, 'ar', 'PERSON_GENDER', 'آخر', 'آخر / غير محدد'),
        (12, 'hi', 'PERSON_GENDER', 'अन्य', 'अन्य / निर्दिष्ट नहीं');

    PRINT 'cg_ref_code_tr seeded with 3 languages for 12 codes.';

    -------------------------------------------------------------------------
    -- 4) cities (BASE TABLE - language-neutral data only)
    -- IDs will be:
    -- 1: Abu Dhabi
    -- 2: Dubai
    -- 3: Al Ain
    -------------------------------------------------------------------------
    INSERT INTO cities (timezone)
    VALUES
        ('Asia/Dubai'),
        ('Asia/Dubai'),
        ('Asia/Dubai');

    PRINT 'cities base table seeded.';

    -------------------------------------------------------------------------
    -- 5) city_tr (TRANSLATION TABLE)
    -- Columns: name, state, country
    -------------------------------------------------------------------------
    INSERT INTO city_tr (city_id, language_code, name, state, country)
    VALUES
        -- ID 1: Abu Dhabi
        (1, 'en', 'Abu Dhabi', 'Abu Dhabi', 'United Arab Emirates'),
        (1, 'ar', 'أبو ظبي', 'أبو ظبي', 'الإمارات العربية المتحدة'),
        (1, 'hi', 'अबू धाबी', 'अबू धाबी', 'संयुक्त अरब अमीरात'),

        -- ID 2: Dubai
        (2, 'en', 'Dubai', 'Dubai', 'United Arab Emirates'),
        (2, 'ar', 'دبي', 'دبي', 'الإمارات العربية المتحدة'),
        (2, 'hi', 'दुबई', 'दुबई', 'संयुक्त अरब अमीरात'),

        -- ID 3: Al Ain
        (3, 'en', 'Al Ain', 'Abu Dhabi', 'United Arab Emirates'),
        (3, 'ar', 'العين', 'أبو ظبي', 'الإمارات العربية المتحدة'),
        (3, 'hi', 'अल ऐन', 'अबू धाबी', 'संयुक्त अरब अमीरात');

    PRINT 'city_tr seeded with 3 languages for 3 cities.';

    -------------------------------------------------------------------------
    -- 6) clinics (BASE TABLE - language-neutral data only)
    -- IDs will be:
    -- 1: SmileCare Dental Clinic - Abu Dhabi
    -- 2: SmileCare Dental Clinic - Dubai
    -------------------------------------------------------------------------
    INSERT INTO clinics (phone, email, city_id, operating_hours, license_number, timezone_id, active)
    VALUES
        ('+971-2-555-1111', 'info.ad@smilecare.com', 1, 'Sat-Thu 09:00-21:00', 'AD-DC-001', 'Asia/Dubai', 1),
        ('+971-4-555-2222', 'info.dxb@smilecare.com', 2, 'Sat-Thu 10:00-22:00', 'DXB-DC-001', 'Asia/Dubai', 1);

    PRINT 'clinics base table seeded.';

    -------------------------------------------------------------------------
    -- 7) clinic_tr (TRANSLATION TABLE)
    -- Columns: name, address
    -------------------------------------------------------------------------
    INSERT INTO clinic_tr (clinic_id, language_code, name, address)
    VALUES
        -- ID 1: Abu Dhabi Clinic
        (1, 'en', 'SmileCare Dental Clinic - Abu Dhabi', 'Hamdan Street, Abu Dhabi, UAE'),
        (1, 'ar', 'عيادة سمايل كير للأسنان - أبوظبي', 'شارع حمدان، أبوظبي، الإمارات'),
        (1, 'hi', 'स्माइलकेयर डेंटल क्लिनिक - अबू धाबी', 'हमदान स्ट्रीट, अबू धाबी, यूएई'),

        -- ID 2: Dubai Clinic
        (2, 'en', 'SmileCare Dental Clinic - Dubai', 'Sheikh Zayed Road, Dubai, UAE'),
        (2, 'ar', 'عيادة سمايل كير للأسنان - دبي', 'شارع الشيخ زايد، دبي، الإمارات'),
        (2, 'hi', 'स्माइलकेयर डेंटल क्लिनिक - दुबई', 'शेख ज़ायद रोड, दुबई, यूएई');

    PRINT 'clinic_tr seeded with 3 languages for 2 clinics.';

    -------------------------------------------------------------------------
    -- 8) persons (BASE TABLE - language-neutral data only)
    -- IDs will be:
    -- 1: Dr. Ahmed (Dentist)
    -- 2: Nurse Fatima
    -- 3: Receptionist Omar
    -- 4: Patient Ali
    -- 5: Patient Sara
    -- 6: System Admin
    -------------------------------------------------------------------------
    INSERT INTO persons (email, phone, mobile_phone, date_of_birth, gender, nationality,
                         city, state, postal_code, country,
                         emergency_contact_name, emergency_contact_phone, is_active)
    VALUES
        ('ahmed.mansoori@smilecare.com', '+971-2-555-1111', '+971-50-123-4567', '1980-05-10',
         'MALE', 'Emirati', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Mansoor Al Mansoori', '+971-50-111-2222', 1),

        ('fatima.suwaidi@smilecare.com', '+971-2-555-2222', '+971-50-234-5678', '1988-09-20',
         'FEMALE', 'Emirati', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Aisha Al Suwaidi', '+971-50-333-4444', 1),

        ('omar.hassan@smilecare.com', '+971-2-555-3333', '+971-55-345-6789', '1990-02-15',
         'MALE', 'Jordanian', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Hassan Ahmad', '+962-79-111-2222', 1),

        ('ali.khan@example.com', '+971-2-555-4444', '+971-50-456-7890', '1995-07-01',
         'MALE', 'Pakistani', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Imran Khan', '+92-300-111-2222', 1),

        ('sara.mohammed@example.com', '+971-2-555-5555', '+971-50-567-8901', '1998-11-12',
         'FEMALE', 'Sudanese', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Mohammed Ali', '+249-91-111-3333', 1),

        ('admin@dentalclinic.local', '+971-2-555-6666', '+971-50-678-9012', '1975-03-25',
         'MALE', 'Canadian', 'Abu Dhabi', 'Abu Dhabi', '00000', 'United Arab Emirates',
         'Jane Doe', '+1-416-555-0000', 1);

    PRINT 'persons base table seeded.';

    -------------------------------------------------------------------------
    -- 9) person_tr (TRANSLATION TABLE)
    -- Columns: first_name, middle_name, last_name, address, emergency_contact_relationship
    -------------------------------------------------------------------------
    INSERT INTO person_tr (person_id, language_code, first_name, middle_name, last_name, address, emergency_contact_relationship)
    VALUES
        -- ID 1: Dr. Ahmed
        (1, 'en', 'Ahmed', NULL, 'Al Mansoori', 'Khalidiya, Abu Dhabi', 'Brother'),
        (1, 'ar', 'أحمد', NULL, 'المنصوري', 'الخالدية، أبوظبي', 'أخ'),
        (1, 'hi', 'अहमद', NULL, 'अल मंसूरी', 'खालिदिया, अबू धाबी', 'भाई'),

        -- ID 2: Nurse Fatima
        (2, 'en', 'Fatima', NULL, 'Al Suwaidi', 'Al Reem Island, Abu Dhabi', 'Sister'),
        (2, 'ar', 'فاطمة', NULL, 'السويدي', 'جزيرة الريم، أبوظبي', 'أخت'),
        (2, 'hi', 'फातिमा', NULL, 'अल सुवैदी', 'अल रीम आइलैंड, अबू धाबी', 'बहन'),

        -- ID 3: Receptionist Omar
        (3, 'en', 'Omar', NULL, 'Hassan', 'Electra Street, Abu Dhabi', 'Father'),
        (3, 'ar', 'عمر', NULL, 'حسان', 'شارع الإلكترا، أبوظبي', 'أب'),
        (3, 'hi', 'उमर', NULL, 'हसन', 'इलेक्ट्रा स्ट्रीट, अबू धाबी', 'पिता'),

        -- ID 4: Patient Ali
        (4, 'en', 'Ali', NULL, 'Khan', 'Muroor Road, Abu Dhabi', 'Father'),
        (4, 'ar', 'علي', NULL, 'خان', 'طريق المرور، أبوظبي', 'أب'),
        (4, 'hi', 'अली', NULL, 'खान', 'मुरूर रोड, अबू धाबी', 'पिता'),

        -- ID 5: Patient Sara
        (5, 'en', 'Sara', NULL, 'Mohammed', 'Tourist Club Area, Abu Dhabi', 'Father'),
        (5, 'ar', 'سارة', NULL, 'محمد', 'منطقة النادي السياحي، أبوظبي', 'أب'),
        (5, 'hi', 'सारा', NULL, 'मोहम्मद', 'टूरिस्ट क्लब एरिया, अबू धाबी', 'पिता'),

        -- ID 6: System Admin
        (6, 'en', 'John', NULL, 'Doe', 'Somewhere in Abu Dhabi', 'Spouse'),
        (6, 'ar', 'جون', NULL, 'دو', 'في مكان ما في أبوظبي', 'زوج/زوجة'),
        (6, 'hi', 'जॉन', NULL, 'डो', 'अबू धाबी में कहीं', 'जीवनसाथी');

    PRINT 'person_tr seeded with 3 languages for 6 persons.';

    -------------------------------------------------------------------------
    -- 10) employees (BASE TABLE - language-neutral data only)
    -- IDs will be:
    -- 1: Dr Ahmed (prs_id=1)
    -- 2: Nurse Fatima (prs_id=2)
    -- 3: Receptionist Omar (prs_id=3)
    -------------------------------------------------------------------------
    INSERT INTO employees (prs_id, employee_number, employee_type, license_number, experience_years,
                           hire_date, employment_status, salary, hourly_rate, work_schedule,
                           supervisor_id, clinic_id, can_work_multiple_clinics, matrix_room_id, matrix_user_id)
    VALUES
        (1, 'EMP-0001', 'DENTIST', 'DENT-AD-001', 15,
         '2010-01-01', 'ACTIVE', 45000.00, 0.00, 'Sat-Thu 09:00-17:00',
         NULL, 1, 1, '@room:matrix.local', '@dr.ahmed:matrix.local'),

        (2, 'EMP-0002', 'NURSE', NULL, 8,
         '2015-06-01', 'ACTIVE', 18000.00, 0.00, 'Sat-Thu 09:00-17:00',
         1, 1, 0, '@room:matrix.local', '@nurse.fatima:matrix.local'),

        (3, 'EMP-0003', 'RECEPTIONIST', NULL, 5,
         '2018-09-15', 'ACTIVE', 12000.00, 0.00, 'Sat-Thu 09:00-18:00',
         1, 1, 0, '@room:matrix.local', '@omar.reception:matrix.local');

    PRINT 'employees base table seeded.';

    -------------------------------------------------------------------------
    -- 11) employee_tr (TRANSLATION TABLE)
    -- Columns: department, position_title, specialization, qualification, notes
    -------------------------------------------------------------------------
    INSERT INTO employee_tr (employee_id, language_code, department, position_title, specialization, qualification, notes)
    VALUES
        -- ID 1: Dr Ahmed
        (1, 'en', 'Dentistry', 'Senior Dentist', 'General Dentistry', 'BDS, MDS', 'Clinic owner and lead dentist'),
        (1, 'ar', 'طب الأسنان', 'طبيب أسنان أول', 'طب الأسنان العام', 'بكالوريوس طب الأسنان، ماجستير', 'مالك العيادة وطبيب الأسنان الرئيسي'),
        (1, 'hi', 'दंत चिकित्सा', 'वरिष्ठ दंत चिकित्सक', 'सामान्य दंत चिकित्सा', 'बीडीएस, एमडीएस', 'क्लिनिक मालिक और प्रमुख दंत चिकित्सक'),

        -- ID 2: Nurse Fatima
        (2, 'en', 'Nursing', 'Dental Nurse', 'Dental Nursing', 'BSc Nursing', 'Assists dentist in all procedures'),
        (2, 'ar', 'التمريض', 'ممرضة أسنان', 'تمريض الأسنان', 'بكالوريوس التمريض', 'تساعد الطبيب في جميع الإجراءات'),
        (2, 'hi', 'नर्सिंग', 'डेंटल नर्स', 'डेंटल नर्सिंग', 'बीएससी नर्सिंग', 'सभी प्रक्रियाओं में दंत चिकित्सक की सहायता करती है'),

        -- ID 3: Receptionist Omar
        (3, 'en', 'Front Desk', 'Receptionist', 'Front Desk', 'Diploma in Business', 'Handles appointments and calls'),
        (3, 'ar', 'مكتب الاستقبال', 'موظف استقبال', 'مكتب الاستقبال', 'دبلوم في الأعمال', 'يتعامل مع المواعيد والمكالمات'),
        (3, 'hi', 'फ्रंट डेस्क', 'रिसेप्शनिस्ट', 'फ्रंट डेस्क', 'बिजनेस में डिप्लोमा', 'अपॉइंटमेंट और कॉल संभालता है');

    PRINT 'employee_tr seeded with 3 languages for 3 employees.';

    -------------------------------------------------------------------------
    -- 12) employee_clinic (access to multiple clinics)
    -------------------------------------------------------------------------
    INSERT INTO employee_clinic (employee_id, clinic_id, access_level, start_date, end_date, is_active)
    VALUES
        (1, 1, 'FULL', '2010-01-01', NULL, 1), -- Dr Ahmed Abu Dhabi
        (1, 2, 'LIMITED', '2020-01-01', NULL, 1), -- Dr Ahmed also works in Dubai
        (2, 1, 'FULL', '2015-06-01', NULL, 1), -- Nurse Fatima Abu Dhabi
        (3, 1, 'FULL', '2018-09-15', NULL, 1); -- Receptionist Omar Abu Dhabi

    PRINT 'employee_clinic seeded.';

    -------------------------------------------------------------------------
    -- 13) patients (BASE TABLE - language-neutral data only)
    -- IDs will be:
    -- 1: Ali (prs_id=4)
    -- 2: Sara (prs_id=5)
    -------------------------------------------------------------------------
    INSERT INTO patients (prs_id, patient_number, blood_type, insurance_provider, insurance_policy_number,
                          primary_clinic_id, patient_status, matrix_room_id, matrix_user_id)
    VALUES
        (4, 'PAT-0001', 'O+', 'Daman', 'DAM-123456', 1,
         'ACTIVE', '@room:matrix.local', '@ali.khan:matrix.local'),

        (5, 'PAT-0002', 'A-', 'Thiqa', 'THQ-654321', 1,
         'ACTIVE', '@room:matrix.local', '@sara.mohammed:matrix.local');

    PRINT 'patients base table seeded.';

    -------------------------------------------------------------------------
    -- 14) patient_tr (TRANSLATION TABLE)
    -- Columns: medical_history, current_medications, allergies, notes
    -------------------------------------------------------------------------
    INSERT INTO patient_tr (patient_id, language_code, medical_history, current_medications, allergies, notes)
    VALUES
        -- ID 1: Patient Ali
        (1, 'en', 'No chronic diseases. Occasional tooth sensitivity.', 'Vitamin D supplement', 'No known allergies', 'Prefers evening appointments.'),
        (1, 'ar', 'لا توجد أمراض مزمنة. حساسية الأسنان العرضية.', 'مكمل فيتامين د', 'لا توجد حساسية معروفة', 'يفضل المواعيد المسائية.'),
        (1, 'hi', 'कोई पुरानी बीमारी नहीं। कभी-कभार दांत संवेदनशील।', 'विटामिन डी सप्लीमेंट', 'कोई ज्ञात एलर्जी नहीं', 'शाम की नियुक्तियों को प्राथमिकता देता है।'),

        -- ID 2: Patient Sara
        (2, 'en', 'Mild asthma. History of orthodontic treatment.', 'Asthma inhaler as needed', 'Allergic to penicillin', 'Nervous about dental procedures, needs reassurance.'),
        (2, 'ar', 'ربو خفيف. تاريخ من علاج تقويم الأسنان.', 'بخاخ الربو عند الحاجة', 'حساسية من البنسلين', 'قلق من إجراءات الأسنان، تحتاج إلى الطمأنينة.'),
        (2, 'hi', 'हल्का दमा। ऑर्थोडॉन्टिक उपचार का इतिहास।', 'आवश्यकतानुसार अस्थमा इनहेलर', 'पेनिसिलिन से एलर्जी', 'दंत प्रक्रियाओं के बारे में चिंतित, आश्वासन की आवश्यकता है।');

    PRINT 'patient_tr seeded with 3 languages for 2 patients.';

    -------------------------------------------------------------------------
    -- 15) users (app users)
    -- IDs will be:
    -- 1: admin (prs_id=6)
    -- 2: dr.ahmed (prs_id=1)
    -- 3: reception1 (prs_id=3)
    -------------------------------------------------------------------------
    INSERT INTO users (prs_id, username, password, role, access_scope, enabled)
    VALUES
        (6, 'admin', 'admin-hash-placeholder', 'ADMIN', 'GLOBAL', 1),
        (1, 'dr.ahmed', 'dr-ahmed-hash-placeholder', 'DOCTOR', 'CLINIC', 1),
        (3, 'reception1', 'reception-hash-placeholder', 'RECEPTION', 'CLINIC', 1);

    PRINT 'users seeded.';

    -------------------------------------------------------------------------
    -- 16) user_roles
    -------------------------------------------------------------------------
    INSERT INTO user_roles (user_id, role_name, role_json_details, granted_date, expiry_date, granted_by, is_active)
    VALUES
        (1, 'ADMIN', '{"permissions":["ALL"]}', '2020-01-01', NULL, 1, 1),
        (2, 'DOCTOR', '{"permissions":["APPOINTMENTS_VIEW","TREATMENTS_MANAGE","PRESCRIPTIONS_MANAGE"]}', '2020-01-01', NULL, 1, 1),
        (3, 'RECEPTION', '{"permissions":["APPOINTMENTS_MANAGE","PATIENTS_VIEW"]}', '2020-01-01', NULL, 1, 1);

    PRINT 'user_roles seeded.';

    -------------------------------------------------------------------------
    -- 17) user_claims
    -------------------------------------------------------------------------
    INSERT INTO user_claims (user_id, claim_name, claim_json_details, scope, granted_date, expiry_date, granted_by, is_active)
    VALUES
        (1, 'CAN_MANAGE_USERS', '{"allowed":true}', 'GLOBAL', GETDATE(), NULL, 1, 1),
        (2, 'CAN_SIGN_PRESCRIPTIONS', '{"maxDaily":50}', 'CLINIC', GETDATE(), NULL, 1, 1),
        (3, 'CAN_BOOK_APPOINTMENTS', '{"channels":["phone","online"]}', 'CLINIC', GETDATE(), NULL, 1, 1);

    PRINT 'user_claims seeded.';

    -------------------------------------------------------------------------
    -- 18) appointments
    -- IDs will be:
    -- 1: Ali checkup (COMPLETED)
    -- 2: Sara filling (COMPLETED)
    -- 3: Ali follow-up (SCHEDULED)
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
    -- 19) treatments
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
    -- 20) prescriptions
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
    -- 21) api_usage_logs
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
    -- 22) api_usage_summaries
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
    -- 23) user_activity_summaries
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
    PRINT 'MULTILINGUAL SEED DATA COMPLETED SUCCESSFULLY';
    PRINT '=========================================================';
    PRINT 'Languages: 3 (en, ar, hi)';
    PRINT 'Translation tables seeded: 6';
    PRINT '  - cg_ref_code_tr: 36 rows (12 codes × 3 languages)';
    PRINT '  - city_tr: 9 rows (3 cities × 3 languages)';
    PRINT '  - clinic_tr: 6 rows (2 clinics × 3 languages)';
    PRINT '  - person_tr: 18 rows (6 persons × 3 languages)';
    PRINT '  - employee_tr: 9 rows (3 employees × 3 languages)';
    PRINT '  - patient_tr: 6 rows (2 patients × 3 languages)';
    PRINT 'Total translation rows: 84';
    PRINT '=========================================================';

END TRY
BEGIN CATCH
    ROLLBACK TRANSACTION;
    PRINT 'Error occurred during seeding:';
    PRINT ERROR_MESSAGE();
    THROW;
END CATCH;
