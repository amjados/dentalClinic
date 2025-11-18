package com.dentalclinic.dentalclinicapp.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private Long userId;
    private String username;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String message, Long userId, String username, String role) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static class LoginResponseBuilder {
        private boolean success;
        private String message;
        private Long userId;
        private String username;
        private String role;

        public LoginResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public LoginResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public LoginResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public LoginResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public LoginResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(success, message, userId, username, role);
        }
    }
}
