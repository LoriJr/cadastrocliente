package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.model.entity.UserVerificationToken;
import com.viratech.cadastrocliente.model.enums.UserStatus;
import com.viratech.cadastrocliente.repository.UserVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserVerificationService {

    private final UserVerificationTokenRepository tokenRepository;

    @Transactional
    public void verifyEmail(String token){

        UserVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() ->
                new RuntimeException("Invalid Token"));

        if (verificationToken.isUsed()) {
            throw new RuntimeException("Token already used.");
        }

        if (verificationToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Expired token.");
        }

        User user = verificationToken.getUser();

        user.setUserStatus(UserStatus.ACTIVE);

        verificationToken.setUsed(true);

    }
}
