package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Appointment;
import com.dentalclinic.dentalclinicapp.entity.Appointment.AppointmentStatus;
import com.dentalclinic.dentalclinicapp.entity.Employee;
import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.entity.Person;
import com.dentalclinic.dentalclinicapp.repository.AppointmentRepository;
import com.dentalclinic.dentalclinicapp.repository.EmployeeRepository;
import com.dentalclinic.dentalclinicapp.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment testAppointment;
    private Patient testPatient;
    private Employee testEmployee;
    private Appointment updatedAppointment;

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

        testAppointment = Appointment.builder()
                .id(1L)
                .patient(testPatient)
                .servedByEmployee(testEmployee)
                .appointmentDateTime(LocalDateTime.of(2025, 10, 15, 10, 0))
                .estimatedDurationMinutes(60)
                .status(AppointmentStatus.SCHEDULED)
                .reason("Regular Checkup")
                .notes("First visit")
                .build();

        updatedAppointment = Appointment.builder()
                .appointmentDateTime(LocalDateTime.of(2025, 10, 16, 14, 0))
                .estimatedDurationMinutes(30)
                .status(AppointmentStatus.CONFIRMED)
                .reason("Follow-up")
                .notes("Second visit")
                .build();
    }

    @Test
    void getAllAppointments_ShouldReturnAllAppointments() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment, new Appointment());
        when(appointmentRepository.findAll()).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentService.getAllAppointments();

        // Then
        assertEquals(2, result.size());
        verify(appointmentRepository).findAll();
    }

    @Test
    void getAppointmentById_WhenAppointmentExists_ShouldReturnAppointment() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));

        // When
        Optional<Appointment> result = appointmentService.getAppointmentById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Regular Checkup", result.get().getReason());
        verify(appointmentRepository).findById(1L);
    }

    @Test
    void getAppointmentById_WhenAppointmentNotExists_ShouldReturnEmpty() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Appointment> result = appointmentService.getAppointmentById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(appointmentRepository).findById(1L);
    }

    @Test
    void saveAppointment_WithValidData_ShouldReturnSavedAppointment() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(appointmentRepository.findConflictingAppointments(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(testAppointment)).thenReturn(testAppointment);

        // When
        Appointment result = appointmentService.saveAppointment(testAppointment);

        // Then
        assertEquals(testAppointment, result);
        verify(patientRepository).findById(1L);
        verify(employeeRepository).findById(1L);
        verify(appointmentRepository).save(testAppointment);
    }

    @Test
    void saveAppointment_WithInvalidPatient_ShouldThrowException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> appointmentService.saveAppointment(testAppointment));
        assertEquals("Patient not found", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void saveAppointment_WithInvalidDentist_ShouldThrowException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> appointmentService.saveAppointment(testAppointment));
        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository).findById(1L);
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void saveAppointment_WithConflictingTime_ShouldThrowException() {
        // Given
        Appointment conflictingAppointment = Appointment.builder().id(2L).build();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(appointmentRepository.findConflictingAppointments(any(), any(), any()))
                .thenReturn(Arrays.asList(conflictingAppointment));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> appointmentService.saveAppointment(testAppointment));
        assertEquals("Appointment time conflicts with existing appointment", exception.getMessage());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void updateAppointment_WhenAppointmentExists_ShouldUpdateAndReturnAppointment() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.findConflictingAppointments(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        // When
        Appointment result = appointmentService.updateAppointment(1L, updatedAppointment);

        // Then
        assertEquals(LocalDateTime.of(2025, 10, 16, 14, 0), result.getAppointmentDateTime());
        assertEquals(AppointmentStatus.CONFIRMED, result.getStatus());
        assertEquals("Follow-up", result.getReason());
        assertEquals(30, result.getEstimatedDurationMinutes());
        verify(appointmentRepository).findById(1L);
        verify(appointmentRepository).save(testAppointment);
    }

    @Test
    void updateAppointment_WhenAppointmentNotExists_ShouldThrowException() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> appointmentService.updateAppointment(1L, updatedAppointment));
        assertEquals("Appointment not found with id: 1", exception.getMessage());
        verify(appointmentRepository).findById(1L);
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void deleteAppointment_WhenAppointmentExists_ShouldDeleteAppointment() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));

        // When
        appointmentService.deleteAppointment(1L);

        // Then
        verify(appointmentRepository).findById(1L);
        verify(appointmentRepository).delete(testAppointment);
    }

    @Test
    void deleteAppointment_WhenAppointmentNotExists_ShouldThrowException() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> appointmentService.deleteAppointment(1L));
        assertEquals("Appointment not found with id: 1", exception.getMessage());
        verify(appointmentRepository).findById(1L);
        verify(appointmentRepository, never()).delete(any());
    }

    @Test
    void getAppointmentsByPatient_ShouldReturnPatientAppointments() {
        // Given
        Long patientId = 1L;
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByPatientId(patientId)).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentService.getAppointmentsByPatient(patientId);

        // Then
        assertEquals(1, result.size());
        assertEquals(patientId, result.get(0).getPatient().getId());
        verify(appointmentRepository).findByPatientId(patientId);
    }

    @Test
    void getAppointmentsByEmployee_ShouldReturnEmployeeAppointments() {
        // Given
        Long employeeId = 1L;
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByServedByEmployeeId(employeeId)).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentService.getAppointmentsByEmployee(employeeId);

        // Then
        assertEquals(1, result.size());
        assertEquals(employeeId, result.get(0).getServedByEmployee().getId());
        verify(appointmentRepository).findByServedByEmployeeId(employeeId);
    }

    @Test
    void getAppointmentsByStatus_ShouldReturnAppointmentsWithStatus() {
        // Given
        AppointmentStatus status = AppointmentStatus.SCHEDULED;
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByStatus(status)).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentService.getAppointmentsByStatus(status);

        // Then
        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
        verify(appointmentRepository).findByStatus(status);
    }

    @Test
    void updateAppointmentStatus_WhenAppointmentExists_ShouldUpdateStatus() {
        // Given
        AppointmentStatus newStatus = AppointmentStatus.CONFIRMED;
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        // When
        Appointment result = appointmentService.updateAppointmentStatus(1L, newStatus);

        // Then
        assertEquals(newStatus, result.getStatus());
        verify(appointmentRepository).findById(1L);
        verify(appointmentRepository).save(testAppointment);
    }

    @Test
    void hasConflictingAppointment_WithNoConflicts_ShouldReturnFalse() {
        // Given
        when(appointmentRepository.findConflictingAppointments(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        // When
        boolean result = appointmentService.hasConflictingAppointment(testAppointment);

        // Then
        assertFalse(result);
        verify(appointmentRepository).findConflictingAppointments(
                eq(1L),
                eq(LocalDateTime.of(2025, 10, 15, 10, 0)),
                eq(LocalDateTime.of(2025, 10, 15, 11, 0)));
    }

    @Test
    void hasConflictingAppointment_WithConflicts_ShouldReturnTrue() {
        // Given
        Appointment conflictingAppointment = new Appointment();
        conflictingAppointment.setId(2L);
        when(appointmentRepository.findConflictingAppointments(any(), any(), any()))
                .thenReturn(Arrays.asList(conflictingAppointment));

        // When
        boolean result = appointmentService.hasConflictingAppointment(testAppointment);

        // Then
        assertTrue(result);
    }

    @Test
    void getTotalAppointmentCount_ShouldReturnCount() {
        // Given
        when(appointmentRepository.count()).thenReturn(10L);

        // When
        long result = appointmentService.getTotalAppointmentCount();

        // Then
        assertEquals(10L, result);
        verify(appointmentRepository).count();
    }

    @Test
    void getAppointmentCountByStatus_ShouldReturnStatusCount() {
        // Given
        AppointmentStatus status = AppointmentStatus.SCHEDULED;
        List<Appointment> appointments = Arrays.asList(testAppointment, new Appointment());
        when(appointmentRepository.findByStatus(status)).thenReturn(appointments);

        // When
        long result = appointmentService.getAppointmentCountByStatus(status);

        // Then
        assertEquals(2L, result);
        verify(appointmentRepository).findByStatus(status);
    }
}
