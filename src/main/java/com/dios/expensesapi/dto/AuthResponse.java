package com.dios.expensesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response object containing authentication token and user information")
public class AuthResponse {

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
}
