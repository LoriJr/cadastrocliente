package com.viratech.cadastrocliente.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    private Long id;
    private String name;
    private String email;
    private String fone;
    private String cpf;
    private LocalDate birthDate;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Address address;

}
