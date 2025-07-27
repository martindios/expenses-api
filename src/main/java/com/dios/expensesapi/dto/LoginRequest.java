package com.dios.expensesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request object for user authentication")
public class LoginRequest {

    // ================================
    // FIELDS
    // ================================

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User's password", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    // ================================
    // CONSTRUCTOR
    // ================================

    public LoginRequest() {
    }

    // ================================
    // GETTERS AND SETTERS
    // ================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
