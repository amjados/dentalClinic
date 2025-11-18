package com.dentalclinic.dentalclinicapp.controller;

import com.dentalclinic.dentalclinicapp.dto.matrix.*;
import com.dentalclinic.dentalclinicapp.entity.Employee;
import com.dentalclinic.dentalclinicapp.entity.Patient;
import com.dentalclinic.dentalclinicapp.repository.EmployeeRepository;
import com.dentalclinic.dentalclinicapp.repository.PatientRepository;
import com.dentalclinic.dentalclinicapp.service.MatrixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API Controller for Matrix chat operations
 * Provides endpoints for real-time messaging between patients and staff
 */
@RestController
@RequestMapping("/api/matrix")
@Tag(name = "Matrix Chat", description = "Real-time messaging API for patient-staff communication")
public class MatrixController {

        private static final Logger logger = LoggerFactory.getLogger(MatrixController.class);

        @Autowired
        private MatrixService matrixService;

        @Autowired
        private PatientRepository patientRepository;

        @Autowired
        private EmployeeRepository employeeRepository;

        /**
         * Initialize Matrix bot (should be called on app startup)
         */
        @PostMapping("/init")
        @Operation(summary = "Initialize Matrix bot", description = "Initialize connection to Matrix homeserver")
        public ResponseEntity<Map<String, Object>> initializeMatrix() {
                boolean success = matrixService.initializeMatrixBot();
                return ResponseEntity.ok(Map.of(
                                "success", success,
                                "message",
                                success ? "Matrix bot initialized successfully" : "Failed to initialize Matrix bot"));
        }

        /**
         * Create a new chat room
         */
        @PostMapping("/rooms/create")
        @Operation(summary = "Create a new Matrix room", description = "Create a new encrypted chat room")
        public ResponseEntity<MatrixRoomResponse> createRoom(@RequestBody CreateRoomRequest request) {
                logger.info("Creating Matrix room: {}", request.getName());
                MatrixRoomResponse response = matrixService.createRoom(request);
                return ResponseEntity.ok(response);
        }

        /**
         * Create a direct room between patient and employee (dentist/staff)
         */
        @PostMapping("/rooms/patient/{patientId}/employee/{employeeId}")
        @Operation(summary = "Create patient-employee chat room", description = "Create a secure direct chat room for patient-employee communication")
        public ResponseEntity<?> createPatientEmployeeRoom(
                        @PathVariable Long patientId,
                        @PathVariable Long employeeId) {

                logger.info("Creating chat room for patient {} and employee {}", patientId, employeeId);

                // Verify patient exists
                Patient patient = patientRepository.findById(patientId)
                                .orElse(null);
                if (patient == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(Map.of("error", "Patient not found"));
                }

                // Verify employee exists
                Employee employee = employeeRepository.findById(employeeId)
                                .orElse(null);
                if (employee == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(Map.of("error", "Employee not found"));
                }

                // Check if room already exists
                if (patient.getMatrixRoomId() != null && !patient.getMatrixRoomId().isEmpty()) {
                        return ResponseEntity.ok(MatrixRoomResponse.builder()
                                        .roomId(patient.getMatrixRoomId())
                                        .success(true)
                                        .message("Room already exists")
                                        .build());
                }

                // Create room
                MatrixRoomResponse response = matrixService.createPatientDentistRoom(patientId, employeeId);

                // Update patient with room ID
                if (response.isSuccess()) {
                        patient.setMatrixRoomId(response.getRoomId());
                        patientRepository.save(patient);

                        // Update employee if needed
                        if (employee.getMatrixRoomId() == null) {
                                employee.setMatrixRoomId(response.getRoomId());
                                employeeRepository.save(employee);
                        }
                }

                return ResponseEntity.ok(response);
        }

        /**
         * Send a message to a room
         */
        @PostMapping("/rooms/{roomId}/messages")
        @Operation(summary = "Send a message", description = "Send a message to a Matrix room")
        public ResponseEntity<MatrixMessageResponse> sendMessage(
                        @PathVariable String roomId,
                        @RequestBody Map<String, String> messageData) {

                String message = messageData.get("message");
                String messageType = messageData.getOrDefault("messageType", "m.text");

                SendMessageRequest request = SendMessageRequest.builder()
                                .roomId(roomId)
                                .message(message)
                                .messageType(messageType)
                                .build();

                MatrixMessageResponse response = matrixService.sendMessage(request);
                return ResponseEntity.ok(response);
        }

        /**
         * Send a notification to a patient
         */
        @PostMapping("/notifications/patient/{patientId}")
        @Operation(summary = "Send notification to patient", description = "Send an automated notification to a patient's chat room")
        public ResponseEntity<?> sendPatientNotification(
                        @PathVariable Long patientId,
                        @RequestBody Map<String, String> notificationData) {

                Patient patient = patientRepository.findById(patientId)
                                .orElse(null);

                if (patient == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(Map.of("error", "Patient not found"));
                }

                if (patient.getMatrixRoomId() == null || patient.getMatrixRoomId().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(Map.of("error", "Patient does not have a Matrix room"));
                }

                String message = notificationData.get("message");
                MatrixMessageResponse response = matrixService.sendNotification(
                                patient.getMatrixRoomId(), message);

                return ResponseEntity.ok(response);
        }

        /**
         * Get messages from a room
         */
        @GetMapping("/rooms/{roomId}/messages")
        @Operation(summary = "Get room messages", description = "Retrieve message history from a room")
        public ResponseEntity<Map<String, Object>> getRoomMessages(
                        @PathVariable String roomId,
                        @RequestParam(defaultValue = "50") int limit) {

                List<Map<String, Object>> messages = matrixService.getRoomMessages(roomId, limit);
                return ResponseEntity.ok(Map.of(
                                "roomId", roomId,
                                "messages", messages,
                                "count", messages.size()));
        }

        /**
         * Invite a user to a room
         */
        @PostMapping("/rooms/{roomId}/invite")
        @Operation(summary = "Invite user to room", description = "Invite a user to join a Matrix room")
        public ResponseEntity<Map<String, Object>> inviteUser(
                        @PathVariable String roomId,
                        @RequestBody Map<String, String> inviteData) {

                String userId = inviteData.get("userId");
                boolean success = matrixService.inviteUserToRoom(roomId, userId);

                return ResponseEntity.ok(Map.of(
                                "success", success,
                                "message", success ? "User invited successfully" : "Failed to invite user"));
        }

        /**
         * Get patient's Matrix room
         */
        @GetMapping("/patients/{patientId}/room")
        @Operation(summary = "Get patient's chat room", description = "Get Matrix room ID for a patient")
        public ResponseEntity<?> getPatientRoom(@PathVariable Long patientId) {
                Patient patient = patientRepository.findById(patientId)
                                .orElse(null);

                if (patient == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(Map.of("error", "Patient not found"));
                }

                return ResponseEntity.ok(Map.of(
                                "patientId", patientId,
                                "roomId", patient.getMatrixRoomId() != null ? patient.getMatrixRoomId() : "",
                                "hasRoom", patient.getMatrixRoomId() != null));
        }

        /**
         * Check if Matrix is enabled
         */
        @GetMapping("/status")
        @Operation(summary = "Check Matrix status", description = "Check if Matrix integration is enabled")
        public ResponseEntity<Map<String, Object>> getMatrixStatus() {
                return ResponseEntity.ok(Map.of(
                                "enabled", matrixService.isMatrixEnabled(),
                                "service", "Matrix Protocol Chat"));
        }
}
