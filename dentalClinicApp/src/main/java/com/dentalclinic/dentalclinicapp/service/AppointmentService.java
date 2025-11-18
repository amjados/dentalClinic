package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Appointment;
import com.dentalclinic.dentalclinicapp.entity.Appointment.AppointmentStatus;
import com.dentalclinic.dentalclinicapp.entity.Employee;
import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.repository.AppointmentRepository;
import com.dentalclinic.dentalclinicapp.repository.EmployeeRepository;
import com.dentalclinic.dentalclinicapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment saveAppointment(Appointment appointment) {
        // Validate that patient and dentist exist
        if (appointment.getPatient() != null && appointment.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(appointment.getPatient().getId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            appointment.setPatient(patient);
        }

        if (appointment.getServedByEmployee() != null && appointment.getServedByEmployee().getId() != null) {
            Employee employee = employeeRepository.findById(appointment.getServedByEmployee().getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            appointment.setServedByEmployee(employee);
        }

        // Check for conflicts
        if (hasConflictingAppointment(appointment)) {
            throw new RuntimeException("Appointment time conflicts with existing appointment");
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        appointment.setAppointmentDateTime(appointmentDetails.getAppointmentDateTime());
        appointment.setStatus(appointmentDetails.getStatus());
        appointment.setReason(appointmentDetails.getReason());
        appointment.setNotes(appointmentDetails.getNotes());
        appointment.setEstimatedDurationMinutes(appointmentDetails.getEstimatedDurationMinutes());

        // Check for conflicts if datetime changed
        if (hasConflictingAppointment(appointment)) {
            throw new RuntimeException("Appointment time conflicts with existing appointment");
        }

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointmentRepository.delete(appointment);
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByEmployee(Long employeeId) {
        return appointmentRepository.findByServedByEmployeeId(employeeId);
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }

    public List<Appointment> getAppointmentsByDate(LocalDateTime date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    public List<Appointment> getEmployeeAppointmentsInRange(Long employeeId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        return appointmentRepository.findByEmployeeAndDateTimeBetween(employeeId, startDateTime, endDateTime);
    }

    public List<Appointment> getPatientAppointmentsInRange(Long patientId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        return appointmentRepository.findByPatientAndDateTimeBetween(patientId, startDateTime, endDateTime);
    }

    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public boolean hasConflictingAppointment(Appointment appointment) {
        LocalDateTime startTime = appointment.getAppointmentDateTime();
        LocalDateTime endTime = startTime.plusMinutes(appointment.getEstimatedDurationMinutes());

        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                appointment.getServedByEmployee().getId(), startTime, endTime);

        // Remove the current appointment from conflicts if updating
        if (appointment.getId() != null) {
            conflicts.removeIf(a -> a.getId().equals(appointment.getId()));
        }

        return !conflicts.isEmpty();
    }

    public long getTotalAppointmentCount() {
        return appointmentRepository.count();
    }

    public long getAppointmentCountByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status).size();
    }
}
