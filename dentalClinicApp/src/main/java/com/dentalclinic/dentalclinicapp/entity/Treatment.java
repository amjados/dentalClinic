package com.dentalclinic.dentalclinicapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "treatments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Treatment {

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
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Column(name = "treatment_code", length = 50)
    private String treatmentCode;

    @Column(name = "treatment_name", nullable = false, length = 255)
    private String treatmentName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "treatment_category", length = 100)
    private String treatmentCategory;

    @Column(name = "treatment_date")
    private LocalDate treatmentDate;

    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;

    @Column(name = "actual_cost", precision = 10, scale = 2)
    private BigDecimal actualCost;

    @Column(name = "anesthesia_used", length = 200)
    private String anesthesiaUsed;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    @Builder.Default
    private TreatmentStatus status = TreatmentStatus.PLANNED;

    @Column(name = "tooth_numbers", length = 100)
    private String toothNumbers;

    @Column(name = "treatment_notes", columnDefinition = "TEXT")
    private String treatmentNotes;

    @Column(name = "follow_up_required")
    @Builder.Default
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

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

    public enum TreatmentStatus {
        PLANNED, IN_PROGRESS, COMPLETED, CANCELLED, POSTPONED
    }
}
