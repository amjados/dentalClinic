package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Treatment;
import com.dentalclinic.dentalclinicapp.entity.Treatment.TreatmentStatus;
import com.dentalclinic.dentalclinicapp.entity.Employee;
import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.entity.Person;
import com.dentalclinic.dentalclinicapp.repository.TreatmentRepository;
import com.dentalclinic.dentalclinicapp.repository.EmployeeRepository;
import com.dentalclinic.dentalclinicapp.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentServiceTest {

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TreatmentService treatmentService;

    private Treatment testTreatment;
    private Patient testPatient;
    private Employee testEmployee;
    private Treatment updatedTreatment;

    @BeforeEach
    void setUp() {
        // Create test person for patient
        Person patientPerson = Person.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        testPatient = Patient.builder()
                .id(1L)
                .person(patientPerson)
                .patientNumber("P001")
                .build();

        // Create test person for employee/dentist
        Person employeePerson = Person.builder()
                .id(2L)
                .firstName("Dr. Smith")
                .lastName("Johnson")
                .build();

        testEmployee = Employee.builder()
                .id(1L)
                .person(employeePerson)
                .employeeNumber("EMP001")
                .build();

        testTreatment = Treatment.builder()
                .id(1L)
                .patient(testPatient)
                .servedByEmployee(testEmployee)
                .treatmentName("Root Canal")
                .description("Root canal therapy for tooth #14")
                .treatmentDate(LocalDate.of(2025, 10, 15))
                .actualCost(new BigDecimal("800.00"))
                .status(TreatmentStatus.COMPLETED)
                .toothNumbers("14")
                .treatmentNotes("Patient tolerated procedure well")
                .followUpRequired(true)
                .followUpDate(LocalDate.of(2025, 11, 15))
                .build();

        updatedTreatment = Treatment.builder()
                .treatmentName("Dental Filling")
                .description("Composite filling for tooth #12")
                .treatmentDate(LocalDate.of(2025, 10, 20))
                .actualCost(new BigDecimal("150.00"))
                .status(TreatmentStatus.IN_PROGRESS)
                .toothNumbers("12")
                .treatmentNotes("Small cavity restored")
                .followUpRequired(false)
                .build();
    }

    @Test
    void getAllTreatments_ShouldReturnAllTreatments() {
        // Given
        List<Treatment> treatments = Arrays.asList(testTreatment, new Treatment());
        when(treatmentRepository.findAll()).thenReturn(treatments);

        // When
        List<Treatment> result = treatmentService.getAllTreatments();

        // Then
        assertEquals(2, result.size());
        verify(treatmentRepository).findAll();
    }

    @Test
    void getTreatmentById_WhenTreatmentExists_ShouldReturnTreatment() {
        // Given
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(testTreatment));

        // When
        Optional<Treatment> result = treatmentService.getTreatmentById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Root Canal", result.get().getTreatmentName());
        verify(treatmentRepository).findById(1L);
    }

    @Test
    void getTreatmentById_WhenTreatmentNotExists_ShouldReturnEmpty() {
        // Given
        when(treatmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Treatment> result = treatmentService.getTreatmentById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(treatmentRepository).findById(1L);
    }

    @Test
    void saveTreatment_WithValidData_ShouldReturnSavedTreatment() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(treatmentRepository.save(testTreatment)).thenReturn(testTreatment);

        // When
        Treatment result = treatmentService.saveTreatment(testTreatment);

        // Then
        assertEquals(testTreatment, result);
        verify(patientRepository).findById(1L);
        verify(employeeRepository).findById(1L);
        verify(treatmentRepository).save(testTreatment);
    }

    @Test
    void saveTreatment_WithInvalidPatient_ShouldThrowException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> treatmentService.saveTreatment(testTreatment));
        assertEquals("Patient not found", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(treatmentRepository, never()).save(any());
    }

    @Test
    void saveTreatment_WithInvalidEmployee_ShouldThrowException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> treatmentService.saveTreatment(testTreatment));
        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository).findById(1L);
        verify(treatmentRepository, never()).save(any());
    }

    @Test
    void updateTreatment_WhenTreatmentExists_ShouldUpdateAndReturnTreatment() {
        // Given
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(testTreatment));
        when(treatmentRepository.save(any(Treatment.class))).thenReturn(testTreatment);

        // When
        Treatment result = treatmentService.updateTreatment(1L, updatedTreatment);

        // Then
        assertEquals("Dental Filling", result.getTreatmentName());
        assertEquals("Composite filling for tooth #12", result.getDescription());
        assertEquals(new BigDecimal("150.00"), result.getActualCost());
        assertEquals(TreatmentStatus.IN_PROGRESS, result.getStatus());
        assertEquals("12", result.getToothNumbers());
        assertFalse(result.getFollowUpRequired());
        verify(treatmentRepository).findById(1L);
        verify(treatmentRepository).save(testTreatment);
    }

    @Test
    void updateTreatment_WhenTreatmentNotExists_ShouldThrowException() {
        // Given
        when(treatmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> treatmentService.updateTreatment(1L, updatedTreatment));
        assertEquals("Treatment not found with id: 1", exception.getMessage());
        verify(treatmentRepository).findById(1L);
        verify(treatmentRepository, never()).save(any());
    }

    @Test
    void deleteTreatment_WhenTreatmentExists_ShouldDeleteTreatment() {
        // Given
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(testTreatment));

        // When
        treatmentService.deleteTreatment(1L);

        // Then
        verify(treatmentRepository).findById(1L);
        verify(treatmentRepository).delete(testTreatment);
    }

    @Test
    void deleteTreatment_WhenTreatmentNotExists_ShouldThrowException() {
        // Given
        when(treatmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> treatmentService.deleteTreatment(1L));
        assertEquals("Treatment not found with id: 1", exception.getMessage());
        verify(treatmentRepository).findById(1L);
        verify(treatmentRepository, never()).delete(any());
    }

    @Test
    void getTreatmentsByPatient_ShouldReturnPatientTreatments() {
        // Given
        Long patientId = 1L;
        List<Treatment> treatments = Arrays.asList(testTreatment);
        when(treatmentRepository.findByPatientId(patientId)).thenReturn(treatments);

        // When
        List<Treatment> result = treatmentService.getTreatmentsByPatient(patientId);

        // Then
        assertEquals(1, result.size());
        assertEquals(patientId, result.get(0).getPatient().getId());
        verify(treatmentRepository).findByPatientId(patientId);
    }

    @Test
    void getTreatmentsByEmployee_ShouldReturnEmployeeTreatments() {
        // Given
        Long employeeId = 1L;
        List<Treatment> treatments = Arrays.asList(testTreatment);
        when(treatmentRepository.findByServedByEmployeeId(employeeId)).thenReturn(treatments);

        // When
        List<Treatment> result = treatmentService.getTreatmentsByEmployee(employeeId);

        // Then
        assertEquals(1, result.size());
        assertEquals(employeeId, result.get(0).getServedByEmployee().getId());
        verify(treatmentRepository).findByServedByEmployeeId(employeeId);
    }

    @Test
    void getTreatmentsByStatus_ShouldReturnTreatmentsWithStatus() {
        // Given
        TreatmentStatus status = TreatmentStatus.COMPLETED;
        List<Treatment> treatments = Arrays.asList(testTreatment);
        when(treatmentRepository.findByStatus(status)).thenReturn(treatments);

        // When
        List<Treatment> result = treatmentService.getTreatmentsByStatus(status);

        // Then
        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
        verify(treatmentRepository).findByStatus(status);
    }

    @Test
    void getTreatmentsByDate_ShouldReturnTreatmentsOnDate() {
        // Given
        LocalDate treatmentDate = LocalDate.of(2025, 10, 15);
        List<Treatment> treatments = Arrays.asList(testTreatment);
        when(treatmentRepository.findByTreatmentDate(treatmentDate)).thenReturn(treatments);

        // When
        List<Treatment> result = treatmentService.getTreatmentsByDate(treatmentDate);

        // Then
        assertEquals(1, result.size());
        assertEquals(treatmentDate, result.get(0).getTreatmentDate());
        verify(treatmentRepository).findByTreatmentDate(treatmentDate);
    }

    @Test
    void getTreatmentsRequiringFollowUp_ShouldReturnFollowUpTreatments() {
        // Given
        LocalDate beforeDate = LocalDate.of(2025, 12, 1);
        List<Treatment> treatments = Arrays.asList(testTreatment);
        when(treatmentRepository.findTreatmentsRequiringFollowUp(beforeDate)).thenReturn(treatments);

        // When
        List<Treatment> result = treatmentService.getTreatmentsRequiringFollowUp(beforeDate);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getFollowUpRequired());
        verify(treatmentRepository).findTreatmentsRequiringFollowUp(beforeDate);
    }

    @Test
    void searchTreatmentsByName_ShouldReturnMatchingTreatments() {
        // Given
        String treatmentName = "Root";
        List<Treatment> treatments = Arrays.asList(testTreatment);
        when(treatmentRepository.findByTreatmentNameContaining(treatmentName)).thenReturn(treatments);

        // When
        List<Treatment> result = treatmentService.searchTreatmentsByName(treatmentName);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTreatmentName().contains("Root"));
        verify(treatmentRepository).findByTreatmentNameContaining(treatmentName);
    }

    @Test
    void getTotalCostByPatient_WhenCostExists_ShouldReturnTotal() {
        // Given
        Long patientId = 1L;
        BigDecimal totalCost = new BigDecimal("1500.00");
        when(treatmentRepository.getTotalCostByPatient(patientId)).thenReturn(totalCost);

        // When
        BigDecimal result = treatmentService.getTotalCostByPatient(patientId);

        // Then
        assertEquals(totalCost, result);
        verify(treatmentRepository).getTotalCostByPatient(patientId);
    }

    @Test
    void getTotalCostByPatient_WhenCostIsNull_ShouldReturnZero() {
        // Given
        Long patientId = 1L;
        when(treatmentRepository.getTotalCostByPatient(patientId)).thenReturn(null);

        // When
        BigDecimal result = treatmentService.getTotalCostByPatient(patientId);

        // Then
        assertEquals(BigDecimal.ZERO, result);
        verify(treatmentRepository).getTotalCostByPatient(patientId);
    }

    @Test
    void updateTreatmentStatus_WhenTreatmentExists_ShouldUpdateStatus() {
        // Given
        TreatmentStatus newStatus = TreatmentStatus.COMPLETED;
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(testTreatment));
        when(treatmentRepository.save(any(Treatment.class))).thenReturn(testTreatment);

        // When
        Treatment result = treatmentService.updateTreatmentStatus(1L, newStatus);

        // Then
        assertEquals(newStatus, result.getStatus());
        verify(treatmentRepository).findById(1L);
        verify(treatmentRepository).save(testTreatment);
    }

    @Test
    void getTotalTreatmentCount_ShouldReturnCount() {
        // Given
        when(treatmentRepository.count()).thenReturn(15L);

        // When
        long result = treatmentService.getTotalTreatmentCount();

        // Then
        assertEquals(15L, result);
        verify(treatmentRepository).count();
    }

    @Test
    void getTreatmentCountByStatus_ShouldReturnStatusCount() {
        // Given
        TreatmentStatus status = TreatmentStatus.COMPLETED;
        List<Treatment> treatments = Arrays.asList(testTreatment, new Treatment());
        when(treatmentRepository.findByStatus(status)).thenReturn(treatments);

        // When
        long result = treatmentService.getTreatmentCountByStatus(status);

        // Then
        assertEquals(2L, result);
        verify(treatmentRepository).findByStatus(status);
    }

    @Test
    void getUpcomingFollowUps_ShouldReturnUpcomingFollowUps() {
        // Given
        LocalDate weekFromNow = LocalDate.now().plusDays(7);
        List<Treatment> treatments = Arrays.asList(testTreatment);
        when(treatmentRepository.findTreatmentsRequiringFollowUp(weekFromNow)).thenReturn(treatments);

        // When
        List<Treatment> result = treatmentService.getUpcomingFollowUps();

        // Then
        assertEquals(1, result.size());
        verify(treatmentRepository).findTreatmentsRequiringFollowUp(weekFromNow);
    }
}
