package com.dentalclinic.dentalclinicapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_claims")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "claim_name", nullable = false, length = 100)
    private String claimName; // References cg_ref_code.code_value where code_name = 'user_claims'

    @Column(name = "claim_json_details", columnDefinition = "TEXT")
    private String claimJsonDetails; // JSON string containing detailed permissions and constraints

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", length = 50)
    @Builder.Default
    private User.AccessScope scope = User.AccessScope.CLINIC_SPECIFIC;

    @Column(name = "granted_date")
    @Builder.Default
    private LocalDate grantedDate = LocalDate.now();

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "granted_by")
    private User grantedBy;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AccessScope {
        SYSTEM_WIDE, // Access across entire system
        CITY_WIDE, // Access within specific cities
        CLINIC_SPECIFIC, // Access within specific clinics
        DEPARTMENT_SPECIFIC, // Access within specific departments
        PERSONAL // Access to own data only
    }
}
