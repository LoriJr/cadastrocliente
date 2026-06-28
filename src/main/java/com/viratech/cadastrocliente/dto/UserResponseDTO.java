package com.viratech.cadastrocliente.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponseDTO(

        Long id,
        String name,
        String email,
        String cpf,
        String rg,
        LocalDate birthDate,
        AddressDTO address,
        LocalDateTime createdAt
) {
}
