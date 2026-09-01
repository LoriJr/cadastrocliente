package com.viratech.cadastrocliente.model.builders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.viratech.cadastrocliente.dto.AddressDTO;
import com.viratech.cadastrocliente.dto.UserResponseDTO;

public class UserResponseDtoBuilder {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String rg;
    private LocalDate birthDate;
    private AddressDTO address;
    private LocalDateTime createdAt;

    private UserResponseDtoBuilder(){}

    public static UserResponseDtoBuilder umUserResponseDTO() {
        UserResponseDtoBuilder builder = new UserResponseDtoBuilder();
        setDefaultValues(builder);
        return builder;
    }

    private static void setDefaultValues(UserResponseDtoBuilder builder) {
        builder.id = 1L;
        builder.name = "Usuario Valido";
        builder.email = "email@email";
        builder.cpf = "12332165478";
        builder.rg = "424214181";
        builder.birthDate = LocalDate.of(1995, 1, 1);
        builder.address = AddressBuilder.aAddress().nowDTO();
        builder.createdAt = LocalDateTime.now();
    }

    public UserResponseDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserResponseDtoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserResponseDtoBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserResponseDtoBuilder cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public UserResponseDtoBuilder rg(String rg) {
        this.rg = rg;
        return this;
    }

    public UserResponseDtoBuilder birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public UserResponseDtoBuilder address(AddressDTO address) {
        this.address = address;
        return this;
    }

    public UserResponseDtoBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserResponseDTO now() {
        return new UserResponseDTO(id, name, email, cpf, rg, birthDate, address, createdAt);
    }
}