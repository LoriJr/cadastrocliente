package com.viratech.cadastrocliente.dto;

import com.viratech.cadastrocliente.model.enums.RoleName;

import java.time.LocalDateTime;
import java.util.Set;

public record UserRoleResponseDTO(
        String name,
        String email,
        LocalDateTime createdAt,
        Set<RoleName> roles
) {
}
