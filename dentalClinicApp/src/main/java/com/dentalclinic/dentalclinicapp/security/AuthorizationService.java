package com.dentalclinic.dentalclinicapp.security;

import com.dentalclinic.dentalclinicapp.enums.AccessLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationService {

    // Simple authorization service for location-based access control
    // This is a basic implementation - can be expanded later

    public boolean hasClinicAccess(Long userId, String userRole, List<Long> clinicIds, Long clinicId,
            AccessLevel requiredLevel) {
        if (userId == null || clinicId == null) {
            return false;
        }

        // Super admin has access to everything
        if (isSuperAdmin(userRole)) {
            return true;
        }

        // System admin has access to all clinics
        if (isSystemAdmin(userRole)) {
            return true;
        }

        // Check if user has access to this clinic
        if (clinicIds == null || !clinicIds.contains(clinicId)) {
            return false;
        }

        return true;
    }

    public boolean hasCityAccess(Long userId, String userRole, List<Long> cityIds, Long cityId,
            AccessLevel requiredLevel) {
        if (userId == null || cityId == null) {
            return false;
        }

        // Super admin has access to everything
        if (isSuperAdmin(userRole)) {
            return true;
        }

        // System admin has access to all cities
        if (isSystemAdmin(userRole)) {
            return true;
        }

        // Check if user has access to this city
        if (cityIds == null || !cityIds.contains(cityId)) {
            return false;
        }

        return true;
    }

    // Role checking helper methods
    private boolean isSuperAdmin(String role) {
        return "SUPER_ADMIN".equals(role);
    }

    private boolean isSystemAdmin(String role) {
        return "SYSTEM_ADMIN".equals(role);
    }

    private boolean isAdminRole(String role) {
        return "ADMIN".equals(role) || "SYSTEM_ADMIN".equals(role) || "SUPER_ADMIN".equals(role);
    }

    private boolean isManagerRole(String role) {
        return "MANAGER".equals(role);
    }

    private boolean isHealthcareRole(String role) {
        return "DENTIST".equals(role) || "NURSE".equals(role) || "HYGIENIST".equals(role);
    }

    // Check if access level is sufficient
    public boolean isAccessLevelSufficient(AccessLevel userLevel, AccessLevel requiredLevel) {
        if (userLevel == null || requiredLevel == null) {
            return false;
        }

        return userLevel.isAtLeast(requiredLevel);
    }
}
