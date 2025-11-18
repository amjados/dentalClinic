-- Explicitly use master database
USE [master];
GO

-- Only creates database if it doesn't exist
IF NOT EXISTS (
    SELECT name
    FROM sys.databases
    WHERE
        name = 'dentalclinic_v2'
) BEGIN CREATE
DATABASE dentalclinic_v2
COLLATE SQL_Latin1_General_CP1_CI_AS;

PRINT 'Database [dentalclinic_v2] created successfully!';

END ELSE BEGIN PRINT 'Database [dentalclinic_v2] already exists.';

END

-- Switch to the new database
USE dentalclinic_v2;
GO