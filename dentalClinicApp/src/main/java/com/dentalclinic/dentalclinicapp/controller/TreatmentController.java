package com.dentalclinic.dentalclinicapp.controller;

import com.dentalclinic.dentalclinicapp.entity.Treatment;
import com.dentalclinic.dentalclinicapp.entity.Treatment.TreatmentStatus;
import com.dentalclinic.dentalclinicapp.service.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/treatments")
@Tag(name = "Treatment Management", description = "APIs for managing treatments")
public class TreatmentController {

    @Autowired
    private TreatmentService treatmentService;

    @GetMapping
    @Operation(summary = "Get all treatments", description = "Retrieve a list of all treatments")
    public ResponseEntity<List<Treatment>> getAllTreatments() {
        List<Treatment> treatments = treatmentService.getAllTreatments();
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get treatment by ID", description = "Retrieve a treatment by its ID")
    public ResponseEntity<Treatment> getTreatmentById(@PathVariable Long id) {
        Optional<Treatment> treatment = treatmentService.getTreatmentById(id);
        return treatment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new treatment", description = "Create a new treatment record")
    public ResponseEntity<Treatment> createTreatment(@Valid @RequestBody Treatment treatment) {
        try {
            Treatment savedTreatment = treatmentService.saveTreatment(treatment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTreatment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update treatment", description = "Update an existing treatment record")
    public ResponseEntity<Treatment> updateTreatment(@PathVariable Long id,
            @Valid @RequestBody Treatment treatmentDetails) {
        try {
            Treatment updatedTreatment = treatmentService.updateTreatment(id, treatmentDetails);
            return ResponseEntity.ok(updatedTreatment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete treatment", description = "Delete a treatment record")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long id) {
        try {
            treatmentService.deleteTreatment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get treatments by patient", description = "Get all treatments for a specific patient")
    public ResponseEntity<List<Treatment>> getTreatmentsByPatient(@PathVariable Long patientId) {
        List<Treatment> treatments = treatmentService.getTreatmentsByPatient(patientId);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get treatments by employee", description = "Get all treatments performed by a specific employee")
    public ResponseEntity<List<Treatment>> getTreatmentsByEmployee(@PathVariable Long employeeId) {
        List<Treatment> treatments = treatmentService.getTreatmentsByEmployee(employeeId);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get treatments by status", description = "Get all treatments with a specific status")
    public ResponseEntity<List<Treatment>> getTreatmentsByStatus(@PathVariable TreatmentStatus status) {
        List<Treatment> treatments = treatmentService.getTreatmentsByStatus(status);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get treatments by date", description = "Get all treatments for a specific date")
    public ResponseEntity<List<Treatment>> getTreatmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Treatment> treatments = treatmentService.getTreatmentsByDate(date);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/follow-up")
    @Operation(summary = "Get treatments requiring follow-up", description = "Get treatments that require follow-up")
    public ResponseEntity<List<Treatment>> getTreatmentsRequiringFollowUp(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate) {
        LocalDate checkDate = beforeDate != null ? beforeDate : LocalDate.now();
        List<Treatment> treatments = treatmentService.getTreatmentsRequiringFollowUp(checkDate);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/patient/{patientId}/range")
    @Operation(summary = "Get patient treatments in range", description = "Get patient treatments within a date range")
    public ResponseEntity<List<Treatment>> getPatientTreatmentsInRange(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Treatment> treatments = treatmentService.getPatientTreatmentsInRange(patientId, startDate, endDate);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/search")
    @Operation(summary = "Search treatments by name", description = "Search treatments by treatment name")
    public ResponseEntity<List<Treatment>> searchTreatmentsByName(@RequestParam String treatmentName) {
        List<Treatment> treatments = treatmentService.searchTreatmentsByName(treatmentName);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/patient/{patientId}/total-cost")
    @Operation(summary = "Get total cost by patient", description = "Get total treatment cost for a specific patient")
    public ResponseEntity<BigDecimal> getTotalCostByPatient(@PathVariable Long patientId) {
        BigDecimal totalCost = treatmentService.getTotalCostByPatient(patientId);
        return ResponseEntity.ok(totalCost);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update treatment status", description = "Update only the status of a treatment")
    public ResponseEntity<Treatment> updateTreatmentStatus(@PathVariable Long id,
            @RequestParam TreatmentStatus status) {
        try {
            Treatment updatedTreatment = treatmentService.updateTreatmentStatus(id, status);
            return ResponseEntity.ok(updatedTreatment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get total treatment count", description = "Get the total number of treatments")
    public ResponseEntity<Long> getTotalTreatmentCount() {
        long count = treatmentService.getTotalTreatmentCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Get treatment count by status", description = "Get the count of treatments by status")
    public ResponseEntity<Long> getTreatmentCountByStatus(@PathVariable TreatmentStatus status) {
        long count = treatmentService.getTreatmentCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/upcoming-follow-ups")
    @Operation(summary = "Get upcoming follow-ups", description = "Get treatments with upcoming follow-ups (within 7 days)")
    public ResponseEntity<List<Treatment>> getUpcomingFollowUps() {
        List<Treatment> treatments = treatmentService.getUpcomingFollowUps();
        return ResponseEntity.ok(treatments);
    }
}
