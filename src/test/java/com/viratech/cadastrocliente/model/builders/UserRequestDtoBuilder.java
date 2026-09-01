package com.viratech.cadastrocliente.model.builders;


import java.time.LocalDate;
import com.viratech.cadastrocliente.dto.AddressDTO;
import com.viratech.cadastrocliente.dto.UserRequestDTO;

public class UserRequestDtoBuilder {
    private String name;
    private String email;
    private String phone;
    private String cpf;
    private String rg;
    private LocalDate birthDate;
    private AddressDTO address;

    private UserRequestDtoBuilder(){}

    public static UserRequestDtoBuilder umUserRequestDTO() {
        UserRequestDtoBuilder builder = new UserRequestDtoBuilder();
        setDefaultValues(builder);
        return builder;
    }

    private static void setDefaultValues(UserRequestDtoBuilder builder) {
        builder.name = "Usuario Valido";
        builder.email = "email@email";
        builder.phone = "11911112222";
        builder.cpf = "12332165478";
        builder.rg = "424214181";
        builder.birthDate = LocalDate.of(1995, 1, 1);
        builder.address = AddressBuilder.aAddress().nowDTO();
    }

    public UserRequestDtoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserRequestDtoBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserRequestDtoBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserRequestDtoBuilder cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public UserRequestDtoBuilder rg(String rg) {
        this.rg = rg;
        return this;
    }

    public UserRequestDtoBuilder birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public UserRequestDtoBuilder address(AddressDTO address) {
        this.address = address;
        return this;
    }

    public UserRequestDTO now() {
        return new UserRequestDTO(name, email, phone, cpf, rg, birthDate, address);
    }
}