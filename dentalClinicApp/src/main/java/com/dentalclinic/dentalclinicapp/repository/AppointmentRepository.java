package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.Appointment;
import com.dentalclinic.dentalclinicapp.entity.Appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

        List<Appointment> findByPatientId(Long patientId);

        List<Appointment> findByRequestByEmployeeId(Long requestByEmployeeId);

        List<Appointment> findByServedByEmployeeId(Long servedByEmployeeId);

        List<Appointment> findByStatus(AppointmentStatus status);

        @Query("SELECT a FROM Appointment a WHERE a.servedByEmployee.id = :employeeId AND " +
                        "a.appointmentDateTime BETWEEN :startDateTime AND :endDateTime")
        List<Appointment> findByEmployeeAndDateTimeBetween(
                        @Param("employeeId") Long employeeId,
                        @Param("startDateTime") LocalDateTime startDateTime,
                        @Param("endDateTime") LocalDateTime endDateTime);

        @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND " +
                        "a.appointmentDateTime BETWEEN :startDateTime AND :endDateTime")
        List<Appointment> findByPatientAndDateTimeBetween(
                        @Param("patientId") Long patientId,
                        @Param("startDateTime") LocalDateTime startDateTime,
                        @Param("endDateTime") LocalDateTime endDateTime);

        @Query("SELECT a FROM Appointment a WHERE " +
                        "CAST(a.appointmentDateTime AS date) = CAST(:date AS date)")
        List<Appointment> findByAppointmentDate(@Param("date") LocalDateTime date);

        @Query("SELECT a FROM Appointment a WHERE a.servedByEmployee.id = :employeeId AND " +
                        "a.appointmentDateTime >= :startTime AND a.appointmentDateTime < :endTime AND " +
                        "a.status NOT IN ('CANCELLED', 'NO_SHOW')")
        List<Appointment> findConflictingAppointments(
                        @Param("employeeId") Long employeeId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);
}
