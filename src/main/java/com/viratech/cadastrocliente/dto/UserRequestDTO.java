package com.viratech.cadastrocliente.dto;

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
        @Pattern(regexp = "\\d{10,11}", message = "Phone must contain 10 or 11 digits")
        String phone,

        @NotBlank
        @CPF
        String cpf,

        @NotNull
        @Past
        LocalDate birthDate,

        @Valid
        AddressDTO address
){
}
