package com.femi.userservice.dto;

import com.femi.userservice.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequestDTO {

    @NotNull(message = "Role is required")
    private Role role;
}