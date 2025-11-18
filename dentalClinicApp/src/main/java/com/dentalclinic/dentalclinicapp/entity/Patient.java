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
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prs_id", nullable = false)
    private Person person;

    @Column(name = "patient_number", unique = true, nullable = false, length = 50)
    private String patientNumber;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(name = "current_medications", columnDefinition = "TEXT")
    private String currentMedications;

    @Column(name = "allergies", length = 500)
    private String allergies;

    @Convert(converter = com.dentalclinic.dentalclinicapp.converter.BloodTypeConverter.class)
    @Column(name = "blood_type", length = 5)
    private BloodType bloodType;

    @Column(name = "insurance_provider", length = 200)
    private String insuranceProvider;

    @Column(name = "insurance_policy_number", length = 100)
    private String insurancePolicyNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_clinic_id")
    private Clinic primaryClinic;

    @Enumerated(EnumType.STRING)
    @Column(name = "patient_status", length = 30)
    @Builder.Default
    private PatientStatus patientStatus = PatientStatus.ACTIVE;

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
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Treatment> treatments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Prescription> prescriptions;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", patientNumber='" + patientNumber + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", currentMedications='" + currentMedications + '\'' +
                ", allergies='" + allergies + '\'' +
                ", bloodType=" + bloodType +
                ", insuranceProvider='" + insuranceProvider + '\'' +
                ", insurancePolicyNumber='" + insurancePolicyNumber + '\'' +
                ", patientStatus=" + patientStatus +
                ", notes='" + notes + '\'' +
                ", matrixRoomId='" + matrixRoomId + '\'' +
                ", matrixUserId='" + matrixUserId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public enum BloodType {
        A_POSITIVE("A+"), A_NEGATIVE("A-"),
        B_POSITIVE("B+"), B_NEGATIVE("B-"),
        AB_POSITIVE("AB+"), AB_NEGATIVE("AB-"),
        O_POSITIVE("O+"), O_NEGATIVE("O-");

        private final String value;

        BloodType(String value) {
            this.value = value;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public String getValue() {
            return value;
        }

        @com.fasterxml.jackson.annotation.JsonCreator
        public static BloodType fromValue(String value) {
            for (BloodType bloodType : BloodType.values()) {
                if (bloodType.value.equals(value)) {
                    return bloodType;
                }
            }
            throw new IllegalArgumentException("Unknown blood type: " + value);
        }
    }

    public enum PatientStatus {
        ACTIVE, INACTIVE, TRANSFERRED, DECEASED
    }
}
