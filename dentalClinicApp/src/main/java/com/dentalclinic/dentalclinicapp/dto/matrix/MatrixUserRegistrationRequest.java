package com.dentalclinic.dentalclinicapp.dto.matrix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for registering a Matrix user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatrixUserRegistrationRequest {
    private String username;
    private String password;
    private String displayName;
    private String email;
}
