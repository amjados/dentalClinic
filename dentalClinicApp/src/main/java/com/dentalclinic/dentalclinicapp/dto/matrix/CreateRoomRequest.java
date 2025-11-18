package com.dentalclinic.dentalclinicapp.dto.matrix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating a Matrix chat room
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    private String name;
    private String topic;
    private boolean isDirect;
    private List<String> inviteUserIds;
    private String preset; // "private_chat", "public_chat", "trusted_private_chat"
}
