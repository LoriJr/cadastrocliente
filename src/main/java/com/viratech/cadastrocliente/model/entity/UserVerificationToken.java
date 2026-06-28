package com.viratech.cadastrocliente.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user_verification_token")
public class UserVerificationToken {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @Column(nullable = false)
    private boolean used = false;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;


}
