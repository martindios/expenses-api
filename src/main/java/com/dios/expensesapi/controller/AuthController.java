package com.dios.expensesapi.controller;

import com.dios.expensesapi.config.JwtUtil;
import com.dios.expensesapi.dto.AuthResponse;
import com.dios.expensesapi.dto.LoginRequest;
import com.dios.expensesapi.dto.RegisterRequest;
import com.dios.expensesapi.model.Role;
import com.dios.expensesapi.model.User;
import com.dios.expensesapi.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoint to manage authorization")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.USER)
                .build();

        User savedUser = userService.createUser(user);
        String token = jwtUtil.generateToken(savedUser);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(savedUser.getEmail());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
