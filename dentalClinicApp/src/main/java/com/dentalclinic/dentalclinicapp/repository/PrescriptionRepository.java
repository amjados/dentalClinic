package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.Prescription;
import com.dentalclinic.dentalclinicapp.entity.Prescription.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Optional<Prescription> findByPrescriptionNumber(String prescriptionNumber);

    List<Prescription> findByPatientId(Long patientId);

    List<Prescription> findByRequestByEmployeeId(Long employeeId);

    List<Prescription> findByServedByEmployeeId(Long employeeId);

    List<Prescription> findByTreatmentId(Long treatmentId);

    List<Prescription> findByClinicId(Long clinicId);

    List<Prescription> findByStatus(PrescriptionStatus status);

    @Query("SELECT p FROM Prescription p WHERE p.patient.id = :patientId AND p.status = :status")
    List<Prescription> findByPatientIdAndStatus(@Param("patientId") Long patientId,
            @Param("status") PrescriptionStatus status);

    @Query("SELECT p FROM Prescription p WHERE p.prescribedDate BETWEEN :startDate AND :endDate")
    List<Prescription> findByPrescribedDateBetween(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // TODO: expiryDate field doesn't exist in Prescription entity - needs to be
    // added to database
    // @Query("SELECT p FROM Prescription p WHERE p.expiryDate < :date AND p.status
    // != 'EXPIRED' AND p.status != 'COMPLETED' AND p.status != 'CANCELLED'")
    // List<Prescription> findExpiredPrescriptions(@Param("date") LocalDate date);

    @Query("SELECT p FROM Prescription p WHERE p.patient.id = :patientId AND p.medicationName LIKE %:medicationName%")
    List<Prescription> findByPatientIdAndMedicationName(@Param("patientId") Long patientId,
            @Param("medicationName") String medicationName);

    // TODO: refillsAllowed and refillsUsed fields don't exist in Prescription
    // entity - needs to be added to database
    // @Query("SELECT p FROM Prescription p WHERE p.refillsAllowed > p.refillsUsed
    // AND p.status = 'DISPENSED'")
    // List<Prescription> findPrescriptionsWithRefillsAvailable();

    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.status = :status")
    long countByStatus(@Param("status") PrescriptionStatus status);

    @Query("SELECT p FROM Prescription p WHERE p.clinic.id = :clinicId AND p.prescribedDate BETWEEN :startDate AND :endDate")
    List<Prescription> findByClinicIdAndDateRange(@Param("clinicId") Long clinicId,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
