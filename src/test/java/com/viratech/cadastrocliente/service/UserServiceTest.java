package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.AddressDTO;
import com.viratech.cadastrocliente.dto.UserRequestDTO;
import com.viratech.cadastrocliente.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.builders.AddressBuilder;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        UserRequestDTO request = createRequest();

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
                .thenReturn(createResponse());

        service.userSave(request, Locale.of("pt", "BR"));

        verify(repository).save(user);
    }

    @Test
    @DisplayName("Deve definir o status PENDING_VERIFICATION")
    void shouldSetPendingVerificationStatus() throws MessagingException {

        UserRequestDTO request = createRequest();

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
                .thenReturn(createResponse());

        service.userSave(request, Locale.US);

        assertEquals(UserStatus.PENDING_VERIFICATION, user.getUserStatus());
    }

    private UserRequestDTO createRequest() {

        AddressDTO dto = addressMapper.toDTO(AddressBuilder.aAddress().now());


        return new UserRequestDTO(
                "Junior Oliveira",
                "junior@email.com",
                "(11) 99999-9999",
                "32260000800",
                "424188661",
                LocalDate.of(1985, 1, 29),
                dto
        );
    }

    private UserResponseDTO createResponse() {

        AddressDTO address = new AddressDTO(
                "09781220",
                "Rua Tiradentes",
                "1963",
                "Bloco 4 ap 31",
                "Ferrazópolis",
                "São Bernardo do Campo",
                "SP"
        );

        return new UserResponseDTO(
                1L,
                "Junior Oliveira",
                "junior@email.com",
                "32260000800",
                "424188661",
                LocalDate.of(1985, 1, 29),
                address,
                LocalDateTime.now()
        );

    }

}
