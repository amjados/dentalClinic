
-- Seed appointments
INSERT INTO appointments (patient_id, request_by_employee_id, served_by_employee_id, clinic_id, appointment_datetime,
                          estimated_duration_minutes, appointment_type_cg_ref_value, is_canceled_cg_ref_value)
VALUES
(1, 1, 1, 1, GETDATE(), 30, NULL, NULL);


-- Seed appointments_tr
INSERT INTO appointments_tr (appointments_id, language_code, reason, notes, cancellation_reason)
VALUES
(1, 'en', 'Routine dental checkup', 'No special notes', NULL);


-- Seed treatments
INSERT INTO treatments (patient_id, request_by_employee_id, served_by_employee_id, appointment_id, clinic_id,
                        treatment_code, treatment_name, description, treatment_category, treatment_start_date,
                        treatment_end_date, estimated_cost, actual_cost, anesthesia_used, is_canceled_cg_ref_value,
                        tooth_numbers, treatment_notes, follow_up_required, follow_up_date)
VALUES
(1, 1, 1, 1, 1, 'T001', 'Teeth Cleaning', 'Standard cleaning treatment', 'Cleaning',
 GETDATE(), GETDATE(), 200.00, 180.00, 'Local', NULL, '14,15', 'None', 0, NULL);


-- Seed prescriptions
INSERT INTO prescriptions (patient_id, request_by_employee_id, served_by_employee_id, treatment_id, clinic_id,
                           prescription_number, medication_name, medication_type, dosage, frequency, duration,
                           quantity_prescribed, instructions, is_canceled_cg_ref_value, prescribed_date)
VALUES
(1, 1, 1, 1, 1, 'RX001', 'Ibuprofen', 'Tablet', '200mg', 'Twice a day', '5 days', 10, 'Take after meals', NULL, GETDATE());


-- Seed api_usage_logs
INSERT INTO api_usage_logs (user_id, username, user_role, clinic_id, city_id, http_method, request_url, request_uri,
                             endpoint_pattern, response_status, processing_time_ms, client_ip, user_agent)
VALUES
(1, 'johndoe', 'ADMIN', 1, 1, 'GET', '/api/appointments', '/api/appointments', '/api/appointments', 200, 123, '192.168.1.1', 'Postman');


-- Seed api_usage_summaries
INSERT INTO api_usage_summaries (summary_date, endpoint_pattern, http_method, total_requests, successful_requests, failed_requests,
                                  avg_response_time_ms, min_response_time_ms, max_response_time_ms, unique_users)
VALUES
(GETDATE(), '/api/appointments', 'GET', 10, 9, 1, 150.0, 100, 200, 2);


-- Seed user_activity_summaries
INSERT INTO user_activity_summaries (user_id, activity_date, total_requests, successful_requests, failed_requests,
                                     avg_response_time_ms, total_session_time_minutes, unique_endpoints_accessed,
                                     first_activity_at, last_activity_at)
VALUES
(1, GETDATE(), 10, 9, 1, 145.5, 45, 3, GETDATE(), GETDATE());
