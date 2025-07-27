package com.dios.expensesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing authentication token and user information")
public class AuthResponse {

    // ================================
    // FIELDS
    // ================================

    @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuQG...")
    private String token;

    @Schema(description = "Token type", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    // ================================
    // CONSTRUCTORS
    // ================================

    public AuthResponse() {
    }

    // ================================
    // GETTERS AND SETTERS
    // ================================

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
