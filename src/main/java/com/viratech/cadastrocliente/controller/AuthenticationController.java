package com.viratech.cadastrocliente.controller;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserCredentialRepository repository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserCredentialRequestDTO requestLogin) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    requestLogin.email(), requestLogin.password());

            var authentication = authenticationManager.authenticate(authenticationToken);

            String accessToken = tokenService.createToken((UserCredential) authentication.getPrincipal());
            String refreshToken = tokenService.createRefreshToken((UserCredential) authentication.getPrincipal());

            return ResponseEntity.ok(new AuthToken(accessToken, refreshToken));
        } catch (AuthenticationException ex) {
            throw new InvalidLoginException("email or password invalid");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshToken token) {

        try {
            var refreshTokenOld = token.refreshToken();
            Long id = Long.valueOf(tokenService.verifyToken(refreshTokenOld));
            var user = repository.findById(id);

            String accessToken = tokenService.createToken(user.orElseThrow());
            String refreshToken = tokenService.createRefreshToken(user.orElseThrow());

            return ResponseEntity.ok(new AuthToken(accessToken, refreshToken));
        } catch (AuthenticationException ex) {
            throw new InvalidLoginException("failed refresh token!");
        }
    }

}
