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
