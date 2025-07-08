package com.dios.expensesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User roles available in the system")
public enum Role {
    @Schema(description = "Regular user with basic permissions")
    USER,

    @Schema(description = "Administrator with full system access")
    ADMIN
}
