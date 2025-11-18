package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        // Update personal information through Person entity
        if (patient.getPerson() != null && patientDetails.getPerson() != null) {
            patient.getPerson().setFirstName(patientDetails.getPerson().getFirstName());
            patient.getPerson().setLastName(patientDetails.getPerson().getLastName());
            patient.getPerson().setEmail(patientDetails.getPerson().getEmail());
            patient.getPerson().setPhone(patientDetails.getPerson().getPhone());
            patient.getPerson().setDateOfBirth(patientDetails.getPerson().getDateOfBirth());
            patient.getPerson().setAddress(patientDetails.getPerson().getAddress());
            patient.getPerson().setEmergencyContactName(patientDetails.getPerson().getEmergencyContactName());
        }

        // Update patient-specific medical information
        patient.setMedicalHistory(patientDetails.getMedicalHistory());
        patient.setAllergies(patientDetails.getAllergies());
        patient.setBloodType(patientDetails.getBloodType());
        patient.setInsuranceProvider(patientDetails.getInsuranceProvider());
        patient.setInsurancePolicyNumber(patientDetails.getInsurancePolicyNumber());

        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByPersonEmail(email);
    }

    public List<Patient> searchPatients(String searchTerm) {
        return patientRepository.findByPersonFirstNameContainingIgnoreCaseOrPersonLastNameContainingIgnoreCase(
                searchTerm, searchTerm);
    }

    public Page<Patient> searchPatientsWithPagination(String searchTerm, Pageable pageable) {
        return patientRepository.searchPatientsByPersonName(searchTerm, pageable);
    }

    public List<Patient> findByPhone(String phone) {
        return patientRepository.findByPersonPhone(phone);
    }

    public List<Patient> findPatientsByBirthDateRange(LocalDate startDate, LocalDate endDate) {
        return patientRepository.findByPersonDateOfBirthBetween(startDate, endDate);
    }

    public boolean existsByEmail(String email) {
        return patientRepository.findByPersonEmail(email).isPresent();
    }

    public long getTotalPatientCount() {
        return patientRepository.count();
    }
}
