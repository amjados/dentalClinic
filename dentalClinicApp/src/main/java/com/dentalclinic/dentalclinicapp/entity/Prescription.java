package com.dentalclinic.dentalclinicapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_by_employee_id", nullable = false)
    private Employee requestByEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "served_by_employee_id")
    private Employee servedByEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Column(name = "prescription_number", unique = true, nullable = false, length = 50)
    private String prescriptionNumber;

    @Column(name = "medication_name", nullable = false, length = 200)
    private String medicationName;

    @Column(name = "medication_type", length = 100)
    private String medicationType;

    @Column(name = "dosage", length = 100)
    private String dosage;

    @Column(name = "frequency", length = 100)
    private String frequency;

    @Column(name = "duration", length = 100)
    private String duration;

    @Column(name = "quantity_prescribed")
    private Integer quantityPrescribed;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    @Builder.Default
    private PrescriptionStatus status = PrescriptionStatus.PENDING;

    @Column(name = "prescribed_date", nullable = false)
    private LocalDate prescribedDate;

    @Column(name = "dispensed_date")
    private LocalDate dispensedDate;

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

    public enum PrescriptionStatus {
        PENDING, DISPENSED, PARTIALLY_DISPENSED, COMPLETED, CANCELLED, EXPIRED
    }
}
