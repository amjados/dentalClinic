package com.dentalclinic.dentalclinicapp.enums;

public enum UserRole {
    // Global roles
    SUPER_ADMIN,        // Access all cities, all clinics
    CHAIN_MANAGER,      // Access multiple cities/clinics
    
    // City-level roles  
    CITY_ADMIN,         // Admin for all clinics in a city
    CITY_MANAGER,       // Manager for all clinics in a city
    
    // Clinic-level roles
    CLINIC_ADMIN,       // Admin for specific clinic
    CLINIC_MANAGER,     // Manager for specific clinic
    
    // Staff roles (clinic-specific)
    DENTIST,           // Dentist at specific clinic
    PHARMACIST,        // Pharmacist at specific clinic  
    RECEPTIONIST,      // Receptionist at specific clinic
    NURSE,             // Nurse at specific clinic
    
    // Patient/Public (can visit any clinic)
    PATIENT,           // Patient (can book at any clinic)
    PUBLIC             // Public access (view all clinics)
}
