package com.dentalclinic.dentalclinicapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing all staff members with their roles and professional
 * details
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@ToString(exclude = { "person", "supervisor", "clinic", "subordinates", "clinicAccesses", "requestedAppointments",
        "servedAppointments", "requestedTreatments", "servedTreatments", "requestedPrescriptions",
        "servedPrescriptions" })
@EqualsAndHashCode(exclude = { "person", "supervisor", "clinic", "subordinates", "clinicAccesses",
        "requestedAppointments", "servedAppointments", "requestedTreatments", "servedTreatments",
        "requestedPrescriptions", "servedPrescriptions" })
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prs_id", nullable = false)
    private Person person;

    @Column(name = "employee_number", unique = true, nullable = false, length = 50)
    private String employeeNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type", nullable = false, length = 50)
    private EmployeeType employeeType;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "position_title", length = 150)
    private String positionTitle;

    @Column(name = "license_number", unique = true, length = 100)
    private String licenseNumber;

    @Column(name = "specialization", length = 200)
    private String specialization;

    @Column(name = "qualification", length = 500)
    private String qualification;

    @Column(name = "experience_years")
    @Builder.Default
    private Integer experienceYears = 0;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", length = 30)
    @Builder.Default
    private EmploymentStatus employmentStatus = EmploymentStatus.ACTIVE;

    @Column(name = "salary", precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(name = "hourly_rate", precision = 8, scale = 2)
    private BigDecimal hourlyRate;

    @Column(name = "work_schedule", length = 200)
    private String workSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Employee supervisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @Column(name = "can_work_multiple_clinics")
    @Builder.Default
    private Boolean canWorkMultipleClinics = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "matrix_room_id", length = 255)
    private String matrixRoomId;

    @Column(name = "matrix_user_id", length = 255)
    private String matrixUserId;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relationships
    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Employee> subordinates;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EmployeeClinicAccess> clinicAccesses;

    @OneToMany(mappedBy = "requestByEmployee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> requestedAppointments;

    @OneToMany(mappedBy = "servedByEmployee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> servedAppointments;

    @OneToMany(mappedBy = "requestByEmployee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Treatment> requestedTreatments;

    @OneToMany(mappedBy = "servedByEmployee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Treatment> servedTreatments;

    @OneToMany(mappedBy = "requestByEmployee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Prescription> requestedPrescriptions;

    @OneToMany(mappedBy = "servedByEmployee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Prescription> servedPrescriptions;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum EmployeeType {
        DENTIST, NURSE, PHARMACIST, RECEPTIONIST, CLEANER, OFFICE_BOY,
        DATA_ENTRY, SECURITY, MANAGER, ASSISTANT, TECHNICIAN, ADMINISTRATOR,
        ACCOUNTANT, MARKETING, HR_STAFF, IT_SUPPORT, MAINTENANCE, DRIVER, OTHER
    }

    public enum EmploymentStatus {
        ACTIVE, INACTIVE, TERMINATED, SUSPENDED, ON_LEAVE
    }
}
