package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.authentication.AuthenticationService;
import com.viratech.cadastrocliente.authentication.TokenService;
import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.exceptions.InvalidLoginException;
import com.viratech.cadastrocliente.repository.UserCredentialRepository;
import com.viratech.cadastrocliente.security.AuthToken;
import com.viratech.cadastrocliente.security.RefreshToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável pela autenticação de usuários e gerenciamento
 * dos tokens de acesso da aplicação.
 *
 * <p>Disponibiliza endpoints para realização de login e renovação
 * de tokens JWT utilizando refresh tokens.</p>
 *
 * @author Lou Junior
 * @since 1.0
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@Valid @RequestBody UserCredentialRequestDTO requestLogin) {

        AuthToken authToken = authenticationService.login(
                requestLogin
        );
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthToken> refreshToken(@Valid @RequestBody RefreshToken token) {

        AuthToken authToken = authenticationService.refreshToken(
                token.refreshToken()
        );
        return ResponseEntity.ok(authToken);
    }

}
