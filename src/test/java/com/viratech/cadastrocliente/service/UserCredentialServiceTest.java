package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
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

import static com.viratech.cadastrocliente.model.builders.UserBuilder.aUser;
import static com.viratech.cadastrocliente.model.builders.UserCredentialBuilder.aUserCredential;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
