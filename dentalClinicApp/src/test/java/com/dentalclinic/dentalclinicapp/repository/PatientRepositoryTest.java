package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.entity.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PatientRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PatientRepository patientRepository;

    private Patient testPatient;
    private Person testPerson;

    @BeforeEach
    void setUp() {
        // Clean up any existing test data
        patientRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Create test person
        testPerson = new Person();
        testPerson.setFirstName("John");
        testPerson.setLastName("Doe");
        testPerson.setEmail("john.doe@email.com");
        testPerson.setPhone("123-456-7890");
        testPerson.setDateOfBirth(LocalDate.of(1990, 5, 15));
        testPerson.setGender(Person.Gender.MALE);
        testPerson.setAddress("123 Main St");
        testPerson.setCity("TestCity");
        testPerson.setState("TestState");
        testPerson.setPostalCode("12345");

        entityManager.persist(testPerson);
        entityManager.flush();

        // Create test patient
        testPatient = new Patient();
        testPatient.setPerson(testPerson);
        testPatient.setPatientNumber("PAT001");
        testPatient.setInsuranceProvider("TestInsurance");
        testPatient.setInsurancePolicyNumber("POL123456");
        testPatient.setMedicalHistory("No known allergies");

        entityManager.persist(testPatient);
        entityManager.flush();
    }

    @Test
    void findByPersonEmail_ShouldReturnPatient_WhenEmailExists() {
        // When
        Optional<Patient> result = patientRepository.findByPersonEmail("john.doe@email.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getPatientNumber()).isEqualTo("PAT001");
        assertThat(result.get().getPerson().getFirstName()).isEqualTo("John");
    }

    @Test
    void findByPersonEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
        // When
        Optional<Patient> result = patientRepository.findByPersonEmail("nonexistent@email.com");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByPersonPhone_ShouldReturnPatient_WhenPhoneExists() {
        // When
        List<Patient> result = patientRepository.findByPersonPhone("123-456-7890");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPerson().getPhone()).isEqualTo("123-456-7890");
    }

    @Test
    void findByPersonFirstNameContainingIgnoreCaseOrPersonLastNameContainingIgnoreCase_ShouldReturnPatients() {
        // When
        List<Patient> result = patientRepository
                .findByPersonFirstNameContainingIgnoreCaseOrPersonLastNameContainingIgnoreCase("joh", "doe");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPerson().getFirstName()).isEqualTo("John");
    }

    @Test
    void searchPatientsByPersonName_ShouldReturnPatients_WhenSearchTermMatches() {
        // When
        Page<Patient> result = patientRepository.searchPatientsByPersonName("john", PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPerson().getFirstName()).isEqualTo("John");
    }

    @Test
    void searchPatientsByPersonName_ShouldReturnPatients_WhenSearchingByEmail() {
        // When
        Page<Patient> result = patientRepository.searchPatientsByPersonName("john.doe", PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPerson().getEmail()).isEqualTo("john.doe@email.com");
    }

    @Test
    void searchPatientsByPersonName_ShouldReturnPatients_WhenSearchingByPhone() {
        // When
        Page<Patient> result = patientRepository.searchPatientsByPersonName("123-456", PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPerson().getPhone()).isEqualTo("123-456-7890");
    }

    @Test
    void findByPersonDateOfBirthBetween_ShouldReturnPatients_WithinDateRange() {
        // Given
        LocalDate startDate = LocalDate.of(1989, 1, 1);
        LocalDate endDate = LocalDate.of(1991, 12, 31);

        // When
        List<Patient> result = patientRepository.findByPersonDateOfBirthBetween(startDate, endDate);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPatientNumber()).isEqualTo("PAT001");
        assertThat(result.get(0).getPerson().getDateOfBirth()).isEqualTo(LocalDate.of(1990, 5, 15));
    }

    @Test
    void save_ShouldPersistPatient() {
        // Given
        Person newPerson = new Person();
        newPerson.setFirstName("Alice");
        newPerson.setLastName("Johnson");
        newPerson.setEmail("alice.johnson@email.com");
        newPerson.setPhone("444-555-6666");
        newPerson.setDateOfBirth(LocalDate.of(1992, 3, 10));
        newPerson.setGender(Person.Gender.FEMALE);
        entityManager.persist(newPerson);
        entityManager.flush();

        Patient newPatient = new Patient();
        newPatient.setPerson(newPerson);
        newPatient.setPatientNumber("PAT003");
        newPatient.setInsuranceProvider("AnotherInsurance");

        // When
        Patient savedPatient = patientRepository.save(newPatient);

        // Then
        assertThat(savedPatient.getId()).isNotNull();
        assertThat(savedPatient.getPatientNumber()).isEqualTo("PAT003");

        // Verify in database
        Optional<Patient> retrieved = patientRepository.findByPersonEmail("alice.johnson@email.com");
        assertThat(retrieved).isPresent();
    }

    @Test
    void delete_ShouldRemovePatient() {
        // When
        patientRepository.delete(testPatient);
        entityManager.flush();

        // Then
        Optional<Patient> result = patientRepository.findByPersonEmail("john.doe@email.com");
        assertThat(result).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllPatients() {
        // Given - Create another patient
        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Smith");
        person2.setEmail("jane.smith@email.com");
        person2.setPhone("555-123-4567");
        person2.setDateOfBirth(LocalDate.of(1985, 8, 20));
        person2.setGender(Person.Gender.FEMALE);
        entityManager.persist(person2);
        entityManager.flush();

        Patient patient2 = new Patient();
        patient2.setPerson(person2);
        patient2.setPatientNumber("PAT002");
        patient2.setInsuranceProvider("TestInsurance");
        entityManager.persist(patient2);
        entityManager.flush();

        // When
        List<Patient> result = patientRepository.findAll();

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void existsById_ShouldReturnTrue_WhenPatientExists() {
        // When
        boolean exists = patientRepository.existsById(testPatient.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_ShouldReturnFalse_WhenPatientDoesNotExist() {
        // When
        boolean exists = patientRepository.existsById(99999L);

        // Then
        assertThat(exists).isFalse();
    }
}
