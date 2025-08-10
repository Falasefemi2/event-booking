package com.femi.userservice.controller;

import com.femi.userservice.dto.ApiResponseData;
import com.femi.userservice.dto.AuthResponseDTO;
import com.femi.userservice.dto.LoginRequestDTO;
import com.femi.userservice.dto.RegisterRequestDTO;
import com.femi.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponseData<AuthResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        log.info("Registration request received for email: {}", request.getEmail());

        AuthResponseDTO response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseData.<AuthResponseDTO>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ApiResponseData<AuthResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {

        log.info("Login request received for email: {}", request.getEmail());

        AuthResponseDTO response = authService.login(request);

        return ResponseEntity.ok(ApiResponseData.<AuthResponseDTO>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build());
    }
}


