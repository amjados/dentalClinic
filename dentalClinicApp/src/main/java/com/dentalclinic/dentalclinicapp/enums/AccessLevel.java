package com.dentalclinic.dentalclinicapp.enums;

public enum AccessLevel {
    // Full operational access
    FULL_ACCESS(100), // Complete CRUD operations at this location

    // Specialized access levels
    ADMINISTRATIVE_ONLY(80), // Admin functions only (reports, user management)
    CLINICAL_ONLY(70), // Patient care only (no admin functions)

    // Restricted access
    LIMITED_ACCESS(50), // Restricted to specific departments/functions
    EMERGENCY_ACCESS(40), // Limited time access during emergencies
    SUPERVISED_ACCESS(30), // Requires supervision/approval
    TRAINING_ACCESS(20), // Learning mode with limited real operations

    // Read-only access
    READ_ONLY(10), // View data only, no modifications
    AUDIT_ACCESS(5); // Read-only access for auditing purposes

    private final int level;

    AccessLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAtLeast(AccessLevel other) {
        return this.level >= other.level;
    }
}
