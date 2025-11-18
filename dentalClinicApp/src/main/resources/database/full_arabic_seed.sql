USE dentalclinic_v2;
GO

-- Arabic language (if not already seeded)
IF NOT EXISTS (SELECT 1 FROM languages WHERE code = 'ar')
BEGIN
    INSERT INTO languages (code, name) VALUES ('ar', N'العربية');
END


-- Arabic: cg_ref_code_tr
INSERT INTO cg_ref_code_tr (cg_ref_code_id, language_code, code_name, code_display_value, code_desc)
SELECT id, 'ar',
    CASE sub_domain
        WHEN 'gender' THEN 'الجنس'
        WHEN 'nationality' THEN 'الجنسية'
        WHEN 'country' THEN 'الدولة'
        WHEN 'city' THEN 'المدينة'
        WHEN 'work_status' THEN 'حالة العمل'
        WHEN 'appointment_type' THEN 'نوع الموعد'
        WHEN 'is_canceled' THEN 'تم الإلغاء'
        ELSE sub_domain
    END,
    CASE code_value
        WHEN 'M' THEN 'ذكر'
        WHEN 'F' THEN 'أنثى'
        WHEN 'EG' THEN 'مصر'
        WHEN 'US' THEN 'الولايات المتحدة'
        WHEN 'CAI' THEN 'القاهرة'
        WHEN 'NYC' THEN 'نيويورك'
        WHEN 'EMP' THEN 'موظف'
        WHEN 'UNEMP' THEN 'عاطل'
        WHEN 'CHK' THEN 'فحص'
        WHEN 'SURG' THEN 'عملية'
        WHEN 'YES' THEN 'نعم'
        WHEN 'NO' THEN 'لا'
        ELSE code_value
    END,
    'وصف تلقائي'
FROM cg_ref_code;



-- Arabic: city_tr
INSERT INTO city_tr (city_id, language_code, name, state, country) VALUES
(1, 'ar', 'القاهرة', 'محافظة القاهرة', 'مصر'),
(2, 'ar', 'نيويورك', 'ولاية نيويورك', 'الولايات المتحدة');


-- Arabic: clinic_tr
INSERT INTO clinic_tr (clinic_id, language_code, name, address) VALUES
(1, 'ar', 'عيادة وسط البلد', 'شارع النيل 123'),
(2, 'ar', 'عيادة المدينة العليا', 'شارع برودواي 456');


-- Arabic: person_tr
INSERT INTO person_tr (person_id, language_code, first_name, middle_name, last_name, address, emergency_contact_relationship, emergency_contact_name)
VALUES
(1, 'ar', 'جون', 'أ.', 'دو', 'وسط البلد', 'زوجة', 'جين دو'),
(2, 'ar', 'ماري', NULL, 'جين', 'المدينة العليا', 'زوج', 'مارك جين');


-- Arabic: employee_tr
INSERT INTO employee_tr (employee_id, language_code, department, position_title, specialization, qualification, notes)
VALUES
(1, 'ar', 'الأسنان', 'طبيب أسنان أول', 'تقويم الأسنان', 'دكتوراه في طب الأسنان', 'خبير في التركيبات والتقويم');


-- Arabic: patient_tr
INSERT INTO patient_tr (patient_id, language_code, medical_history, current_medications, allergies, notes)
VALUES
(1, 'ar', 'لا توجد أمراض مزمنة', 'ايبوبروفين', 'لا شيء', 'زيارة منتظمة');


-- Arabic: appointments_tr
INSERT INTO appointments_tr (appointments_id, language_code, reason, notes, cancellation_reason)
VALUES
(1, 'ar', 'فحص دوري للأسنان', 'لا توجد ملاحظات خاصة', NULL);
