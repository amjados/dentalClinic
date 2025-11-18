
-- Step 1: Seed cg_ref_code
USE dentalclinic_v2;
GO

INSERT INTO cg_ref_code (sub_domain, code_value, description) VALUES 
('gender', 'M', 'Male'),
('gender', 'F', 'Female'),
('nationality', 'EG', 'Egyptian'),
('nationality', 'SA', 'Saudi'),
('country', 'EG', 'Egypt'),
('country', 'US', 'United States'),
('city', 'CAI', 'Cairo'),
('city', 'NYC', 'New York'),
('work_status', 'EMP', 'Employed'),
('work_status', 'UNEMP', 'Unemployed'),
('appointment_type', 'CHK', 'Checkup'),
('appointment_type', 'SURG', 'Surgery'),
('is_canceled', 'YES', 'Canceled'),
('is_canceled', 'NO', 'Not Canceled');
GO


-- Step 2: Insert dummy rows into related tables

-- persons
INSERT INTO persons (id, name, gender_cg_ref_value, nationality_cg_ref_value, country_cg_ref_value, city_cg_ref_value, work_status_cg_ref_value) VALUES
(1, 'John Doe', NULL, NULL, NULL, NULL, NULL),
(2, 'Jane Smith', NULL, NULL, NULL, NULL, NULL);

-- appointments
INSERT INTO appointments (id, person_id, appointment_type_cg_ref_value, is_canceled_cg_ref_value) VALUES
(1, 1, NULL, NULL),
(2, 2, NULL, NULL);

-- treatments
INSERT INTO treatments (id, appointment_id, is_canceled_cg_ref_value) VALUES
(1, 1, NULL),
(2, 2, NULL);

-- prescriptions
INSERT INTO prescriptions (id, treatment_id, is_canceled_cg_ref_value) VALUES
(1, 1, NULL),
(2, 2, NULL);


-- Step 3: Populate *_cg_ref_value columns based on cg_ref_code

-- persons
UPDATE persons SET gender_cg_ref_value = (SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'gender') WHERE gender_cg_ref_value IS NULL;
UPDATE persons SET nationality_cg_ref_value = (SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'nationality') WHERE nationality_cg_ref_value IS NULL;
UPDATE persons SET country_cg_ref_value = (SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'country') WHERE country_cg_ref_value IS NULL;
UPDATE persons SET city_cg_ref_value = (SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'city') WHERE city_cg_ref_value IS NULL;
UPDATE persons SET work_status_cg_ref_value = (SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'work_status') WHERE work_status_cg_ref_value IS NULL;

-- appointments
UPDATE appointments SET appointment_type_cg_ref_value = (SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'appointment_type') WHERE appointment_type_cg_ref_value IS NULL;
UPDATE appointments SET is_canceled_cg_ref_value = (SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'is_canceled') WHERE is_canceled_cg_ref_value IS NULL;

-- treatments
UPDATE treatments SET is_canceled_cg_ref_value = (SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'is_canceled') WHERE is_canceled_cg_ref_value IS NULL;

-- prescriptions
UPDATE prescriptions SET is_canceled_cg_ref_value = (SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'is_canceled') WHERE is_canceled_cg_ref_value IS NULL;
