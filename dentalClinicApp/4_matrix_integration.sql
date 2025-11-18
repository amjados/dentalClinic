-- Matrix Chat Integration - Database Migration (SQL Server)
-- Add Matrix fields to support real-time chat functionality

USE dentalclinic;
GO

-- Add Matrix fields to patients table
IF NOT EXISTS (
    SELECT 1
    FROM sys.columns
    WHERE
        object_id = OBJECT_ID('patients')
        AND name = 'matrix_room_id'
) BEGIN
ALTER TABLE patients
ADD matrix_room_id NVARCHAR(255) NULL;

PRINT 'Added matrix_room_id to patients table';

END IF NOT EXISTS (
    SELECT 1
    FROM sys.columns
    WHERE
        object_id = OBJECT_ID('patients')
        AND name = 'matrix_user_id'
) BEGIN
ALTER TABLE patients
ADD matrix_user_id NVARCHAR(255) NULL;

PRINT 'Added matrix_user_id to patients table';

END

-- Add Matrix fields to employees table
IF NOT EXISTS (
    SELECT 1
    FROM sys.columns
    WHERE
        object_id = OBJECT_ID('employees')
        AND name = 'matrix_room_id'
) BEGIN
ALTER TABLE employees
ADD matrix_room_id NVARCHAR(255) NULL;

PRINT 'Added matrix_room_id to employees table';

END IF NOT EXISTS (
    SELECT 1
    FROM sys.columns
    WHERE
        object_id = OBJECT_ID('employees')
        AND name = 'matrix_user_id'
) BEGIN
ALTER TABLE employees
ADD matrix_user_id NVARCHAR(255) NULL;

PRINT 'Added matrix_user_id to employees table';

END

-- Add indexes for better query performance
IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE
        name = 'idx_patients_matrix_room'
        AND object_id = OBJECT_ID('patients')
) BEGIN CREATE
INDEX idx_patients_matrix_room ON patients (matrix_room_id);

PRINT 'Created index idx_patients_matrix_room';

END IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE
        name = 'idx_patients_matrix_user'
        AND object_id = OBJECT_ID('patients')
) BEGIN CREATE
INDEX idx_patients_matrix_user ON patients (matrix_user_id);

PRINT 'Created index idx_patients_matrix_user';

END IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE
        name = 'idx_employees_matrix_room'
        AND object_id = OBJECT_ID('employees')
) BEGIN CREATE
INDEX idx_employees_matrix_room ON employees (matrix_room_id);

PRINT 'Created index idx_employees_matrix_room';

END IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE
        name = 'idx_employees_matrix_user'
        AND object_id = OBJECT_ID('employees')
) BEGIN CREATE
INDEX idx_employees_matrix_user ON employees (matrix_user_id);

PRINT 'Created index idx_employees_matrix_user';

END

-- Verification queries
SELECT 'Matrix fields added successfully to patients table' AS status, COUNT(*) AS column_count
FROM sys.columns
WHERE
    object_id = OBJECT_ID('patients')
    AND name IN (
        'matrix_room_id',
        'matrix_user_id'
    );

SELECT 'Matrix fields added successfully to employees table' AS status, COUNT(*) AS column_count
FROM sys.columns
WHERE
    object_id = OBJECT_ID('employees')
    AND name IN (
        'matrix_room_id',
        'matrix_user_id'
    );

PRINT 'Matrix integration migration completed successfully!';
GO