
-- Seed cities
INSERT INTO cities (timezone) VALUES
('Africa/Cairo'), ('America/New_York');


-- Seed city_tr
INSERT INTO city_tr (city_id, language_code, name, state, country) VALUES
(1, 'en', 'Cairo', 'Cairo Governorate', 'Egypt'),
(2, 'en', 'New York', 'New York State', 'USA');


-- Seed clinics
INSERT INTO clinics (phone, email, city_id, operating_hours, license_number) VALUES
('01000000001', 'clinic1@demo.com', 1, '9AM-5PM', 'LIC001'),
('01000000002', 'clinic2@demo.com', 2, '10AM-6PM', 'LIC002');


-- Seed clinic_tr
INSERT INTO clinic_tr (clinic_id, language_code, name, address) VALUES
(1, 'en', 'Downtown Clinic', '123 Nile St'),
(2, 'en', 'Uptown Clinic', '456 Broadway Ave');


-- Seed persons
INSERT INTO persons (email, phone, mobile_phone, date_of_birth, gender_cg_ref_value,
                     nationality_cg_ref_value, country_cg_ref_value, city_cg_ref_value, postal_code, emergency_contact_csv)
VALUES
('john@doe.com', '0101001001', '0101001002', '1990-01-01', NULL, NULL, NULL, NULL, '11511', 'Jane Doe:0123456789'),
('mary@jane.com', '0102002001', '0102002002', '1985-05-15', NULL, NULL, NULL, NULL, '90210', 'Mark Jane:0987654321');


-- Seed person_tr
INSERT INTO person_tr (person_id, language_code, first_name, middle_name, last_name, address, emergency_contact_relationship, emergency_contact_name)
VALUES
(1, 'en', 'John', 'A.', 'Doe', 'Downtown', 'Wife', 'Jane Doe'),
(2, 'en', 'Mary', NULL, 'Jane', 'Uptown', 'Husband', 'Mark Jane');
