package com.femi.userservice.controller;

import com.femi.userservice.dto.*;
import com.femi.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "User Management", description = "User Management API")

public class UserController {

    private final AuthService authService;

    @GetMapping("/profile")
    @Operation(summary = "Get current user profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseData<UserDTO>> getCurrentUser(Authentication authentication) {

        UserDTO user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(ApiResponseData.<UserDTO>builder()
                .success(true)
                .message("User profile retrieved successfully")
                .data(user)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/profile")
    @Operation(summary = "Update current user profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseData<UserDTO>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequestDTO request) {

        UserDTO updatedUser = authService.updateProfile(authentication, request);

        return ResponseEntity.ok(ApiResponseData.<UserDTO>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(updatedUser)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change user password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseData<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequestDTO request) {

        authService.changePassword(authentication, request);

        return ResponseEntity.ok(ApiResponseData.<Void>builder()
                .success(true)
                .message("Password changed successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all users (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseData<List<UserDTO>>> getAllUsers() {

        List<UserDTO> users = authService.getAllUsers();

        return ResponseEntity.ok(ApiResponseData.<List<UserDTO>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(users)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseData<UserDTO>> getUserById(@PathVariable Long id) {

        UserDTO user = authService.getUserById(id);

        return ResponseEntity.ok(ApiResponseData.<UserDTO>builder()
                .success(true)
                .message("User retrieved successfully")
                .data(user)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseData<Void>> deleteUser(@PathVariable Long id) {

        authService.deleteUser(id);

        return ResponseEntity.ok(ApiResponseData.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseData<UserDTO>> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequestDTO request) {

        UserDTO updatedUser = authService.updateUserRole(id, request.getRole());

        return ResponseEntity.ok(ApiResponseData.<UserDTO>builder()
                .success(true)
                .message("User role updated successfully")
                .data(updatedUser)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
