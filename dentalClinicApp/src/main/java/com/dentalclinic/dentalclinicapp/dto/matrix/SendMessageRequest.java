package com.dentalclinic.dentalclinicapp.dto.matrix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for sending a message in a Matrix room
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private String roomId;
    private String message;
    private String messageType; // "m.text", "m.notice", "m.emote", "m.file", "m.image"
}
