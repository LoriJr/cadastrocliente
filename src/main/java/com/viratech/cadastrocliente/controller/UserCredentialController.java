package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
import com.viratech.cadastrocliente.service.UserCredentialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class UserCredentialController {

    private final UserCredentialService credentialService;

    @PostMapping
    public ResponseEntity<UserCredentialResponseDTO> saveCredential(@RequestBody @Valid UserCredentialRequestDTO request){
        //TODO verificar lógica de consulta de usuário pelo email no service
        return null;
    }
}
