package com.viratech.cadastrocliente.dto;

public record UserCredentialRequestDTO(
        String email,
        String password
) {
}
