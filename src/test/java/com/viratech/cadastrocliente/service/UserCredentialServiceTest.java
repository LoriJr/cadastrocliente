package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
import com.viratech.cadastrocliente.dto.UserRoleRequest;
import com.viratech.cadastrocliente.dto.UserRoleResponse;
import com.viratech.cadastrocliente.model.entity.Role;
import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.enums.RoleName;
import com.viratech.cadastrocliente.model.mapper.UserCredentialMapper;
import com.viratech.cadastrocliente.repository.RoleRepository;
import com.viratech.cadastrocliente.repository.UserCredentialRepository;
import com.viratech.cadastrocliente.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.viratech.cadastrocliente.model.builders.UserBuilder.aUser;
import static com.viratech.cadastrocliente.model.builders.UserCredentialBuilder.aUserCredential;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCredentialServiceTest {

    @Mock
    private UserCredentialRepository credentialRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialMapper credentialMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserCredentialService credentialService;

    @Test
    @DisplayName("Deve salvar credencial com sucesso")
    public void shouldSaveUserCredentialSuccessfully()
            throws MessagingException {

        UserCredentialRequestDTO requestDTO = new UserCredentialRequestDTO("usuario@email.com","123456");

        UserCredentialResponseDTO responseDTO = new UserCredentialResponseDTO("usuario@email.com","123456");

        User user = aUser().now();

        UserCredential userCredential = aUserCredential().now();
        userCredential.setRoles(new HashSet<>());

        Role defaultRole = new Role();

        when(userRepository.findByEmail(requestDTO.email())).thenReturn(Optional.of(user));

        when(credentialRepository.existsByUserEmail(requestDTO.email())).thenReturn(false);

        when(credentialMapper.toEntity(requestDTO)).thenReturn(userCredential);

        doNothing().when(emailService).sendVerificationEmail(user);

        when(roleRepository.findByRoleName(RoleName.USER)).thenReturn(Optional.of(defaultRole));

        when(credentialRepository.save(userCredential)).thenReturn(userCredential);

        when(credentialMapper.toDTO(userCredential)).thenReturn(responseDTO);

        UserCredentialResponseDTO result = credentialService.saveUserCredential(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);
    }

    @Test
    @DisplayName("Deve salvar nova credencial")
    public void shouldSaveNewCredential(){

        UserCredential userCredential = aUserCredential().id(1L).now();

        userCredential.getRoles().forEach(roles -> System.out.println("Role: " + roles.getRoleName()));

        UserRoleRequest request = new UserRoleRequest(RoleName.ADMIN);

        Role adminRole = new Role();
        adminRole.setRoleName(RoleName.ADMIN);

        UserRoleResponse responseDTO =
                new UserRoleResponse(
                        1L,
                        "Usuario Valido",
                        "email@gmail.com",
                        Set.of(RoleName.USER, RoleName.ADMIN)
                        );

        when(credentialRepository.findById(1L)).thenReturn(Optional.of(userCredential));
        when(roleRepository.findByRoleName(RoleName.ADMIN)).thenReturn(Optional.of(adminRole));
        when(credentialMapper.toRoleResponse(userCredential)).thenReturn(responseDTO);

        UserRoleResponse result = credentialService.addRole(1L, request);

        assertNotNull(result);

        assertEquals(2, result.roles().size());
        assertTrue(result.roles().contains(RoleName.ADMIN));
        assertTrue(result.roles().contains(RoleName.USER));

    }


//    @Test
//    @DisplayName("Deve rejeitar lançar exceção para credencial existente")
//    public void shouldThrowException409IfCredentialExists(){
//
//
//    }





}
