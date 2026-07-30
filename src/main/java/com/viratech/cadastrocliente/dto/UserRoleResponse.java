package com.viratech.cadastrocliente.dto;

import com.viratech.cadastrocliente.model.enums.RoleName;

import java.util.Set;

public record UserRoleResponse(
        Long id,
        String name,
        String email,
        Set<RoleName> roles
) {
}
