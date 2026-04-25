package com.viratech.cadastrocliente.model.mapper;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserCredentialMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target= "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "username", source = "email")
    @Mapping(target = "password", source = "password", qualifiedByName = "encryptPassword")
    public abstract UserCredential toEntity(UserCredentialRequestDTO request);


    public abstract UserCredentialResponseDTO toDTO(UserCredential user);

    @Named("encryptPassword")
    protected String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
