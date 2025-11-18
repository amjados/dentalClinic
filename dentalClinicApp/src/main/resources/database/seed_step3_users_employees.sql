
-- Seed employees
INSERT INTO employees (prs_id, employee_number, employee_type, license_number, experience_years, hire_date, work_status_cg_ref_value,
                       salary, hourly_rate, work_schedule, clinic_id, supervisor_id, matrix_room_id, matrix_user_id)
VALUES
(1, 'EMP001', 'Dentist', 'DENT-001', 5, '2018-06-01', NULL, 120000, 75, 'Mon-Fri', 1, NULL, 'room_1', 'user_1');


-- Seed employee_tr
INSERT INTO employee_tr (employee_id, language_code, department, position_title, specialization, qualification, notes)
VALUES
(1, 'en', 'Dental', 'Senior Dentist', 'Orthodontics', 'DDS', 'Experienced in braces and alignment');


-- Seed employee_clinic
INSERT INTO employee_clinic (employee_id, clinic_id, access_level, start_date)
VALUES
(1, 1, 'FULL', '2022-01-01');


-- Seed patients
INSERT INTO patients (prs_id, patient_number, blood_type, insurance_provider, insurance_policy_number, primary_clinic_id,
                      matrix_room_id, matrix_user_id)
VALUES
(2, 'PAT001', 'A+', 'HealthCare Inc.', 'POL12345', 1, 'room_2', 'user_2');


-- Seed patient_tr
INSERT INTO patient_tr (patient_id, language_code, medical_history, current_medications, allergies, notes)
VALUES
(1, 'en', 'No major illnesses', 'Ibuprofen', 'None', 'Regular visitor');


-- Seed users
INSERT INTO users (prs_id, username, password, role, access_scope)
VALUES
(1, 'johndoe', 'hashed_password1', 'ADMIN', 'ALL'),
(2, 'maryjane', 'hashed_password2', 'PATIENT', 'LIMITED');


-- Seed user_roles
INSERT INTO user_roles (user_id, role_name, role_json_details, granted_date)
VALUES
(1, 'ADMIN', '{"permissions":["ALL"]}', GETDATE()),
(2, 'PATIENT', '{"permissions":["VIEW_APPOINTMENTS"]}', GETDATE());


-- Seed user_claims
INSERT INTO user_claims (user_id, claim_name, claim_json_details, scope, granted_date)
VALUES
(1, 'DATA_ACCESS', '{"level":"FULL"}', 'ALL', GETDATE()),
(2, 'VIEW_OWN_DATA', '{"level":"LIMITED"}', 'PATIENT', GETDATE());
