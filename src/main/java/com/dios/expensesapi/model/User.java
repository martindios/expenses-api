package com.dios.expensesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/* Implement UserDetails (Spring Security interface) so that the User
*  entity is directly compatible */
@Schema(description = "User entity representing a registered user in the system.")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique identifier for the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Column(unique = true, nullable = false)
    @Schema(description = "User's email address used for authentication", example = "user@example.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "User's encrypted password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Column(nullable = false)
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "User's role in the system", example = "USER")
    private Role role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when the user was created", example = "01-01-2001 01:01:01")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "Indicates if the user account is active", example = "true")
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}