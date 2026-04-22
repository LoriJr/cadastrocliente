package com.viratech.cadastrocliente.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_credentials")
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String password;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
