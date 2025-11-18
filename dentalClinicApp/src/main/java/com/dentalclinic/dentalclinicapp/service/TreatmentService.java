package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Treatment;
import com.dentalclinic.dentalclinicapp.entity.Treatment.TreatmentStatus;
import com.dentalclinic.dentalclinicapp.entity.Employee;
import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.repository.TreatmentRepository;
import com.dentalclinic.dentalclinicapp.repository.EmployeeRepository;
import com.dentalclinic.dentalclinicapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TreatmentService {

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Treatment> getAllTreatments() {
        return treatmentRepository.findAll();
    }

    public Optional<Treatment> getTreatmentById(Long id) {
        return treatmentRepository.findById(id);
    }

    public Treatment saveTreatment(Treatment treatment) {
        // Validate that patient and dentist exist
        if (treatment.getPatient() != null && treatment.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(treatment.getPatient().getId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            treatment.setPatient(patient);
        }

        if (treatment.getServedByEmployee() != null && treatment.getServedByEmployee().getId() != null) {
            Employee employee = employeeRepository.findById(treatment.getServedByEmployee().getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            treatment.setServedByEmployee(employee);
        }

        return treatmentRepository.save(treatment);
    }

    public Treatment updateTreatment(Long id, Treatment treatmentDetails) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + id));

        treatment.setTreatmentName(treatmentDetails.getTreatmentName());
        treatment.setDescription(treatmentDetails.getDescription());
        treatment.setTreatmentDate(treatmentDetails.getTreatmentDate());
        treatment.setEstimatedCost(treatmentDetails.getEstimatedCost());
        treatment.setActualCost(treatmentDetails.getActualCost());
        treatment.setStatus(treatmentDetails.getStatus());
        treatment.setToothNumbers(treatmentDetails.getToothNumbers());
        treatment.setTreatmentNotes(treatmentDetails.getTreatmentNotes());
        treatment.setFollowUpRequired(treatmentDetails.getFollowUpRequired());
        treatment.setFollowUpDate(treatmentDetails.getFollowUpDate());

        return treatmentRepository.save(treatment);
    }

    public void deleteTreatment(Long id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + id));
        treatmentRepository.delete(treatment);
    }

    public List<Treatment> getTreatmentsByPatient(Long patientId) {
        return treatmentRepository.findByPatientId(patientId);
    }

    public List<Treatment> getTreatmentsByEmployee(Long employeeId) {
        return treatmentRepository.findByServedByEmployeeId(employeeId);
    }

    public List<Treatment> getTreatmentsByStatus(TreatmentStatus status) {
        return treatmentRepository.findByStatus(status);
    }

    public List<Treatment> getTreatmentsByDate(LocalDate treatmentDate) {
        return treatmentRepository.findByTreatmentDate(treatmentDate);
    }

    public List<Treatment> getTreatmentsRequiringFollowUp(LocalDate beforeDate) {
        return treatmentRepository.findTreatmentsRequiringFollowUp(beforeDate);
    }

    public List<Treatment> getPatientTreatmentsInRange(Long patientId,
            LocalDate startDate,
            LocalDate endDate) {
        return treatmentRepository.findByPatientAndDateBetween(patientId, startDate, endDate);
    }

    public List<Treatment> searchTreatmentsByName(String treatmentName) {
        return treatmentRepository.findByTreatmentNameContaining(treatmentName);
    }

    public BigDecimal getTotalCostByPatient(Long patientId) {
        BigDecimal total = treatmentRepository.getTotalCostByPatient(patientId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Treatment updateTreatmentStatus(Long id, TreatmentStatus status) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + id));
        treatment.setStatus(status);
        return treatmentRepository.save(treatment);
    }

    public long getTotalTreatmentCount() {
        return treatmentRepository.count();
    }

    public long getTreatmentCountByStatus(TreatmentStatus status) {
        return treatmentRepository.findByStatus(status).size();
    }

    public List<Treatment> getUpcomingFollowUps() {
        return treatmentRepository.findTreatmentsRequiringFollowUp(LocalDate.now().plusDays(7));
    }
}
