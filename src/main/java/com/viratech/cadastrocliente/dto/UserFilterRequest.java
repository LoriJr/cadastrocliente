package com.viratech.cadastrocliente.dto;

public record UserFilterRequest (
        Long id,
        String name,
        String email,
        String cpf
){
}
