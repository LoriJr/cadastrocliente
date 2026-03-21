package com.viratech.cadastrocliente.model.dto;

import java.time.LocalDate;

public record UserResponseDTO(

        Long id,
        String name,
        String email,
        String cpf,
        String rg,
        LocalDate birthDate,
        AddressDTO address
) {
}
