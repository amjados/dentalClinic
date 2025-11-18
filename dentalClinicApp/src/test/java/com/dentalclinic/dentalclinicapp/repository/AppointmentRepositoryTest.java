package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.Appointment;
import com.dentalclinic.dentalclinicapp.entity.Appointment.AppointmentStatus;
import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.entity.Person;
import com.dentalclinic.dentalclinicapp.entity.Employee;
import com.dentalclinic.dentalclinicapp.entity.Clinic;
import com.dentalclinic.dentalclinicapp.entity.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AppointmentRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Appointment testAppointment;
    private Patient testPatient;
    private Employee testEmployee;
    private Clinic testClinic;
    private Person patientPerson;
    private Person employeePerson;

    @BeforeEach
    void setUp() {
        // Create test city
        City testCity = new City();
        testCity.setName("Test City");
        testCity.setState("Test State");
        testCity.setCountry("Test Country");
        entityManager.persist(testCity);
        entityManager.flush();

        // Create test clinic
        testClinic = new Clinic();
        testClinic.setName("Test Clinic");
        testClinic.setAddress("123 Test St");
        testClinic.setCity(testCity);
        testClinic.setPhone("555-0123");
        entityManager.persist(testClinic);
        entityManager.flush();

        // Create test person for patient
        patientPerson = new Person();
        patientPerson.setFirstName("John");
        patientPerson.setLastName("Doe");
        patientPerson.setEmail("john.doe@email.com");
        patientPerson.setPhone("123-456-7890");
        patientPerson.setDateOfBirth(LocalDate.of(1990, 5, 15));
        patientPerson.setGender(Person.Gender.MALE);
        entityManager.persist(patientPerson);
        entityManager.flush();

        // Create test patient
        testPatient = new Patient();
        testPatient.setPerson(patientPerson);
        testPatient.setPatientNumber("PAT001");
        testPatient.setInsuranceProvider("TestInsurance");
        entityManager.persist(testPatient);
        entityManager.flush();

        // Create test person for employee
        employeePerson = new Person();
        employeePerson.setFirstName("Dr. Jane");
        employeePerson.setLastName("Smith");
        employeePerson.setEmail("dr.jane@email.com");
        employeePerson.setPhone("987-654-3210");
        employeePerson.setDateOfBirth(LocalDate.of(1980, 3, 20));
        employeePerson.setGender(Person.Gender.FEMALE);
        entityManager.persist(employeePerson);
        entityManager.flush();

        // Create test employee
        testEmployee = new Employee();
        testEmployee.setPerson(employeePerson);
        testEmployee.setEmployeeNumber("EMP001");
        testEmployee.setClinic(testClinic);
        testEmployee.setPositionTitle("Dentist");
        testEmployee.setEmployeeType(Employee.EmployeeType.DENTIST);
        testEmployee.setHireDate(LocalDate.of(2020, 1, 1));
        entityManager.persist(testEmployee);
        entityManager.flush();

        // Create test appointment
        testAppointment = new Appointment();
        testAppointment.setPatient(testPatient);
        testAppointment.setRequestByEmployee(testEmployee);
        testAppointment.setServedByEmployee(testEmployee);
        testAppointment.setClinic(testClinic);
        testAppointment.setAppointmentDateTime(LocalDateTime.of(2025, 10, 15, 14, 30));
        testAppointment.setEstimatedDurationMinutes(30);
        testAppointment.setStatus(AppointmentStatus.SCHEDULED);
        testAppointment.setAppointmentType("Regular Checkup");
        entityManager.persist(testAppointment);
        entityManager.flush();
    }

    @Test
    void findByPatientId_ShouldReturnAppointments_WhenPatientHasAppointments() {
        // When
        List<Appointment> result = appointmentRepository.findByPatientId(testPatient.getId());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPatient().getId()).isEqualTo(testPatient.getId());
        assertThat(result.get(0).getAppointmentType()).isEqualTo("Regular Checkup");
    }

    @Test
    void findByServedByEmployeeId_ShouldReturnAppointments_WhenEmployeeHasAppointments() {
        // When
        List<Appointment> result = appointmentRepository.findByServedByEmployeeId(testEmployee.getId());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getServedByEmployee().getId()).isEqualTo(testEmployee.getId());
    }

    @Test
    void findByStatus_ShouldReturnAppointments_WithGivenStatus() {
        // When
        List<Appointment> result = appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED);

        // Then - check that our test appointment is in the results
        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(apt -> apt.getId().equals(testAppointment.getId()));
        assertThat(result.stream().filter(apt -> apt.getId().equals(testAppointment.getId())).findFirst())
                .isPresent()
                .get()
                .extracting(Appointment::getStatus)
                .isEqualTo(AppointmentStatus.SCHEDULED);
    }

    @Test
    void findByEmployeeAndDateTimeBetween_ShouldReturnAppointments_WithinDateRange() {
        // Given
        LocalDateTime startDateTime = LocalDateTime.of(2025, 10, 15, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 10, 15, 18, 0);

        // When
        List<Appointment> result = appointmentRepository.findByEmployeeAndDateTimeBetween(
                testEmployee.getId(), startDateTime, endDateTime);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getServedByEmployee().getId()).isEqualTo(testEmployee.getId());
        assertThat(result.get(0).getAppointmentDateTime()).isBetween(startDateTime, endDateTime);
    }

    @Test
    void findByPatientAndDateTimeBetween_ShouldReturnAppointments_WithinDateRange() {
        // Given
        LocalDateTime startDateTime = LocalDateTime.of(2025, 10, 15, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 10, 15, 18, 0);

        // When
        List<Appointment> result = appointmentRepository.findByPatientAndDateTimeBetween(
                testPatient.getId(), startDateTime, endDateTime);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPatient().getId()).isEqualTo(testPatient.getId());
        assertThat(result.get(0).getAppointmentDateTime()).isBetween(startDateTime, endDateTime);
    }

    @Test
    void findByAppointmentDate_ShouldReturnAppointments_OnGivenDate() {
        // Given
        LocalDateTime searchDate = LocalDateTime.of(2025, 10, 15, 12, 0);

        // When
        List<Appointment> result = appointmentRepository.findByAppointmentDate(searchDate);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAppointmentDateTime().toLocalDate())
                .isEqualTo(searchDate.toLocalDate());
    }

    @Test
    void findConflictingAppointments_ShouldReturnConflictingAppointments() {
        // Given - Create another appointment that conflicts
        Appointment conflictingAppointment = new Appointment();
        conflictingAppointment.setPatient(testPatient);
        conflictingAppointment.setRequestByEmployee(testEmployee);
        conflictingAppointment.setServedByEmployee(testEmployee);
        conflictingAppointment.setClinic(testClinic);
        conflictingAppointment.setAppointmentDateTime(LocalDateTime.of(2025, 10, 15, 14, 45));
        conflictingAppointment.setStatus(AppointmentStatus.SCHEDULED);
        entityManager.persist(conflictingAppointment);
        entityManager.flush();

        LocalDateTime startTime = LocalDateTime.of(2025, 10, 15, 14, 30);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 15, 15, 0);

        // When
        List<Appointment> result = appointmentRepository.findConflictingAppointments(
                testEmployee.getId(), startTime, endTime);

        // Then
        assertThat(result).hasSize(2); // Both appointments should be returned as conflicting
        assertThat(result).extracting(Appointment::getServedByEmployee)
                .extracting(Employee::getId)
                .containsOnly(testEmployee.getId());
    }

    @Test
    void findConflictingAppointments_ShouldNotReturnCancelledAppointments() {
        // Given - Create a cancelled appointment at the same time
        Appointment cancelledAppointment = new Appointment();
        cancelledAppointment.setPatient(testPatient);
        cancelledAppointment.setRequestByEmployee(testEmployee);
        cancelledAppointment.setServedByEmployee(testEmployee);
        cancelledAppointment.setClinic(testClinic);
        cancelledAppointment.setAppointmentDateTime(LocalDateTime.of(2025, 10, 15, 14, 30));
        cancelledAppointment.setStatus(AppointmentStatus.CANCELLED);
        entityManager.persist(cancelledAppointment);
        entityManager.flush();

        LocalDateTime startTime = LocalDateTime.of(2025, 10, 15, 14, 30);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 15, 15, 0);

        // When
        List<Appointment> result = appointmentRepository.findConflictingAppointments(
                testEmployee.getId(), startTime, endTime);

        // Then
        assertThat(result).hasSize(1); // Only the scheduled appointment, not the cancelled one
        assertThat(result.get(0).getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
    }

    @Test
    void save_ShouldPersistAppointment() {
        // Given
        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(testPatient);
        newAppointment.setRequestByEmployee(testEmployee);
        newAppointment.setServedByEmployee(testEmployee);
        newAppointment.setClinic(testClinic);
        newAppointment.setAppointmentDateTime(LocalDateTime.of(2025, 10, 16, 10, 0));
        newAppointment.setEstimatedDurationMinutes(60);
        newAppointment.setStatus(AppointmentStatus.SCHEDULED);
        newAppointment.setAppointmentType("Root Canal");

        // When
        Appointment savedAppointment = appointmentRepository.save(newAppointment);

        // Then
        assertThat(savedAppointment.getId()).isNotNull();
        assertThat(savedAppointment.getAppointmentType()).isEqualTo("Root Canal");
        assertThat(savedAppointment.getEstimatedDurationMinutes()).isEqualTo(60);
    }

    @Test
    void delete_ShouldRemoveAppointment() {
        // When
        appointmentRepository.delete(testAppointment);
        entityManager.flush();

        // Then
        List<Appointment> result = appointmentRepository.findByPatientId(testPatient.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllAppointments() {
        // Given - Create another appointment
        Appointment secondAppointment = new Appointment();
        secondAppointment.setPatient(testPatient);
        secondAppointment.setRequestByEmployee(testEmployee);
        secondAppointment.setServedByEmployee(testEmployee);
        secondAppointment.setClinic(testClinic);
        secondAppointment.setAppointmentDateTime(LocalDateTime.of(2025, 10, 16, 15, 0));
        secondAppointment.setStatus(AppointmentStatus.SCHEDULED);
        entityManager.persist(secondAppointment);
        entityManager.flush();

        // When
        List<Appointment> result = appointmentRepository.findAll();

        // Then - check that both our test appointments are in the results
        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(apt -> apt.getId().equals(testAppointment.getId()));
        assertThat(result).anyMatch(apt -> apt.getId().equals(secondAppointment.getId()));
    }

    @Test
    void updateAppointmentStatus_ShouldUpdateStatus() {
        // Given
        testAppointment.setStatus(AppointmentStatus.COMPLETED);

        // When
        Appointment updatedAppointment = appointmentRepository.save(testAppointment);

        // Then
        assertThat(updatedAppointment.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);

        // Verify in database
        Appointment retrieved = appointmentRepository.findById(testAppointment.getId()).orElse(null);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
    }
}
