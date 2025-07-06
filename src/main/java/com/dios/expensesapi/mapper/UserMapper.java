package com.dios.expensesapi.mapper;

import com.dios.expensesapi.dto.UserResponseDTO;
import com.dios.expensesapi.model.User;

public class UserMapper {

    public static UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
