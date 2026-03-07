package com.viratech.cadastrocliente.model.dto;

import java.time.LocalDate;

public record UserResponseDTO(

        String name,
        String email,
        String cpf,
        LocalDate birthDate,
        AddressDTO address
) {
}
