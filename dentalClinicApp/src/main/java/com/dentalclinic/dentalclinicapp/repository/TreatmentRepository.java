package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.Treatment;
import com.dentalclinic.dentalclinicapp.entity.Treatment.TreatmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

        List<Treatment> findByPatientId(Long patientId);

        List<Treatment> findByRequestByEmployeeId(Long requestByEmployeeId);

        List<Treatment> findByServedByEmployeeId(Long servedByEmployeeId);

        List<Treatment> findByStatus(TreatmentStatus status);

        List<Treatment> findByTreatmentDate(LocalDate treatmentDate);

        @Query("SELECT t FROM Treatment t WHERE t.followUpRequired = true AND " +
                        "t.followUpDate <= :date AND t.status = 'COMPLETED'")
        List<Treatment> findTreatmentsRequiringFollowUp(@Param("date") LocalDate date);

        @Query("SELECT t FROM Treatment t WHERE t.patient.id = :patientId AND " +
                        "t.treatmentDate BETWEEN :startDate AND :endDate")
        List<Treatment> findByPatientAndDateBetween(
                        @Param("patientId") Long patientId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT t FROM Treatment t WHERE " +
                        "LOWER(t.treatmentName) LIKE LOWER(CONCAT('%', :treatmentName, '%'))")
        List<Treatment> findByTreatmentNameContaining(@Param("treatmentName") String treatmentName);

        @Query("SELECT SUM(t.actualCost) FROM Treatment t WHERE t.patient.id = :patientId AND " +
                        "t.status = 'COMPLETED'")
        java.math.BigDecimal getTotalCostByPatient(@Param("patientId") Long patientId);
}
