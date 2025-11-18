package com.dentalclinic.dentalclinicapp.controller;

import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patient Management", description = "APIs for managing patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve a list of all patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieve a patient by their ID")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        return patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new patient", description = "Create a new patient record")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        try {
            // Check email through Person relationship
            if (patient.getPerson() != null && patient.getPerson().getEmail() != null
                    && patientService.existsByEmail(patient.getPerson().getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            Patient savedPatient = patientService.savePatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient", description = "Update an existing patient record")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
            @Valid @RequestBody Patient patientDetails) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patientDetails);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient", description = "Delete a patient record")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search patients", description = "Search patients by name, email, or phone")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String searchTerm) {
        List<Patient> patients = patientService.searchPatients(searchTerm);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/search/paginated")
    @Operation(summary = "Search patients with pagination", description = "Search patients with pagination support")
    public ResponseEntity<Page<Patient>> searchPatientsWithPagination(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patients = patientService.searchPatientsWithPagination(searchTerm, pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Find patient by email", description = "Find a patient by their email address")
    public ResponseEntity<Patient> getPatientByEmail(@PathVariable String email) {
        Optional<Patient> patient = patientService.findByEmail(email);
        return patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phone/{phone}")
    @Operation(summary = "Find patients by phone", description = "Find patients by their phone number")
    public ResponseEntity<List<Patient>> getPatientsByPhone(@PathVariable String phone) {
        List<Patient> patients = patientService.findByPhone(phone);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/count")
    @Operation(summary = "Get total patient count", description = "Get the total number of patients")
    public ResponseEntity<Long> getTotalPatientCount() {
        long count = patientService.getTotalPatientCount();
        return ResponseEntity.ok(count);
    }
}
