package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserRequestDTO;
import com.viratech.cadastrocliente.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.builders.UserResponseDtoBuilder;
import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.model.enums.UserStatus;
import com.viratech.cadastrocliente.model.mapper.AddressMapper;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.viratech.cadastrocliente.model.builders.UserRequestDtoBuilder.aUserRequestDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private UserService service;

    @Test
    @DisplayName("Deve salvar o usuário no banco")
    void shouldSaveUser() throws MessagingException {

        UserRequestDTO request = aUserRequestDTO().now();
        UserResponseDTO response = UserResponseDtoBuilder.umUserResponseDTO().now();

        User user = new User();

        when(repository.findConflicts(
                request.email(),
                request.cpf(),
                request.rg()
        )).thenReturn(Collections.emptyList());

        when(userMapper.toEntity(request))
                .thenReturn(user);

        when(repository.save(user))
                .thenReturn(user);

        when(userMapper.toResponseDTO(user))
                .thenReturn(response);

        service.userSave(request, Locale.of("pt", "BR"));

        verify(repository).save(user);
    }

    @Test
    @DisplayName("Deve definir o status PENDING_VERIFICATION")
    void shouldSetPendingVerificationStatus() throws MessagingException {

        UserRequestDTO request = aUserRequestDTO().now();
        UserResponseDTO response = UserResponseDtoBuilder.umUserResponseDTO().now();

        User user = new User();

        when(repository.findConflicts(
                request.email(),
                request.cpf(),
                request.rg()
        )).thenReturn(List.of());

        when(userMapper.toEntity(request))
                .thenReturn(user);

        when(repository.save(user))
                .thenReturn(user);

        when(userMapper.toResponseDTO(user))
                .thenReturn(response);

        service.userSave(request, Locale.US);

        assertEquals(UserStatus.PENDING_VERIFICATION, user.getUserStatus());
    }
}
