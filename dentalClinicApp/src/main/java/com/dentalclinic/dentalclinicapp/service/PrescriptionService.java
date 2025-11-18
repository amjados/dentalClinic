package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Prescription;
import com.dentalclinic.dentalclinicapp.entity.Prescription.PrescriptionStatus;
import com.dentalclinic.dentalclinicapp.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    public Optional<Prescription> getPrescriptionByNumber(String prescriptionNumber) {
        return prescriptionRepository.findByPrescriptionNumber(prescriptionNumber);
    }

    public Prescription savePrescription(Prescription prescription) {
        if (prescription.getPrescriptionNumber() == null || prescription.getPrescriptionNumber().isEmpty()) {
            prescription.setPrescriptionNumber(generatePrescriptionNumber());
        }
        if (prescription.getPrescribedDate() == null) {
            prescription.setPrescribedDate(LocalDate.now());
        }
        return prescriptionRepository.save(prescription);
    }

    public Prescription updatePrescription(Long id, Prescription prescriptionDetails) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));

        prescription.setMedicationName(prescriptionDetails.getMedicationName());
        prescription.setMedicationType(prescriptionDetails.getMedicationType());
        prescription.setDosage(prescriptionDetails.getDosage());
        prescription.setFrequency(prescriptionDetails.getFrequency());
        prescription.setDuration(prescriptionDetails.getDuration());
        prescription.setQuantityPrescribed(prescriptionDetails.getQuantityPrescribed());
        prescription.setInstructions(prescriptionDetails.getInstructions());
        prescription.setStatus(prescriptionDetails.getStatus());
        prescription.setDispensedDate(prescriptionDetails.getDispensedDate());

        if (prescriptionDetails.getServedByEmployee() != null) {
            prescription.setServedByEmployee(prescriptionDetails.getServedByEmployee());
        }

        return prescriptionRepository.save(prescription);
    }

    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
        prescriptionRepository.delete(prescription);
    }

    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public List<Prescription> getPrescriptionsByPrescribingEmployeeId(Long employeeId) {
        return prescriptionRepository.findByRequestByEmployeeId(employeeId);
    }

    public List<Prescription> getPrescriptionsByDispensingEmployeeId(Long employeeId) {
        return prescriptionRepository.findByServedByEmployeeId(employeeId);
    }

    public List<Prescription> getPrescriptionsByTreatmentId(Long treatmentId) {
        return prescriptionRepository.findByTreatmentId(treatmentId);
    }

    public List<Prescription> getPrescriptionsByClinicId(Long clinicId) {
        return prescriptionRepository.findByClinicId(clinicId);
    }

    public List<Prescription> getPrescriptionsByStatus(PrescriptionStatus status) {
        return prescriptionRepository.findByStatus(status);
    }

    public List<Prescription> getPrescriptionsByPatientIdAndStatus(Long patientId, PrescriptionStatus status) {
        return prescriptionRepository.findByPatientIdAndStatus(patientId, status);
    }

    public List<Prescription> getPrescriptionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return prescriptionRepository.findByPrescribedDateBetween(startDate, endDate);
    }

    // TODO: Temporarily disabled - repository method is commented out due to
    // missing expiryDate field
    // public List<Prescription> getExpiredPrescriptions() {
    // return prescriptionRepository.findExpiredPrescriptions(LocalDate.now());
    // }

    public List<Prescription> searchPatientPrescriptions(Long patientId, String medicationName) {
        return prescriptionRepository.findByPatientIdAndMedicationName(patientId, medicationName);
    }

    // TODO: Temporarily disabled - repository method is commented out due to
    // missing refillsAllowed/refillsUsed fields
    // public List<Prescription> getPrescriptionsWithRefillsAvailable() {
    // return prescriptionRepository.findPrescriptionsWithRefillsAvailable();
    // }

    public Prescription updatePrescriptionStatus(Long id, PrescriptionStatus status) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
        prescription.setStatus(status);

        if (status == PrescriptionStatus.DISPENSED && prescription.getDispensedDate() == null) {
            prescription.setDispensedDate(LocalDate.now());
        }

        return prescriptionRepository.save(prescription);
    }

    public Prescription processRefill(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));

        // Update dispensed date for refill
        prescription.setDispensedDate(LocalDate.now());

        return prescriptionRepository.save(prescription);
    }

    public long getTotalPrescriptionCount() {
        return prescriptionRepository.count();
    }

    public long getCountByStatus(PrescriptionStatus status) {
        return prescriptionRepository.countByStatus(status);
    }

    public List<Prescription> getPrescriptionsByClinicAndDateRange(Long clinicId, LocalDate startDate,
            LocalDate endDate) {
        return prescriptionRepository.findByClinicIdAndDateRange(clinicId, startDate, endDate);
    }

    private String generatePrescriptionNumber() {
        String prefix = "RX";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp;
    }
}
