package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.model.dto.UserRequestDTO;
import com.viratech.cadastrocliente.model.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
@Tag(name="cadastrocliente")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Método responsável por retornar todos os usuários cadastrados no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<List<UserResponseDTO>> findUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping
    @Operation(summary = "Salva dados de usuário", description = "Método para salvar dados de usuário")
    @ApiResponse(responseCode = "201", description = "Usuário salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Email já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody @Valid UserRequestDTO requestDTO){
        UserResponseDTO userResponseDTO = userService.userSave(requestDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResponseDTO.id())
                .toUri();

        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    @GetMapping("/{email}")
    @Operation(summary = "Consulta usuário por email", description = "Método para consultar usuário por email")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "400", description = "Email inválido")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<UserResponseDTO> findUser(
            @PathVariable
            @Email(message="The email format is invalid")
            String email){
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza um usuário existente",description = "Método para atualizar os dados de um usuário e seu endereço com base no ID informado")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos (erro de validação)")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID fornecido")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO dto, @PathVariable Long id){
        return ResponseEntity.ok().body(userService.updateUser(dto, id));
    }


    @DeleteMapping("/{email}")
    @Operation(summary = "Deleta usuário por email", description = "Método para deletar usuário por email")
    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<Void> deleteUser(@PathVariable String email){
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
