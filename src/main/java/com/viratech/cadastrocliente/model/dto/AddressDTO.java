package com.viratech.cadastrocliente.model.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO (

        @NotBlank
        String postalCode,

        @NotBlank
        String street,

        @NotBlank
        String number,

        @NotBlank
        String neighborhood,

        @NotBlank
        String city,

        @NotBlank
        String state
){
}
