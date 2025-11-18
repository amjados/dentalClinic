package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.entity.Person;
import com.dentalclinic.dentalclinicapp.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;
    private Patient updatedPatient;

    @BeforeEach
    void setUp() {
        // Create test person
        Person testPerson = Person.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .build();

        testPatient = Patient.builder()
                .id(1L)
                .person(testPerson)
                .patientNumber("P001")
                .medicalHistory("No significant history")
                .allergies("None")
                .build();

        // Create updated person
        Person updatedPerson = Person.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@email.com")
                .phone("987-654-3210")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .address("456 Oak Ave")
                .build();

        updatedPatient = Patient.builder()
                .id(1L)
                .person(updatedPerson)
                .patientNumber("P001")
                .medicalHistory("Diabetes")
                .allergies("Penicillin")
                .build();
    }

    @Test
    void getAllPatients_ShouldReturnAllPatients() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient, new Patient());
        when(patientRepository.findAll()).thenReturn(patients);

        // When
        List<Patient> result = patientService.getAllPatients();

        // Then
        assertEquals(2, result.size());
        verify(patientRepository).findAll();
    }

    @Test
    void getPatientById_WhenPatientExists_ShouldReturnPatient() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        // When
        Optional<Patient> result = patientService.getPatientById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getPerson().getFirstName());
        verify(patientRepository).findById(1L);
    }

    @Test
    void getPatientById_WhenPatientNotExists_ShouldReturnEmpty() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Patient> result = patientService.getPatientById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(patientRepository).findById(1L);
    }

    @Test
    void savePatient_ShouldReturnSavedPatient() {
        // Given
        when(patientRepository.save(testPatient)).thenReturn(testPatient);

        // When
        Patient result = patientService.savePatient(testPatient);

        // Then
        assertEquals(testPatient, result);
        verify(patientRepository).save(testPatient);
    }

    @Test
    void updatePatient_WhenPatientExists_ShouldUpdateAndReturnPatient() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        Patient result = patientService.updatePatient(1L, updatedPatient);

        // Then
        assertEquals("Jane", result.getPerson().getFirstName());
        assertEquals("Smith", result.getPerson().getLastName());
        assertEquals("jane.smith@email.com", result.getPerson().getEmail());
        verify(patientRepository).findById(1L);
        verify(patientRepository).save(testPatient);
    }

    @Test
    void updatePatient_WhenPatientNotExists_ShouldThrowException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> patientService.updatePatient(1L, updatedPatient));
        assertEquals("Patient not found with id: 1", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(patientRepository, never()).save(any());
    }

    @Test
    void deletePatient_WhenPatientExists_ShouldDeletePatient() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        // When
        patientService.deletePatient(1L);

        // Then
        verify(patientRepository).findById(1L);
        verify(patientRepository).delete(testPatient);
    }

    @Test
    void deletePatient_WhenPatientNotExists_ShouldThrowException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> patientService.deletePatient(1L));
        assertEquals("Patient not found with id: 1", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(patientRepository, never()).delete(any());
    }

    @Test
    void findByEmail_ShouldReturnPatient() {
        // Given
        String email = "john.doe@email.com";
        when(patientRepository.findByPersonEmail(email)).thenReturn(Optional.of(testPatient));

        // When
        Optional<Patient> result = patientService.findByEmail(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getPerson().getEmail());
        verify(patientRepository).findByPersonEmail(email);
    }

    @Test
    void searchPatients_ShouldReturnMatchingPatients() {
        // Given
        String searchTerm = "John";
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByPersonFirstNameContainingIgnoreCaseOrPersonLastNameContainingIgnoreCase(
                searchTerm, searchTerm)).thenReturn(patients);

        // When
        List<Patient> result = patientService.searchPatients(searchTerm);

        // Then
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getPerson().getFirstName());
        verify(patientRepository).findByPersonFirstNameContainingIgnoreCaseOrPersonLastNameContainingIgnoreCase(
                searchTerm, searchTerm);
    }

    @Test
    void searchPatientsWithPagination_ShouldReturnPagedResults() {
        // Given
        String searchTerm = "John";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> page = new PageImpl<>(Arrays.asList(testPatient));
        when(patientRepository.searchPatientsByPersonName(searchTerm, pageable)).thenReturn(page);

        // When
        Page<Patient> result = patientService.searchPatientsWithPagination(searchTerm, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getPerson().getFirstName());
        verify(patientRepository).searchPatientsByPersonName(searchTerm, pageable);
    }

    @Test
    void findByPhone_ShouldReturnPatientsWithPhone() {
        // Given
        String phone = "123-456-7890";
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByPersonPhone(phone)).thenReturn(patients);

        // When
        List<Patient> result = patientService.findByPhone(phone);

        // Then
        assertEquals(1, result.size());
        assertEquals(phone, result.get(0).getPerson().getPhone());
        verify(patientRepository).findByPersonPhone(phone);
    }

    @Test
    void findPatientsByBirthDateRange_ShouldReturnPatientsInRange() {
        // Given
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1990, 12, 31);
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByPersonDateOfBirthBetween(startDate, endDate)).thenReturn(patients);

        // When
        List<Patient> result = patientService.findPatientsByBirthDateRange(startDate, endDate);

        // Then
        assertEquals(1, result.size());
        assertEquals(LocalDate.of(1990, 1, 1), result.get(0).getPerson().getDateOfBirth());
        verify(patientRepository).findByPersonDateOfBirthBetween(startDate, endDate);
    }

    @Test
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        // Given
        String email = "john.doe@email.com";
        when(patientRepository.findByPersonEmail(email)).thenReturn(Optional.of(testPatient));

        // When
        boolean result = patientService.existsByEmail(email);

        // Then
        assertTrue(result);
        verify(patientRepository).findByPersonEmail(email);
    }

    @Test
    void existsByEmail_WhenEmailNotExists_ShouldReturnFalse() {
        // Given
        String email = "nonexistent@email.com";
        when(patientRepository.findByPersonEmail(email)).thenReturn(Optional.empty());

        // When
        boolean result = patientService.existsByEmail(email);

        // Then
        assertFalse(result);
        verify(patientRepository).findByPersonEmail(email);
    }

    @Test
    void getTotalPatientCount_ShouldReturnCount() {
        // Given
        when(patientRepository.count()).thenReturn(5L);

        // When
        long result = patientService.getTotalPatientCount();

        // Then
        assertEquals(5L, result);
        verify(patientRepository).count();
    }
}
