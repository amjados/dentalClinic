-- Explicitly use master database
USE [master];
GO

-- Only creates database if it doesn't exist
IF NOT EXISTS (
    SELECT name 
    FROM sys.databases 
    WHERE name = 'dentalclinic'
)
BEGIN
    CREATE DATABASE dentalclinic COLLATE SQL_Latin1_General_CP1_CI_AS;
    PRINT 'Database [dentalclinic] created successfully!';
END
ELSE
BEGIN
    PRINT 'Database [dentalclinic] already exists.';
END
GO