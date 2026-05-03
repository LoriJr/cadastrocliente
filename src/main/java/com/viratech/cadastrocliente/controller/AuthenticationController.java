package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Authentication> login(@Valid UserCredentialRequestDTO requestLogin){
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                requestLogin.email(), requestLogin.password());

        var authentication = authenticationManager.authenticate(authenticationToken);

        return ResponseEntity.ok(authentication);
    }
}
