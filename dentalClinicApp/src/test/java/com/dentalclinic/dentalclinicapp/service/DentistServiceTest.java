package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Employee;
import com.dentalclinic.dentalclinicapp.entity.Employee.EmployeeType;
import com.dentalclinic.dentalclinicapp.entity.Employee.EmploymentStatus;
import com.dentalclinic.dentalclinicapp.entity.Person;
import com.dentalclinic.dentalclinicapp.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DentistServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DentistService dentistService;

    private Employee testDentist;
    private Employee updatedDentist;
    private Person testPerson;
    private Person updatedPerson;

    @BeforeEach
    void setUp() {
        testPerson = Person.builder()
                .id(1L)
                .firstName("Dr. John")
                .lastName("Smith")
                .email("dr.smith@clinic.com")
                .phone("555-0123")
                .address("123 Medical Plaza")
                .build();

        testDentist = Employee.builder()
                .id(1L)
                .person(testPerson)
                .employeeNumber("EMP001")
                .employeeType(EmployeeType.DENTIST)
                .licenseNumber("DDS12345")
                .specialization("General Dentistry")
                .experienceYears(10)
                .hireDate(LocalDate.now())
                .employmentStatus(EmploymentStatus.ACTIVE)
                .build();

        updatedPerson = Person.builder()
                .firstName("Dr. Jane")
                .lastName("Johnson")
                .email("dr.johnson@clinic.com")
                .phone("555-0456")
                .address("456 Dental Center")
                .build();

        updatedDentist = Employee.builder()
                .person(updatedPerson)
                .licenseNumber("DDS67890")
                .specialization("Orthodontics")
                .experienceYears(15)
                .build();
    }

    @Test
    void getAllDentists_ShouldReturnAllDentists() {
        // Given
        List<Employee> activeDentists = Arrays.asList(testDentist);
        when(employeeRepository.findActiveDentists()).thenReturn(activeDentists);

        // When
        List<Employee> result = dentistService.getAllDentists();

        // Then
        assertEquals(1, result.size());
        verify(employeeRepository).findActiveDentists();
    }

    @Test
    void getDentistById_WhenDentistExists_ShouldReturnDentist() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testDentist));

        // When
        Optional<Employee> result = dentistService.getDentistById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Dr. John", result.get().getPerson().getFirstName());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void getDentistById_WhenDentistNotExists_ShouldReturnEmpty() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Employee> result = dentistService.getDentistById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void saveDentist_ShouldReturnSavedDentist() {
        // Given
        when(employeeRepository.save(testDentist)).thenReturn(testDentist);

        // When
        Employee result = dentistService.saveDentist(testDentist);

        // Then
        assertEquals(testDentist, result);
        assertEquals(EmployeeType.DENTIST, result.getEmployeeType());
        verify(employeeRepository).save(testDentist);
    }

    @Test
    void updateDentist_WhenDentistExists_ShouldUpdateAndReturnDentist() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testDentist));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testDentist);

        // When
        Employee result = dentistService.updateDentist(1L, updatedDentist);

        // Then
        assertEquals("Dr. Jane", result.getPerson().getFirstName());
        assertEquals("Johnson", result.getPerson().getLastName());
        assertEquals("dr.johnson@clinic.com", result.getPerson().getEmail());
        assertEquals("DDS67890", result.getLicenseNumber());
        assertEquals("Orthodontics", result.getSpecialization());
        assertEquals(15, result.getExperienceYears());
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(testDentist);
    }

    @Test
    void updateDentist_WhenDentistNotExists_ShouldThrowException() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dentistService.updateDentist(1L, updatedDentist));
        assertEquals("Dentist not found with id: 1", exception.getMessage());
        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteDentist_WhenDentistExists_ShouldDeleteDentist() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testDentist));

        // When
        dentistService.deleteDentist(1L);

        // Then
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(testDentist);
    }

    @Test
    void deleteDentist_WhenDentistNotExists_ShouldThrowException() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dentistService.deleteDentist(1L));
        assertEquals("Dentist not found with id: 1", exception.getMessage());
        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).delete(any());
    }

    @Test
    void findByEmail_ShouldReturnDentist() {
        // Given
        String email = "dr.smith@clinic.com";
        List<Employee> allEmployees = Arrays.asList(testDentist);
        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // When
        Optional<Employee> result = dentistService.findByEmail(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getPerson().getEmail());
        verify(employeeRepository).findAll();
    }

    @Test
    void findByLicenseNumber_ShouldReturnDentist() {
        // Given
        String licenseNumber = "DDS12345";
        when(employeeRepository.findByLicenseNumber(licenseNumber)).thenReturn(Optional.of(testDentist));

        // When
        Optional<Employee> result = dentistService.findByLicenseNumber(licenseNumber);

        // Then
        assertTrue(result.isPresent());
        assertEquals(licenseNumber, result.get().getLicenseNumber());
        verify(employeeRepository).findByLicenseNumber(licenseNumber);
    }

    @Test
    void findBySpecialization_ShouldReturnDentistsWithSpecialization() {
        // Given
        String specialization = "General";
        List<Employee> allEmployees = Arrays.asList(testDentist);
        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // When
        List<Employee> result = dentistService.findBySpecialization(specialization);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getSpecialization().contains("General"));
        verify(employeeRepository).findAll();
    }

    @Test
    void searchDentists_ShouldReturnMatchingDentists() {
        // Given
        String searchTerm = "John";
        List<Employee> allEmployees = Arrays.asList(testDentist);
        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // When
        List<Employee> result = dentistService.searchDentists(searchTerm);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getPerson().getFirstName().contains("John"));
        verify(employeeRepository).findAll();
    }

    @Test
    void findByMinimumExperience_ShouldReturnExperiencedDentists() {
        // Given
        Integer minExperience = 5;
        List<Employee> allEmployees = Arrays.asList(testDentist);
        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // When
        List<Employee> result = dentistService.findByMinimumExperience(minExperience);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getExperienceYears() >= minExperience);
        verify(employeeRepository).findAll();
    }

    @Test
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        // Given
        String email = "dr.smith@clinic.com";
        List<Employee> allEmployees = Arrays.asList(testDentist);
        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // When
        boolean result = dentistService.existsByEmail(email);

        // Then
        assertTrue(result);
        verify(employeeRepository).findAll();
    }

    @Test
    void existsByEmail_WhenEmailNotExists_ShouldReturnFalse() {
        // Given
        String email = "nonexistent@clinic.com";
        List<Employee> allEmployees = Arrays.asList(testDentist);
        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // When
        boolean result = dentistService.existsByEmail(email);

        // Then
        assertFalse(result);
        verify(employeeRepository).findAll();
    }

    @Test
    void existsByLicenseNumber_WhenLicenseExists_ShouldReturnTrue() {
        // Given
        String licenseNumber = "DDS12345";
        when(employeeRepository.findByLicenseNumber(licenseNumber)).thenReturn(Optional.of(testDentist));

        // When
        boolean result = dentistService.existsByLicenseNumber(licenseNumber);

        // Then
        assertTrue(result);
        verify(employeeRepository).findByLicenseNumber(licenseNumber);
    }

    @Test
    void existsByLicenseNumber_WhenLicenseNotExists_ShouldReturnFalse() {
        // Given
        String licenseNumber = "NONEXISTENT";
        when(employeeRepository.findByLicenseNumber(licenseNumber)).thenReturn(Optional.empty());

        // When
        boolean result = dentistService.existsByLicenseNumber(licenseNumber);

        // Then
        assertFalse(result);
        verify(employeeRepository).findByLicenseNumber(licenseNumber);
    }

    @Test
    void getTotalDentistCount_ShouldReturnCount() {
        // Given
        List<Employee> allEmployees = Arrays.asList(testDentist, testDentist, testDentist);
        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // When
        long result = dentistService.getTotalDentistCount();

        // Then
        assertEquals(3L, result);
        verify(employeeRepository).findAll();
    }
}
