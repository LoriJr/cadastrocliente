package com.viratech.cadastrocliente.repository;

import com.viratech.cadastrocliente.model.entity.UserVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, Long> {

    Optional<UserVerificationToken> findByToken(String token);
}
