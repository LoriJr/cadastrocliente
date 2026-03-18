package com.viratech.cadastrocliente.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserRequestDTO (

        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}", message = "{error.phone.invalid}")
        String phone,

        @NotBlank
        @CPF(message = "{error.cpf.invalid}")
        String cpf,

        @NotNull
        @Past
        LocalDate birthDate,

        @Valid
        AddressDTO address
){
}
