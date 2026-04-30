package com.viratech.cadastrocliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCredentialRequestDTO(

        @NotBlank
        @Email(message = "error.email.invalid")
        String email,

        @NotBlank
        String password
) {
}
