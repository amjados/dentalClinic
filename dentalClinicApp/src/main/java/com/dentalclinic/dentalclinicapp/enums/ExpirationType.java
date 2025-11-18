package com.dentalclinic.dentalclinicapp.enums;

public enum ExpirationType {
    NEVER_EXPIRES, // Permanent access (regular staff)
    FIXED_DATE, // Expires on specific date/time
    DURATION_BASED // Expires after X days/hours from grant
}
