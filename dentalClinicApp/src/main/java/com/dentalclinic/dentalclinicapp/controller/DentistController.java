package com.dentalclinic.dentalclinicapp.controller;

import com.dentalclinic.dentalclinicapp.entity.Employee;
import com.dentalclinic.dentalclinicapp.service.DentistService;
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
@RequestMapping("/api/dentists")
@Tag(name = "Dentist Management", description = "APIs for managing dentists")
public class DentistController {

    @Autowired
    private DentistService dentistService;

    @GetMapping
    @Operation(summary = "Get all dentists", description = "Retrieve a list of all dentists")
    public ResponseEntity<List<Employee>> getAllDentists() {
        List<Employee> dentists = dentistService.getAllDentists();
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dentist by ID", description = "Retrieve a dentist by their ID")
    public ResponseEntity<Employee> getDentistById(@PathVariable Long id) {
        Optional<Employee> dentist = dentistService.getDentistById(id);
        return dentist.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new dentist", description = "Create a new dentist record")
    public ResponseEntity<Employee> createDentist(@Valid @RequestBody Employee dentist) {
        try {
            // Check if email exists through the person entity
            String email = dentist.getPerson() != null ? dentist.getPerson().getEmail() : null;
            if (email != null && dentistService.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            if (dentist.getLicenseNumber() != null
                    && dentistService.existsByLicenseNumber(dentist.getLicenseNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            Employee savedDentist = dentistService.saveDentist(dentist);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDentist);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update dentist", description = "Update an existing dentist record")
    public ResponseEntity<Employee> updateDentist(@PathVariable Long id,
            @Valid @RequestBody Employee dentistDetails) {
        try {
            Employee updatedDentist = dentistService.updateDentist(id, dentistDetails);
            return ResponseEntity.ok(updatedDentist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete dentist", description = "Delete a dentist record")
    public ResponseEntity<Void> deleteDentist(@PathVariable Long id) {
        try {
            dentistService.deleteDentist(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search dentists", description = "Search dentists by name or specialization")
    public ResponseEntity<List<Employee>> searchDentists(@RequestParam String searchTerm) {
        List<Employee> dentists = dentistService.searchDentists(searchTerm);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Find dentists by specialization", description = "Find dentists by their specialization")
    public ResponseEntity<List<Employee>> getDentistsBySpecialization(@PathVariable String specialization) {
        List<Employee> dentists = dentistService.findBySpecialization(specialization);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Find dentist by email", description = "Find a dentist by their email address")
    public ResponseEntity<Employee> getDentistByEmail(@PathVariable String email) {
        Optional<Employee> dentist = dentistService.findByEmail(email);
        return dentist.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/license/{licenseNumber}")
    @Operation(summary = "Find dentist by license", description = "Find a dentist by their license number")
    public ResponseEntity<Employee> getDentistByLicenseNumber(@PathVariable String licenseNumber) {
        Optional<Employee> dentist = dentistService.findByLicenseNumber(licenseNumber);
        return dentist.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/experience/{years}")
    @Operation(summary = "Find dentists by minimum experience", description = "Find dentists with minimum years of experience")
    public ResponseEntity<List<Employee>> getDentistsByMinimumExperience(@PathVariable Integer years) {
        List<Employee> dentists = dentistService.findByMinimumExperience(years);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/count")
    @Operation(summary = "Get total dentist count", description = "Get the total number of dentists")
    public ResponseEntity<Long> getTotalDentistCount() {
        long count = dentistService.getTotalDentistCount();
        return ResponseEntity.ok(count);
    }
}
