package com.dentalclinic.dentalclinicapp.security;

import com.dentalclinic.dentalclinicapp.entity.Person;
import com.dentalclinic.dentalclinicapp.entity.User;
import com.dentalclinic.dentalclinicapp.entity.UserRole;
import com.dentalclinic.dentalclinicapp.repository.UserRepository;
import com.dentalclinic.dentalclinicapp.repository.UserRoleRepository;
import com.dentalclinic.dentalclinicapp.repository.UserActivitySummaryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebMvc
@Transactional
class AuthIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserActivitySummaryRepository userActivitySummaryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private User testUser;
    private Person testPerson;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Clear dependent data first to avoid FK constraint errors
        userActivitySummaryRepository.deleteAll();
        userRoleRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();

        // Create test person based on seed data structure (Dr. Wilson from seed data)
        testPerson = new Person();
        testPerson.setFirstName("Emily");
        testPerson.setLastName("Wilson");
        testPerson.setEmail("emily.wilson@dentalclinic.com");
        testPerson.setPhone("212-555-1001");
        testPerson.setDateOfBirth(LocalDate.of(1985, 3, 15));
        testPerson.setGender(Person.Gender.FEMALE);
        entityManager.persist(testPerson);
        entityManager.flush();

        // Create test user based on seed data (dr_wilson from seed data)
        testUser = new User();
        testUser.setPerson(testPerson);
        testUser.setUsername("dr_wilson");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole(User.Role.DENTIST);
        testUser.setEnabled(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser = userRepository.save(testUser);
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsValid() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "dr_wilson");
        loginRequest.put("password", "password123");

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.username", is("dr_wilson")))
                .andExpect(jsonPath("$.role", is("DENTIST")));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsInvalid() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "dr_wilson");
        loginRequest.put("password", "wrongpassword");

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    void register_ShouldCreateNewUser_WhenDataValid() throws Exception {
        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("username", "newuser");
        registerRequest.put("email", "newuser@dentalclinic.com");
        registerRequest.put("password", "newpassword123");
        registerRequest.put("firstName", "New");
        registerRequest.put("lastName", "User");
        registerRequest.put("phone", "555-123-4567");
        registerRequest.put("roleName", "PATIENT");

        String registerJson = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.username", is("newuser")))
                .andExpect(jsonPath("$.email", is("newuser@dentalclinic.com")));
    }

    @Test
    void register_ShouldReturnConflict_WhenUsernameExists() throws Exception {
        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("username", "dr_wilson"); // Existing username from setUp
        registerRequest.put("email", "different@dentalclinic.com");
        registerRequest.put("password", "password123");
        registerRequest.put("firstName", "Different");
        registerRequest.put("lastName", "User");
        registerRequest.put("phone", "555-987-6543");
        registerRequest.put("roleName", "PATIENT");

        String registerJson = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Username already exists")));
    }

    @Test
    void findUserByUsername_ShouldReturnUser_WhenExists() throws Exception {
        User foundUser = userRepository.findByUsername("dr_wilson").orElse(null);

        assert foundUser != null;
        assert foundUser.getUsername().equals("dr_wilson");
        assert foundUser.getEmail().equals("emily.wilson@dentalclinic.com");
        assert foundUser.getRole().equals(User.Role.DENTIST);
    }

    @Test
    void findUserByEmail_ShouldReturnUser_WhenExists() throws Exception {
        User foundUser = userRepository.findByEmail("emily.wilson@dentalclinic.com").orElse(null);

        assert foundUser != null;
        assert foundUser.getUsername().equals("dr_wilson");
        assert foundUser.getEmail().equals("emily.wilson@dentalclinic.com");
    }

    @Test
    void passwordEncoding_ShouldWorkCorrectly() throws Exception {
        String rawPassword = "testpassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assert passwordEncoder.matches(rawPassword, encodedPassword);
        assert !passwordEncoder.matches("wrongpassword", encodedPassword);
    }

    @Test
    void userRole_ShouldBeCorrectlyAssociated() throws Exception {
        User foundUser = userRepository.findByUsername("dr_wilson").orElse(null);

        assert foundUser != null;
        assert foundUser.getRole() != null;
        assert foundUser.getRole().equals(User.Role.DENTIST);
    }

    @Test
    void deactivateUser_ShouldUpdateEnabledStatus() throws Exception {
        testUser.setEnabled(false);
        User updatedUser = userRepository.save(testUser);

        assert !updatedUser.getEnabled();

        User foundUser = userRepository.findByUsername("dr_wilson").orElse(null);
        assert foundUser != null;
        assert !foundUser.getEnabled();
    }

    @Test
    void createUserRole_ShouldAssociateRoleWithUser() throws Exception {
        UserRole userRole = new UserRole();
        userRole.setUser(testUser);
        userRole.setRoleName("ADDITIONAL_ROLE");
        userRole.setRoleJsonDetails("{\"permissions\": [\"READ\", \"WRITE\"]}");
        userRole.setGrantedBy(testUser);

        UserRole savedRole = userRoleRepository.save(userRole);

        assert savedRole.getId() != null;
        assert savedRole.getUser().equals(testUser);
        assert savedRole.getRoleName().equals("ADDITIONAL_ROLE");
        assert savedRole.getIsActive();
    }

    @Test
    void findActiveUserRoles_ShouldReturnOnlyActiveRoles() throws Exception {
        // Create active role
        UserRole activeRole = new UserRole();
        activeRole.setUser(testUser);
        activeRole.setRoleName("ACTIVE_ROLE");
        activeRole.setIsActive(true);
        userRoleRepository.save(activeRole);

        // Create inactive role
        UserRole inactiveRole = new UserRole();
        inactiveRole.setUser(testUser);
        inactiveRole.setRoleName("INACTIVE_ROLE");
        inactiveRole.setIsActive(false);
        userRoleRepository.save(inactiveRole);

        // Test finding by user and active status
        List<UserRole> activeRoles = userRoleRepository.findByUserIdAndIsActiveTrue(testUser.getId());
        boolean hasActiveRole = userRoleRepository.existsByUserIdAndRoleNameAndIsActiveTrue(testUser.getId(),
                "ACTIVE_ROLE");
        boolean hasInactiveRole = userRoleRepository.existsByUserIdAndRoleNameAndIsActiveTrue(testUser.getId(),
                "INACTIVE_ROLE");

        assert activeRoles.size() == 1;
        assert activeRoles.get(0).getRoleName().equals("ACTIVE_ROLE");
        assert hasActiveRole;
        assert !hasInactiveRole;
    }

    @Test
    void findUsersByRole_ShouldReturnUsersWithSpecificRole() throws Exception {
        List<User> dentistUsers = userRepository.findByRole(User.Role.DENTIST);

        assert dentistUsers.size() == 1;
        assert dentistUsers.get(0).getUsername().equals("dr_wilson");
    }

    @Test
    void findActiveUsers_ShouldReturnOnlyEnabledUsers() throws Exception {
        // Create disabled user
        Person disabledPerson = new Person();
        disabledPerson.setFirstName("Disabled");
        disabledPerson.setLastName("User");
        disabledPerson.setEmail("disabled@test.com");
        disabledPerson.setPhone("987-654-3210");
        disabledPerson.setDateOfBirth(LocalDate.of(1990, 1, 1));
        disabledPerson.setGender(Person.Gender.FEMALE);
        entityManager.persist(disabledPerson);

        User disabledUser = new User();
        disabledUser.setPerson(disabledPerson);
        disabledUser.setUsername("disableduser");
        disabledUser.setPassword(passwordEncoder.encode("password123"));
        disabledUser.setRole(User.Role.DENTIST);
        disabledUser.setEnabled(false);
        userRepository.save(disabledUser);

        List<User> activeUsers = userRepository.findActiveUsers();

        assert activeUsers.size() == 1;
        assert activeUsers.get(0).getUsername().equals("dr_wilson");
        assert activeUsers.get(0).getEnabled();
    }
}
