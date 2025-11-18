package com.dentalclinic.dentalclinicapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Entity
@Table(name = "clinics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "city", "employeeAccesses", "appointments", "patients" })
@EqualsAndHashCode(exclude = { "city", "employeeAccesses", "appointments", "patients" })
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "license_number", length = 50)
    private String licenseNumber;

    @Column(name = "operating_hours", length = 255)
    private String operatingHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "timezone_id", length = 50)
    @Builder.Default
    private String timezoneId = "UTC";

    @Column(name = "active")
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Note: User access to clinics is now managed through UserClaim entities with
    // JSON-based permissions
    // Employee access is still managed through the employee_clinic table
    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EmployeeClinicAccess> employeeAccesses;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "primaryClinic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Patient> patients;

    // Helper methods
    public ZoneId getTimezone() {
        return ZoneId.of(timezoneId);
    }

    public void setTimezone(ZoneId timezone) {
        this.timezoneId = timezone.getId();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
