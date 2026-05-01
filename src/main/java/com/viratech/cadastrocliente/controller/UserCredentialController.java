package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
import com.viratech.cadastrocliente.service.UserCredentialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class UserCredentialController {

    private final UserCredentialService credentialService;

    @PostMapping("/register")
    public ResponseEntity<UserCredentialResponseDTO> saveCredential(@RequestBody @Valid UserCredentialRequestDTO request){

        UserCredentialResponseDTO response = credentialService.saveUserCredential(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{email}")
                .buildAndExpand(response.email())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
