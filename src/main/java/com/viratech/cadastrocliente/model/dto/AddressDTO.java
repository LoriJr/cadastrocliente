package com.viratech.cadastrocliente.model.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO (

        @NotBlank
        String postalCode,

        @NotBlank
        String addressLine1,

        @NotBlank
        String number,

        String addressLine2,

        @NotBlank
        String neighborhood,

        @NotBlank
        String city,

        @NotBlank
        String state
){
}
