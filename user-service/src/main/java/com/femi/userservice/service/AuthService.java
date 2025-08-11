package com.femi.userservice.service;

import com.femi.userservice.config.JwtService;
import com.femi.userservice.dto.*;
import com.femi.userservice.exceptions.InvalidCredentialsException;
import com.femi.userservice.exceptions.UserAlreadyExistsException;
import com.femi.userservice.model.Role;
import com.femi.userservice.model.User;
import com.femi.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + request.getUsername() + " already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        log.info("User registered successfully with ID: {}", savedUser.getId());

        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .user(modelMapper.map(savedUser, UserDTO.class))
                .message("User registered successfully")
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        log.info("User login attempt with email: {}", request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwtToken = jwtService.generateToken(user);

            log.info("User logged in successfully: {}", request.getEmail());

            return AuthResponseDTO.builder()
                    .accessToken(jwtToken)
                    .user(modelMapper.map(user, UserDTO.class))
                    .message("Login successful")
                    .build();
        } catch (BadCredentialsException e) {
            log.warn("Invalid login attempt for email: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    public UserDTO getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        log.info("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDTO userDto = modelMapper.map(user, UserDTO.class);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .toList();

        if (roles.isEmpty()) {
            log.warn("No roles found for user: {}", email);
            userDto.setRole(Role.USER);
        } else {
            try {
                String roleName = roles.get(0).replace("ROLE_", "");
                userDto.setRole(Role.valueOf(roleName));
                log.info("Assigned role {} to user {}", roleName, email);
            } catch (IllegalArgumentException e) {
                log.error("Invalid role name: {} for user: {}", roles.get(0), email);
                userDto.setRole(Role.USER);
            }
        }
        return userDto;
    }





    public UserDTO updateProfile(Authentication authentication, UpdateProfileRequestDTO request) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UserAlreadyExistsException("Username already exists");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        User updatedUser = userRepository.save(user);
        log.info("User profile updated for ID: {}", updatedUser.getId());

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    public void changePassword(Authentication authentication, ChangePasswordRequestDTO request) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for user ID: {}", user.getId());
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        return modelMapper.map(user, UserDTO.class);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(String.valueOf(id))) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }

        userRepository.deleteById(String.valueOf(id));
        log.info("User deleted with ID: {}", id);
    }

    public UserDTO updateUserRole(Long id, Role role) {
        User user = userRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        user.setRole(role);
        User updatedUser = userRepository.save(user);

        log.info("User role updated to {} for user ID: {}", role, id);
        return modelMapper.map(updatedUser, UserDTO.class);
    }
}
