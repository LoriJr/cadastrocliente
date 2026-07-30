package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
import com.viratech.cadastrocliente.dto.UserRoleRequest;
import com.viratech.cadastrocliente.dto.UserRoleResponse;
import com.viratech.cadastrocliente.service.UserCredentialService;
import com.viratech.cadastrocliente.service.UserVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controller responsável pelo gerenciamento/cadastro das credenciais de acesso dos usuários.
 *
 * <p>Disponibiliza endpoints para criação da senha de acesso de usuários
 * previamente cadastrados na plataforma por meio de seu endereço de e-mail.</p>
 *
 * @author Lou Junior
 * @since 1.0
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class UserCredentialController {

    private final UserCredentialService credentialService;
    private final UserVerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<UserCredentialResponseDTO> saveCredential(@RequestBody @Valid UserCredentialRequestDTO request){

        UserCredentialResponseDTO response = credentialService.saveUserCredential(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{email}")
                .buildAndExpand(response.email())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PatchMapping("/add-role/user/{id}")
    public ResponseEntity<UserRoleResponse> addRole(@PathVariable Long id, @RequestBody @Valid UserRoleRequest request){
        UserRoleResponse response = credentialService.addRole(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token){

        verificationService.verifyEmail(token);

        return ResponseEntity.ok("Conta ativada com sucesso");
    }
}
