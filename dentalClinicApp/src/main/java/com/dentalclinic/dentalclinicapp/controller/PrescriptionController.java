package com.dentalclinic.dentalclinicapp.controller;

import com.dentalclinic.dentalclinicapp.entity.Prescription;
import com.dentalclinic.dentalclinicapp.entity.Prescription.PrescriptionStatus;
import com.dentalclinic.dentalclinicapp.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prescriptions")
@Tag(name = "Prescription Management", description = "APIs for managing prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping
    @Operation(summary = "Get all prescriptions", description = "Retrieve a list of all prescriptions")
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get prescription by ID", description = "Retrieve a prescription by its ID")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        Optional<Prescription> prescription = prescriptionService.getPrescriptionById(id);
        return prescription.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{prescriptionNumber}")
    @Operation(summary = "Get prescription by number", description = "Retrieve a prescription by its prescription number")
    public ResponseEntity<Prescription> getPrescriptionByNumber(@PathVariable String prescriptionNumber) {
        Optional<Prescription> prescription = prescriptionService.getPrescriptionByNumber(prescriptionNumber);
        return prescription.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new prescription", description = "Create a new prescription record")
    public ResponseEntity<Prescription> createPrescription(@Valid @RequestBody Prescription prescription) {
        try {
            Prescription savedPrescription = prescriptionService.savePrescription(prescription);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPrescription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update prescription", description = "Update an existing prescription record")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id,
            @Valid @RequestBody Prescription prescriptionDetails) {
        try {
            Prescription updatedPrescription = prescriptionService.updatePrescription(id, prescriptionDetails);
            return ResponseEntity.ok(updatedPrescription);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete prescription", description = "Delete a prescription record")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        try {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get prescriptions by patient", description = "Retrieve all prescriptions for a specific patient")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/prescribing-employee/{employeeId}")
    @Operation(summary = "Get prescriptions by prescribing employee", description = "Retrieve all prescriptions prescribed by a specific employee")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPrescribingEmployee(@PathVariable Long employeeId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPrescribingEmployeeId(employeeId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/dispensing-employee/{employeeId}")
    @Operation(summary = "Get prescriptions by dispensing employee", description = "Retrieve all prescriptions dispensed by a specific employee")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDispensingEmployee(@PathVariable Long employeeId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDispensingEmployeeId(employeeId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/treatment/{treatmentId}")
    @Operation(summary = "Get prescriptions by treatment", description = "Retrieve all prescriptions for a specific treatment")
    public ResponseEntity<List<Prescription>> getPrescriptionsByTreatment(@PathVariable Long treatmentId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByTreatmentId(treatmentId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/clinic/{clinicId}")
    @Operation(summary = "Get prescriptions by clinic", description = "Retrieve all prescriptions for a specific clinic")
    public ResponseEntity<List<Prescription>> getPrescriptionsByClinic(@PathVariable Long clinicId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByClinicId(clinicId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get prescriptions by status", description = "Retrieve all prescriptions with a specific status")
    public ResponseEntity<List<Prescription>> getPrescriptionsByStatus(@PathVariable PrescriptionStatus status) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByStatus(status);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/patient/{patientId}/status/{status}")
    @Operation(summary = "Get prescriptions by patient and status", description = "Retrieve all prescriptions for a patient with a specific status")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatientAndStatus(
            @PathVariable Long patientId, @PathVariable PrescriptionStatus status) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientIdAndStatus(patientId, status);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get prescriptions by date range", description = "Retrieve prescriptions within a date range")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(prescriptions);
    }

    // TODO: Temporarily disabled - service method is commented out due to missing expiryDate field in database
    // @GetMapping("/expired")
    // @Operation(summary = "Get expired prescriptions", description = "Retrieve all expired prescriptions")
    // public ResponseEntity<List<Prescription>> getExpiredPrescriptions() {
    //     List<Prescription> prescriptions = prescriptionService.getExpiredPrescriptions();
    //     return ResponseEntity.ok(prescriptions);
    // }

    @GetMapping("/patient/{patientId}/search")
    @Operation(summary = "Search patient prescriptions", description = "Search prescriptions by patient and medication name")
    public ResponseEntity<List<Prescription>> searchPatientPrescriptions(
            @PathVariable Long patientId, @RequestParam String medicationName) {
        List<Prescription> prescriptions = prescriptionService.searchPatientPrescriptions(patientId, medicationName);
        return ResponseEntity.ok(prescriptions);
    }

    // TODO: Temporarily disabled - service method is commented out due to missing refillsAllowed/refillsUsed fields in database
    // @GetMapping("/refills-available")
    // @Operation(summary = "Get prescriptions with refills available", description = "Retrieve prescriptions that have refills available")
    // public ResponseEntity<List<Prescription>> getPrescriptionsWithRefillsAvailable() {
    //     List<Prescription> prescriptions = prescriptionService.getPrescriptionsWithRefillsAvailable();
    //     return ResponseEntity.ok(prescriptions);
    // }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update prescription status", description = "Update the status of a prescription")
    public ResponseEntity<Prescription> updatePrescriptionStatus(
            @PathVariable Long id, @RequestParam PrescriptionStatus status) {
        try {
            Prescription updatedPrescription = prescriptionService.updatePrescriptionStatus(id, status);
            return ResponseEntity.ok(updatedPrescription);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/refill")
    @Operation(summary = "Process prescription refill", description = "Process a refill for a prescription")
    public ResponseEntity<Prescription> processRefill(@PathVariable Long id) {
        try {
            Prescription updatedPrescription = prescriptionService.processRefill(id);
            return ResponseEntity.ok(updatedPrescription);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get total prescription count", description = "Get the total number of prescriptions")
    public ResponseEntity<Long> getTotalPrescriptionCount() {
        long count = prescriptionService.getTotalPrescriptionCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Get prescription count by status", description = "Get the count of prescriptions by status")
    public ResponseEntity<Long> getCountByStatus(@PathVariable PrescriptionStatus status) {
        long count = prescriptionService.getCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/clinic/{clinicId}/date-range")
    @Operation(summary = "Get prescriptions by clinic and date range", description = "Retrieve prescriptions for a clinic within a date range")
    public ResponseEntity<List<Prescription>> getPrescriptionsByClinicAndDateRange(
            @PathVariable Long clinicId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByClinicAndDateRange(clinicId, startDate,
                endDate);
        return ResponseEntity.ok(prescriptions);
    }
}
