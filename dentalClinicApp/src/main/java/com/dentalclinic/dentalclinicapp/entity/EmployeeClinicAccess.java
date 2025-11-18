package com.dentalclinic.dentalclinicapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing employee clinic access (Many-to-Many for employees who
 * work at multiple clinics)
 */
@Entity
@Table(name = "employee_clinic")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(EmployeeClinicAccessId.class)
public class EmployeeClinicAccess {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", length = 50)
    @Builder.Default
    private AccessLevel accessLevel = AccessLevel.FULL;

    @Column(name = "start_date")
    @Builder.Default
    private LocalDate startDate = LocalDate.now();

    @Column(name = "end_date")
    private LocalDate endDate;

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

    public enum AccessLevel {
        FULL, LIMITED, READ_ONLY, EMERGENCY_ONLY
    }
}
