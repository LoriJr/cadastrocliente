package com.viratech.cadastrocliente.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String addressLine1;

    @Column(nullable = false)
    private String number;

    private String addressLine2;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;
}
