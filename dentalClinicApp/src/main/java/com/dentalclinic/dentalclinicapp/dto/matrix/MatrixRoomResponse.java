package com.dentalclinic.dentalclinicapp.dto.matrix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Matrix room creation response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatrixRoomResponse {
    private String roomId;
    private String roomName;
    private boolean success;
    private String message;
}
