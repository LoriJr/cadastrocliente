package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.dto.UserRequestDTO;
import com.viratech.cadastrocliente.dto.UserResponseDTO;
import com.viratech.cadastrocliente.dto.UserRoleProjection;
import com.viratech.cadastrocliente.dto.UserRoleResponseDTO;
import com.viratech.cadastrocliente.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento dos usuários da aplicação. * * <p>Disponibiliza endpoints para cadastro, consulta, atualização, remoção * e paginação de usuários, além de consultas relacionadas aos perfis * (roles) associados às credenciais dos usuários.</p> * * <p>As operações utilizam DTOs para entrada e saída de dados, evitando * a exposição direta das entidades de domínio através da API.</p> * * @author Lou Junior * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
@Tag(name = "Usuários", description = "Operações relacionadas ao gerenciamento de usuários")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    /**
     * Retorna todos os usuários cadastrados. * * @return lista de usuários cadastrados
     */
    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna todos os usuários cadastrados no sistema.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))), @ApiResponse(responseCode = "500", description = "Erro interno no servidor")})
    public ResponseEntity<List<UserResponseDTO>> findUsers() {
        log.info("[findUsers] Starting user listing");
        return ResponseEntity.ok(userService.findAllUsers());
    }

    /**
     * Retorna os usuários de forma paginada. * * <p>O parâmetro {@code page} começa em zero. Por exemplo, * {@code page=0&size=20} retorna os primeiros 20 registros.</p> * * @param pageable parâmetros de paginação e ordenação * @return página contendo os usuários encontrados
     */
    @GetMapping("/page")
    @Operation(summary = "Lista usuários de forma paginada", description = "Retorna os usuários utilizando paginação e ordenação.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Página de usuários retornada com sucesso"), @ApiResponse(responseCode = "500", description = "Erro interno no servidor")})
    public Page<UserResponseDTO> getAllUsersPage(@ParameterObject Pageable pageable) {
        return userService.getAllUsersPage(pageable);
    }

    /**
     * Retorna os usuários de forma paginada juntamente com suas roles. * * @param pageable parâmetros de paginação e ordenação * @return página contendo os usuários e suas respectivas roles
     */
    @GetMapping("/role")
    @Operation(summary = "Lista usuários com suas roles", description = "Retorna uma página de usuários contendo as roles associadas a cada usuário.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuários e roles retornados com sucesso"), @ApiResponse(responseCode = "500", description = "Erro interno no servidor")})
    public Page<UserRoleResponseDTO> getAllUsersRolePage(@ParameterObject Pageable pageable) {
        return userService.getUsersPage(pageable);
    }

    /**
     * Retorna uma projeção paginada dos usuários e suas roles. * * <p>Este endpoint utiliza uma projeção para retornar somente os campos * necessários da consulta, evitando o carregamento completo das entidades.</p> * * @param pageable parâmetros de paginação e ordenação * @return página contendo os dados projetados dos usuários e suas roles
     */
    @GetMapping("/role-projection")
    @Operation(summary = "Consulta usuários utilizando Projection", description = "Retorna dados específicos dos usuários e suas roles utilizando uma projeção.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Projeção retornada com sucesso"), @ApiResponse(responseCode = "500", description = "Erro interno no servidor")})
    public Page<UserRoleProjection> getAllUsersRoleProjectionPage(@ParameterObject Pageable pageable) {
        return userService.getUserRoleProjection(pageable);
    }

    /**
     * Cadastra um novo usuário. * * <p>Após o cadastro dos dados do usuário, o fluxo de confirmação * de cadastro é iniciado através do envio do e-mail de verificação.</p> * * @param requestDTO dados do usuário que será cadastrado * @return usuário cadastrado e sua localização através do header Location * @throws MessagingException caso ocorra um erro no envio do e-mail
     */
    @PostMapping
    @Operation(summary = "Cadastra um novo usuário", description = "Realiza o cadastro dos dados de um novo usuário e inicia o processo de confirmação do cadastro.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"), @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"), @ApiResponse(responseCode = "409", description = "E-mail, CPF ou RG já cadastrado"), @ApiResponse(responseCode = "500", description = "Erro interno no servidor")})
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody @Valid UserRequestDTO requestDTO) throws MessagingException {
        String className = UserController.class.getSimpleName();
        log.info("[{}] Start flow [saveUser]", className);
        UserResponseDTO userResponseDTO = userService.userSave(requestDTO, null);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userResponseDTO.id()).toUri();
        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    /**
     * Busca um usuário através do endereço de e-mail. * * @param email endereço de e-mail do usuário * @return dados do usuário encontrado
     */
    @GetMapping("/{email}")
    @Operation(summary = "Consulta usuário por e-mail", description = "Retorna os dados de um usuário utilizando o endereço de e-mail.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuário encontrado"), @ApiResponse(responseCode = "400", description = "Formato de e-mail inválido"), @ApiResponse(responseCode = "404", description = "Usuário não encontrado"), @ApiResponse(responseCode = "500", description = "Erro interno no servidor")})
    public ResponseEntity<UserResponseDTO> findUser(@Parameter(description = "Endereço de e-mail do usuário", required = true, example = "usuario@email.com") @PathVariable @Email(message = "The email format is invalid") String email) {
        log.info("[findUser] Finding user by email: {}", email);
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    /**
     * Atualiza parcialmente os dados de um usuário. * * @param dto dados que serão atualizados * @param id identificador do usuário * @return dados atualizados do usuário
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza um usuário", description = "Atualiza os dados de um usuário e seu endereço com base no ID informado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"), @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"), @ApiResponse(responseCode = "404", description = "Usuário não encontrado"), @ApiResponse(responseCode = "500", description = "Erro interno no servidor")})
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Valid UserRequestDTO dto, @Parameter(description = "ID do usuário", required = true, example = "200025") @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(dto, id));
    }

    /**
     * Remove um usuário através do endereço de e-mail. * * @param email endereço de e-mail do usuário que será removido * @return resposta sem conteúdo após a exclusão
     */
    @DeleteMapping("/{email}")
    @Operation(summary = "Remove usuário por e-mail", description = "Remove um usuário do sistema utilizando seu endereço de e-mail.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"), @ApiResponse(responseCode = "400", description = "Formato de e-mail inválido"), @ApiResponse(responseCode = "404", description = "Usuário não encontrado"), @ApiResponse(responseCode = "500", description = "Erro interno no servidor")})
    public ResponseEntity<Void> deleteUser(@Parameter(description = "Endereço de e-mail do usuário", required = true, example = "usuario@email.com") @PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}