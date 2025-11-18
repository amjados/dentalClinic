package com.dentalclinic.dentalclinicapp.controller;

import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.entity.Person;
import com.dentalclinic.dentalclinicapp.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebMvc
@Transactional
class PatientControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Patient testPatient;
    private Person testPerson;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Clear repositories
        patientRepository.deleteAll();
        entityManager.flush();

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
        testPatient = patientRepository.save(testPatient);
    }

    @Test
    void getAllPatients_ShouldReturnListOfPatients() throws Exception {
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patientNumber", is("PAT001")))
                .andExpect(jsonPath("$[0].person.firstName", is("John")))
                .andExpect(jsonPath("$[0].person.lastName", is("Doe")));
    }

    @Test
    void getPatientById_ShouldReturnPatient_WhenPatientExists() throws Exception {
        mockMvc.perform(get("/api/patients/{id}", testPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patientNumber", is("PAT001")))
                .andExpect(jsonPath("$.person.firstName", is("John")))
                .andExpect(jsonPath("$.person.email", is("john.doe@email.com")));
    }

    @Test
    void getPatientById_ShouldReturnNotFound_WhenPatientDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/patients/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPatient_ShouldCreateNewPatient() throws Exception {
        // Create new person for the patient
        Person newPerson = new Person();
        newPerson.setFirstName("Jane");
        newPerson.setLastName("Smith");
        newPerson.setEmail("jane.smith@email.com");
        newPerson.setPhone("987-654-3210");
        newPerson.setDateOfBirth(LocalDate.of(1985, 8, 20));
        newPerson.setGender(Person.Gender.FEMALE);
        entityManager.persist(newPerson);
        entityManager.flush();

        Patient newPatient = new Patient();
        newPatient.setPerson(newPerson);
        newPatient.setPatientNumber("PAT002");
        newPatient.setInsuranceProvider("AnotherInsurance");

        String patientJson = objectMapper.writeValueAsString(newPatient);

        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(patientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientNumber", is("PAT002")))
                .andExpect(jsonPath("$.person.firstName", is("Jane")));
    }

    @Test
    void updatePatient_ShouldUpdateExistingPatient() throws Exception {
        testPatient.setInsuranceProvider("UpdatedInsurance");
        String patientJson = objectMapper.writeValueAsString(testPatient);

        mockMvc.perform(put("/api/patients/{id}", testPatient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(patientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insuranceProvider", is("UpdatedInsurance")));
    }

    @Test
    void deletePatient_ShouldDeletePatient_WhenPatientExists() throws Exception {
        mockMvc.perform(delete("/api/patients/{id}", testPatient.getId()))
                .andExpect(status().isNoContent());

        // Verify patient was deleted
        mockMvc.perform(get("/api/patients/{id}", testPatient.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchPatientsByEmail_ShouldReturnPatient_WhenEmailExists() throws Exception {
        mockMvc.perform(get("/api/patients/email/{email}", "john.doe@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientNumber", is("PAT001")))
                .andExpect(jsonPath("$.person.email", is("john.doe@email.com")));
    }

    @Test
    void searchPatientsByPhone_ShouldReturnPatients_WhenPhoneExists() throws Exception {
        mockMvc.perform(get("/api/patients/phone/{phone}", "123-456-7890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].person.phone", is("123-456-7890")));
    }

    @Test
    void searchPatients_ShouldReturnPaginatedResults() throws Exception {
        mockMvc.perform(get("/api/patients/search/paginated")
                .param("searchTerm", "john")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].person.firstName", is("John")));
    }

    @Test
    void createPatient_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
        // Create patient without required fields
        Patient invalidPatient = new Patient();
        String patientJson = objectMapper.writeValueAsString(invalidPatient);

        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(patientJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateNonExistentPatient_ShouldReturnNotFound() throws Exception {
        Patient nonExistentPatient = new Patient();
        nonExistentPatient.setPatientNumber("PAT999");
        String patientJson = objectMapper.writeValueAsString(nonExistentPatient);

        mockMvc.perform(put("/api/patients/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(patientJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllPatients_ShouldReturnEmptyList_WhenNoPatients() throws Exception {
        // Clear all patients
        patientRepository.deleteAll();

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
