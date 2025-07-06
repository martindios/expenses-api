package com.dios.expensesapi.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private String firstName;
    private String lastName;
}
