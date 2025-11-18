USE dentalclinic_v2;
GO

-- Seed languages
INSERT INTO languages (code, name) VALUES
('en', 'English'),
('ar', 'Arabic');


-- Seed cg_ref_code (reference data)
INSERT INTO cg_ref_code (code_id, code_lng, main_domain, sub_domain, code_value)
VALUES 
('GEN-M', 'en', 'personal', 'gender', 'M'),
('GEN-F', 'en', 'personal', 'gender', 'F'),
('CNT-EG', 'en', 'location', 'country', 'EG'),
('CNT-US', 'en', 'location', 'country', 'US'),
('CITY-C', 'en', 'location', 'city', 'CAI'),
('CITY-N', 'en', 'location', 'city', 'NYC'),
('NAT-EG', 'en', 'nationality', 'nationality', 'EG'),
('WORK-E', 'en', 'employment', 'work_status', 'EMP'),
('WORK-U', 'en', 'employment', 'work_status', 'UNEMP'),
('CANCEL-Y', 'en', 'status', 'is_canceled', 'YES'),
('CANCEL-N', 'en', 'status', 'is_canceled', 'NO'),
('APT-CHK', 'en', 'appointment', 'appointment_type', 'CHK');


-- Seed cg_ref_code_tr
INSERT INTO cg_ref_code_tr (cg_ref_code_id, language_code, code_name, code_display_value, code_desc)
SELECT id, 'en', sub_domain, code_value, 'Default desc'
FROM cg_ref_code;
