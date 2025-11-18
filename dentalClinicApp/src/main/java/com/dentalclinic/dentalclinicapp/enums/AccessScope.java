package com.dentalclinic.dentalclinicapp.enums;

public enum AccessScope {
    GLOBAL, // All cities, all clinics
    MULTI_CITY, // Multiple cities
    CITY_WIDE, // All clinics in a city
    CLINIC_SPECIFIC, // Single clinic only
    DEPARTMENT_SPECIFIC // Single department in clinic
}
