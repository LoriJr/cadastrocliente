package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.authentication.TokenService;
import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserCredentialRequestDTO requestLogin){
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                requestLogin.email(), requestLogin.password());

        var authentication = authenticationManager.authenticate(authenticationToken);

        String accessToken = tokenService.createToken((UserCredential) authentication.getPrincipal());

        return ResponseEntity.ok(accessToken);
    }
}
