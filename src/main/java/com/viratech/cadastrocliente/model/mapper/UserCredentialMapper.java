package com.viratech.cadastrocliente.model.mapper;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
import com.viratech.cadastrocliente.dto.UserRoleResponse;
import com.viratech.cadastrocliente.model.entity.Role;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.enums.RoleName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserCredentialMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target= "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    //@Mapping(target = "username", source = "email")
    @Mapping(target = "password", source = "password", qualifiedByName = "encryptPassword")
    public abstract UserCredential toEntity(UserCredentialRequestDTO request);

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    public abstract UserCredentialResponseDTO toDTO(UserCredential user);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    public abstract UserRoleResponse toRoleResponse(UserCredential credential);

    @Named("encryptPassword")
    protected String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Named("mapRoles")
    protected Set<RoleName> mapRoles(Set<Role> roles) {

        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
    }
}
