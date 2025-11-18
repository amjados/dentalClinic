package com.dentalclinic.dentalclinicapp.controller;

import com.dentalclinic.dentalclinicapp.entity.Clinic;
import com.dentalclinic.dentalclinicapp.service.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clinics")
@Tag(name = "Clinic Management", description = "APIs for managing clinics")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    @GetMapping
    @Operation(summary = "Get all clinics", description = "Retrieve a list of all clinics")
    public ResponseEntity<List<Clinic>> getAllClinics() {
        List<Clinic> clinics = clinicService.getAllClinics();
        return ResponseEntity.ok(clinics);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get clinic by ID", description = "Retrieve a clinic by its ID")
    public ResponseEntity<Clinic> getClinicById(@PathVariable Long id) {
        Optional<Clinic> clinic = clinicService.getClinicById(id);
        return clinic.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get clinic by name", description = "Retrieve a clinic by its name")
    public ResponseEntity<Clinic> getClinicByName(@PathVariable String name) {
        Optional<Clinic> clinic = clinicService.getClinicByName(name);
        return clinic.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new clinic", description = "Create a new clinic record")
    public ResponseEntity<Clinic> createClinic(@Valid @RequestBody Clinic clinic) {
        try {
            if (clinic.getName() != null && clinicService.existsByName(clinic.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            Clinic savedClinic = clinicService.saveClinic(clinic);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClinic);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update clinic", description = "Update an existing clinic record")
    public ResponseEntity<Clinic> updateClinic(@PathVariable Long id,
            @Valid @RequestBody Clinic clinicDetails) {
        try {
            Clinic updatedClinic = clinicService.updateClinic(id, clinicDetails);
            return ResponseEntity.ok(updatedClinic);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete clinic", description = "Delete a clinic record")
    public ResponseEntity<Void> deleteClinic(@PathVariable Long id) {
        try {
            clinicService.deleteClinic(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/city/{cityId}")
    @Operation(summary = "Get clinics by city", description = "Retrieve all clinics in a specific city")
    public ResponseEntity<List<Clinic>> getClinicsByCityId(@PathVariable Long cityId) {
        List<Clinic> clinics = clinicService.getClinicsByCityId(cityId);
        return ResponseEntity.ok(clinics);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active clinics", description = "Retrieve all active clinics")
    public ResponseEntity<List<Clinic>> getActiveClinics() {
        List<Clinic> clinics = clinicService.getActiveClinics();
        return ResponseEntity.ok(clinics);
    }

    @GetMapping("/active/city/{cityId}")
    @Operation(summary = "Get active clinics by city", description = "Retrieve all active clinics in a specific city")
    public ResponseEntity<List<Clinic>> getActiveClinicsByCityId(@PathVariable Long cityId) {
        List<Clinic> clinics = clinicService.getActiveClinicsByCityId(cityId);
        return ResponseEntity.ok(clinics);
    }

    @GetMapping("/active/city-name/{cityName}")
    @Operation(summary = "Get active clinics by city name", description = "Retrieve all active clinics in a city by city name")
    public ResponseEntity<List<Clinic>> getActiveClinicsByCityName(@PathVariable String cityName) {
        List<Clinic> clinics = clinicService.getActiveClinicsByCityName(cityName);
        return ResponseEntity.ok(clinics);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate clinic", description = "Activate a clinic")
    public ResponseEntity<Clinic> activateClinic(@PathVariable Long id) {
        try {
            Clinic clinic = clinicService.activateClinic(id);
            return ResponseEntity.ok(clinic);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate clinic", description = "Deactivate a clinic")
    public ResponseEntity<Clinic> deactivateClinic(@PathVariable Long id) {
        try {
            Clinic clinic = clinicService.deactivateClinic(id);
            return ResponseEntity.ok(clinic);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get total clinic count", description = "Get the total number of clinics")
    public ResponseEntity<Long> getTotalClinicCount() {
        long count = clinicService.getTotalClinicCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/active")
    @Operation(summary = "Get active clinic count", description = "Get the count of active clinics")
    public ResponseEntity<Long> getActiveClinicCount() {
        long count = clinicService.getActiveClinicCount();
        return ResponseEntity.ok(count);
    }
}
