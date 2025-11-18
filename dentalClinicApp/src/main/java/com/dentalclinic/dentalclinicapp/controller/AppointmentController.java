package com.dentalclinic.dentalclinicapp.controller;

import com.dentalclinic.dentalclinicapp.entity.Appointment;
import com.dentalclinic.dentalclinicapp.entity.Appointment.AppointmentStatus;
import com.dentalclinic.dentalclinicapp.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment Management", description = "APIs for managing appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    @Operation(summary = "Get all appointments", description = "Retrieve a list of all appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieve an appointment by its ID")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new appointment", description = "Create a new appointment")
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody Appointment appointment) {
        try {
            Appointment savedAppointment = appointmentService.saveAppointment(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update appointment", description = "Update an existing appointment")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id,
            @Valid @RequestBody Appointment appointmentDetails) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(id, appointmentDetails);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete appointment", description = "Delete an appointment")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get appointments by patient", description = "Get all appointments for a specific patient")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get appointments by employee", description = "Get all appointments for a specific employee")
    public ResponseEntity<List<Appointment>> getAppointmentsByEmployee(@PathVariable Long employeeId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByEmployee(employeeId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get appointments by status", description = "Get all appointments with a specific status")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        List<Appointment> appointments = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/date")
    @Operation(summary = "Get appointments by date", description = "Get all appointments for a specific date")
    public ResponseEntity<List<Appointment>> getAppointmentsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDate(date);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/employee/{employeeId}/range")
    @Operation(summary = "Get employee appointments in range", description = "Get employee appointments within a date range")
    public ResponseEntity<List<Appointment>> getEmployeeAppointmentsInRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        List<Appointment> appointments = appointmentService.getEmployeeAppointmentsInRange(
                employeeId, startDateTime, endDateTime);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}/range")
    @Operation(summary = "Get patient appointments in range", description = "Get patient appointments within a date range")
    public ResponseEntity<List<Appointment>> getPatientAppointmentsInRange(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        List<Appointment> appointments = appointmentService.getPatientAppointmentsInRange(
                patientId, startDateTime, endDateTime);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update appointment status", description = "Update only the status of an appointment")
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable Long id,
            @RequestParam AppointmentStatus status) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get total appointment count", description = "Get the total number of appointments")
    public ResponseEntity<Long> getTotalAppointmentCount() {
        long count = appointmentService.getTotalAppointmentCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Get appointment count by status", description = "Get the count of appointments by status")
    public ResponseEntity<Long> getAppointmentCountByStatus(@PathVariable AppointmentStatus status) {
        long count = appointmentService.getAppointmentCountByStatus(status);
        return ResponseEntity.ok(count);
    }
}
