package com.dentalclinic.dentalclinicapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prs_id")
    private Person person;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password; // BCrypt encoded

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_scope", length = 50)
    @Builder.Default
    private AccessScope accessScope = AccessScope.CLINIC_SPECIFIC;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Role-based access control relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserClaim> userClaims;

    // Roles granted by this user
    @OneToMany(mappedBy = "grantedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserRole> grantedRoles;

    // Claims granted by this user
    @OneToMany(mappedBy = "grantedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserClaim> grantedClaims;

    // Activity tracking - REMOVED to prevent conflicts with new multi-module
    // structure
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    // private List<UserActivitySummary> activitySummaries;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper method to get email from person
    public String getEmail() {
        return person != null ? person.getEmail() : null;
    }

    public enum Role {
        ADMIN, SUPER_ADMIN, REGIONAL_MANAGER, CLINIC_ADMIN, OFFICE_MANAGER,
        DOCTOR, DENTIST, NURSE, PHARMACIST, RECEPTION, RECEPTIONIST, TECHNICIAN, PATIENT,
        BILLING_SPECIALIST, DATA_ENTRY, SECURITY, CLEANER, OTHER
    }

    public enum AccessScope {
        GLOBAL, SYSTEM_WIDE, CITY_WIDE, CLINIC, CLINIC_SPECIFIC
    }
}
