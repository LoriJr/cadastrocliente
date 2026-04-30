package com.viratech.cadastrocliente.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserRequestDTO (

        @NotBlank
        String name,

        @NotBlank
        @Email(message = "error.email.invalid")
        String email,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}", message = "error.phone.invalid")
        String phone,

        @NotBlank
        @CPF(message = "error.cpf.invalid")
        String cpf,

        @NotBlank
        @Pattern(regexp = "^(([0-9]{1,2}\\\\.[0-9]{3}\\\\.[0-9]{3}-[0-9Xx])|([0-9]{7,9}))$", message = "error.rg.invalid")
        String rg,

        @NotNull
        @Past
        LocalDate birthDate,

        @Valid
        AddressDTO address
){
}
