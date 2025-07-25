package com.dios.expensesapi.service;

import com.dios.expensesapi.exception.DuplicateResourceException;
import com.dios.expensesapi.exception.ResourceNotFoundException;
import com.dios.expensesapi.model.Role;
import com.dios.expensesapi.model.User;
import com.dios.expensesapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private final UUID testUserId = UUID.randomUUID();
    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";
    private final String encodedPassword = "encodedPassword123";

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .id(testUserId)
                .email(testEmail)
                .password(testPassword)
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createUser_WithValidData_ShouldCreateUserSuccessfully() {
        // Arrange
        when(userRepository.existsByEmail(testEmail)).thenReturn(false);
        when(passwordEncoder.encode(testPassword)).thenReturn(encodedPassword);

        User savedUser = User.builder()
                .id(testUserId)
                .email(testEmail)
                .password(encodedPassword)
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(testUser);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getEmail()).isEqualTo(testEmail);
        assertThat(result.getPassword()).isEqualTo(encodedPassword);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");

        // Verify interactions
        verify(userRepository).existsByEmail(testEmail);
        verify(passwordEncoder).encode(testPassword);
        verify(userRepository).save(testUser);

        // Verify that the password was encoded before saving
        assertThat(testUser.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldThrowDuplicateResourceException() {
        when(userRepository.existsByEmail(testEmail)).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("email")
                .hasMessageContaining(testEmail);

        verify(userRepository).existsByEmail(testEmail);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

        User result = userService.findByEmail(testEmail);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getEmail()).isEqualTo(testEmail);
        assertThat(result.getFirstName()).isEqualTo("John");

        verify(userRepository).findByEmail(testEmail);
    }

    @Test
    void findByEmail_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail(testEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining(testEmail);

        verify(userRepository).findByEmail(testEmail);
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        User result = userService.findById(testUserId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getEmail()).isEqualTo(testEmail);
        assertThat(result.getFirstName()).isEqualTo("John");

        verify(userRepository).findById(testUserId);
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(testUserId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining(testUserId.toString());

        verify(userRepository).findById(testUserId);
    }

    @Test
    void createUser_ShouldEncodePasswordBeforeSaving() {
        String originalPassword = "plainPassword";
        testUser.setPassword(originalPassword);

        when(userRepository.existsByEmail(testEmail)).thenReturn(false);
        when(passwordEncoder.encode(originalPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.createUser(testUser);

        verify(passwordEncoder).encode(originalPassword);
        assertThat(testUser.getPassword()).isEqualTo(encodedPassword);
        verify(userRepository).save(testUser);
    }
}
