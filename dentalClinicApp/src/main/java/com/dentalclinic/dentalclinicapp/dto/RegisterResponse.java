package com.dentalclinic.dentalclinicapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    private boolean success;
    private String message;
    private Long userId;
    private String username;
    private String email;
    private String role;
}
