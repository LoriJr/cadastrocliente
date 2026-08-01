package com.viratech.cadastrocliente.dto;

import com.viratech.cadastrocliente.model.enums.RoleName;

import java.time.LocalDateTime;

public interface UserRoleProjection {

    String getName();
    String getEmail();
    LocalDateTime getCreatedAt();
    RoleName getRoleName();
}
