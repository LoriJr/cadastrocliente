package com.viratech.cadastrocliente.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserExportDTO(

    String name,
    String email,
    String phone,
    String cpf,
    String rg,
    LocalDate birthDate,
    LocalDateTime createdAt,

    String addressLine1,
    String number,
    String addressLine2,
    String neighborhood,
    String zipCode,
    String city,
    String state
){}
