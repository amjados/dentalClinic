package com.dentalclinic.dentalclinicapp.dto.matrix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Matrix message response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatrixMessageResponse {
    private String eventId;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime timestamp;
    private boolean success;
}
