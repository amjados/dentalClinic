package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.config.MatrixConfig;
import com.dentalclinic.dentalclinicapp.dto.matrix.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for managing Matrix Protocol operations
 * Handles room creation, messaging, and user management
 */
@Service
@SuppressWarnings("unchecked")
public class MatrixService {

    private static final Logger logger = LoggerFactory.getLogger(MatrixService.class);

    @Autowired
    private MatrixConfig matrixConfig;

    @Autowired
    private RestTemplate matrixRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String accessToken;

    /**
     * Initialize Matrix bot by logging in
     */
    public boolean initializeMatrixBot() {
        if (!matrixConfig.isMatrixEnabled()) {
            logger.info("Matrix integration is disabled");
            return false;
        }

        try {
            // Check if access token is already configured
            if (matrixConfig.getBotAccessToken() != null && !matrixConfig.getBotAccessToken().isEmpty()) {
                this.accessToken = matrixConfig.getBotAccessToken();
                logger.info("Using configured Matrix access token");
                return true;
            }

            // Login to get access token
            String loginUrl = matrixConfig.getHomeserverUrl() + "/_matrix/client/r0/login";

            Map<String, Object> loginRequest = new HashMap<>();
            loginRequest.put("type", "m.login.password");
            loginRequest.put("user", matrixConfig.getBotUsername());
            loginRequest.put("password", matrixConfig.getBotPassword());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(loginRequest, headers);
            ResponseEntity<Map> response = matrixRestTemplate.postForEntity(loginUrl, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                this.accessToken = (String) response.getBody().get("access_token");
                logger.info("Successfully logged in to Matrix homeserver");
                return true;
            }

            logger.error("Failed to login to Matrix: {}", response.getStatusCode());
            return false;

        } catch (Exception e) {
            logger.error("Error initializing Matrix bot: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Create a new Matrix room for patient-dentist communication
     */
    public MatrixRoomResponse createRoom(CreateRoomRequest request) {
        if (!matrixConfig.isMatrixEnabled() || accessToken == null) {
            return MatrixRoomResponse.builder()
                    .success(false)
                    .message("Matrix is not enabled or not initialized")
                    .build();
        }

        try {
            String createRoomUrl = matrixConfig.getHomeserverUrl() +
                    "/_matrix/client/r0/createRoom?access_token=" + accessToken;

            Map<String, Object> roomRequest = new HashMap<>();
            roomRequest.put("name", request.getName());
            roomRequest.put("topic", request.getTopic());
            roomRequest.put("is_direct", request.isDirect());

            if (request.getPreset() != null) {
                roomRequest.put("preset", request.getPreset());
            } else {
                roomRequest.put("preset", "trusted_private_chat");
            }

            // Invite users if provided
            if (request.getInviteUserIds() != null && !request.getInviteUserIds().isEmpty()) {
                roomRequest.put("invite", request.getInviteUserIds());
            }

            // Set room to encrypted
            Map<String, Object> encryptionEvent = new HashMap<>();
            encryptionEvent.put("type", "m.room.encryption");
            encryptionEvent.put("state_key", "");
            Map<String, String> encryptionContent = new HashMap<>();
            encryptionContent.put("algorithm", "m.megolm.v1.aes-sha2");
            encryptionEvent.put("content", encryptionContent);

            roomRequest.put("initial_state", Collections.singletonList(encryptionEvent));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request1 = new HttpEntity<>(roomRequest, headers);
            ResponseEntity<Map> response = matrixRestTemplate.postForEntity(createRoomUrl, request1, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String roomId = (String) response.getBody().get("room_id");
                logger.info("Successfully created Matrix room: {}", roomId);

                return MatrixRoomResponse.builder()
                        .roomId(roomId)
                        .roomName(request.getName())
                        .success(true)
                        .message("Room created successfully")
                        .build();
            }

            return MatrixRoomResponse.builder()
                    .success(false)
                    .message("Failed to create room: " + response.getStatusCode())
                    .build();

        } catch (Exception e) {
            logger.error("Error creating Matrix room: {}", e.getMessage(), e);
            return MatrixRoomResponse.builder()
                    .success(false)
                    .message("Error creating room: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Send a message to a Matrix room
     */
    public MatrixMessageResponse sendMessage(SendMessageRequest request) {
        if (!matrixConfig.isMatrixEnabled() || accessToken == null) {
            return MatrixMessageResponse.builder()
                    .success(false)
                    .build();
        }

        try {
            String txnId = UUID.randomUUID().toString();
            String messageType = request.getMessageType() != null ? request.getMessageType() : "m.text";

            String sendMessageUrl = matrixConfig.getHomeserverUrl() +
                    "/_matrix/client/r0/rooms/" + request.getRoomId() +
                    "/send/m.room.message/" + txnId + "?access_token=" + accessToken;

            Map<String, Object> messageContent = new HashMap<>();
            messageContent.put("msgtype", messageType);
            messageContent.put("body", request.getMessage());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(messageContent, headers);
            ResponseEntity<Map> response = matrixRestTemplate.exchange(
                    sendMessageUrl, HttpMethod.PUT, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String eventId = (String) response.getBody().get("event_id");
                logger.info("Successfully sent message to room: {}", request.getRoomId());

                return MatrixMessageResponse.builder()
                        .eventId(eventId)
                        .roomId(request.getRoomId())
                        .message(request.getMessage())
                        .timestamp(LocalDateTime.now())
                        .success(true)
                        .build();
            }

            return MatrixMessageResponse.builder()
                    .success(false)
                    .build();

        } catch (Exception e) {
            logger.error("Error sending Matrix message: {}", e.getMessage(), e);
            return MatrixMessageResponse.builder()
                    .success(false)
                    .build();
        }
    }

    /**
     * Create a direct chat room between patient and dentist
     */
    public MatrixRoomResponse createPatientDentistRoom(Long patientId, Long employeeId) {
        String roomName = matrixConfig.getRoomPrefix() + "_patient_" + patientId + "_employee_" + employeeId;
        String topic = "Private consultation room";

        CreateRoomRequest request = CreateRoomRequest.builder()
                .name(roomName)
                .topic(topic)
                .isDirect(true)
                .preset("trusted_private_chat")
                .build();

        return createRoom(request);
    }

    /**
     * Send an automated notification (appointment reminder, test results, etc.)
     */
    public MatrixMessageResponse sendNotification(String roomId, String notificationMessage) {
        SendMessageRequest request = SendMessageRequest.builder()
                .roomId(roomId)
                .message(notificationMessage)
                .messageType("m.notice")
                .build();

        return sendMessage(request);
    }

    /**
     * Invite a user to a room
     */
    public boolean inviteUserToRoom(String roomId, String userId) {
        if (!matrixConfig.isMatrixEnabled() || accessToken == null) {
            return false;
        }

        try {
            String inviteUrl = matrixConfig.getHomeserverUrl() +
                    "/_matrix/client/r0/rooms/" + roomId + "/invite?access_token=" + accessToken;

            Map<String, Object> inviteRequest = new HashMap<>();
            inviteRequest.put("user_id", userId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(inviteRequest, headers);
            ResponseEntity<Map> response = matrixRestTemplate.postForEntity(inviteUrl, request, Map.class);

            boolean success = response.getStatusCode() == HttpStatus.OK;
            logger.info("Invite user {} to room {}: {}", userId, roomId, success);
            return success;

        } catch (Exception e) {
            logger.error("Error inviting user to room: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Get room messages (for history/sync)
     */
    public List<Map<String, Object>> getRoomMessages(String roomId, int limit) {
        if (!matrixConfig.isMatrixEnabled() || accessToken == null) {
            return Collections.emptyList();
        }

        try {
            String messagesUrl = matrixConfig.getHomeserverUrl() +
                    "/_matrix/client/r0/rooms/" + roomId +
                    "/messages?access_token=" + accessToken +
                    "&dir=b&limit=" + limit;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = matrixRestTemplate.exchange(
                    messagesUrl, HttpMethod.GET, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> chunk = (List<Map<String, Object>>) response.getBody().get("chunk");
                return chunk != null ? chunk : Collections.emptyList();
            }

            return Collections.emptyList();

        } catch (Exception e) {
            logger.error("Error getting room messages: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public boolean isMatrixEnabled() {
        return matrixConfig.isMatrixEnabled();
    }
}
