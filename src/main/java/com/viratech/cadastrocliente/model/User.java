package com.viratech.cadastrocliente.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users" , uniqueConstraints = {
        @UniqueConstraint(name="uk_user_email", columnNames = "email"),
        @UniqueConstraint(name="uk_user_cpf", columnNames = "cpf"),
        @UniqueConstraint(name = "uk_user_rg",  columnNames = "rg")
})

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Embedded
    private Address address;

}
