package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.dto.LoginRequest;
import com.dentalclinic.dentalclinicapp.dto.LoginResponse;
import com.dentalclinic.dentalclinicapp.dto.RegisterRequest;
import com.dentalclinic.dentalclinicapp.dto.RegisterResponse;
import com.dentalclinic.dentalclinicapp.entity.Person;
import com.dentalclinic.dentalclinicapp.entity.User;
import com.dentalclinic.dentalclinicapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            return LoginResponse.builder()
                    .success(false)
                    .message("User not found")
                    .build();
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Invalid password")
                    .build();
        }

        // For now, return a simple success response
        // Later we can add JWT token generation here
        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return RegisterResponse.builder()
                    .success(false)
                    .message("Username already exists")
                    .build();
        }

        // Check if email already exists by examining all users' person records
        Optional<User> existingUserByEmail = userRepository.findAll().stream()
                .filter(user -> user.getPerson() != null && request.getEmail().equals(user.getPerson().getEmail()))
                .findFirst();

        if (existingUserByEmail.isPresent()) {
            return RegisterResponse.builder()
                    .success(false)
                    .message("Email already exists")
                    .build();
        }

        try {
            // Create Person first
            Person person = new Person();
            person.setFirstName(request.getFirstName());
            person.setLastName(request.getLastName());
            person.setEmail(request.getEmail());
            person.setPhone(request.getPhone());

            entityManager.persist(person);
            entityManager.flush(); // Ensure Person is saved and ID is generated

            // Create User
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPerson(person);
            user.setRole(User.Role.PATIENT); // Default role
            user.setEnabled(true);

            User savedUser = userRepository.save(user);

            return RegisterResponse.builder()
                    .success(true)
                    .message("User registered successfully")
                    .username(savedUser.getUsername())
                    .email(person.getEmail())
                    .build();
        } catch (Exception e) {
            return RegisterResponse.builder()
                    .success(false)
                    .message("Registration failed: " + e.getMessage())
                    .build();
        }
    }

    public boolean registerUser(User user) {
        try {
            // Check if username already exists
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return false;
            }

            // Encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Save user
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
