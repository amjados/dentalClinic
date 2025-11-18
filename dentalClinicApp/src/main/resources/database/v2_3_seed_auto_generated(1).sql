-- Auto-generated seed script for *_cg_ref_value fields
USE dentalclinic_v2;
GO

-- Populate gender_cg_ref_value in persons
UPDATE persons
SET gender_cg_ref_value = (
    SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'gender'
)
WHERE gender_cg_ref_value IS NULL;

-- Populate nationality_cg_ref_value in persons
UPDATE persons
SET nationality_cg_ref_value = (
    SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'nationality'
)
WHERE nationality_cg_ref_value IS NULL;

-- Populate country_cg_ref_value in persons
UPDATE persons
SET country_cg_ref_value = (
    SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'country'
)
WHERE country_cg_ref_value IS NULL;

-- Populate city_cg_ref_value in persons
UPDATE persons
SET city_cg_ref_value = (
    SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'city'
)
WHERE city_cg_ref_value IS NULL;

-- Populate work_status_cg_ref_value in persons
UPDATE persons
SET work_status_cg_ref_value = (
    SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'work_status'
)
WHERE work_status_cg_ref_value IS NULL;

-- Populate appointment_type_cg_ref_value in appointments
UPDATE appointments
SET appointment_type_cg_ref_value = (
    SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'appointment_type'
)
WHERE appointment_type_cg_ref_value IS NULL;

-- Populate is_canceled_cg_ref_value in appointments
UPDATE appointments
SET is_canceled_cg_ref_value = (
    SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'is_canceled'
)
WHERE is_canceled_cg_ref_value IS NULL;

-- Populate is_canceled_cg_ref_value in treatments
UPDATE treatments
SET is_canceled_cg_ref_value = (
    SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'is_canceled'
)
WHERE is_canceled_cg_ref_value IS NULL;

-- Populate is_canceled_cg_ref_value in prescriptions
UPDATE prescriptions
SET is_canceled_cg_ref_value = (
    SELECT TOP 1 code_value FROM cg_ref_code WHERE sub_domain = 'is_canceled'
)
WHERE is_canceled_cg_ref_value IS NULL;
