package com.viratech.cadastrocliente.dto;

import com.viratech.cadastrocliente.model.enums.RoleName;
import jakarta.validation.constraints.NotNull;

public record UserRoleRequest(
        @NotNull RoleName roleName
){}
