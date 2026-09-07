package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserRequestDTO;
import com.viratech.cadastrocliente.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.builders.UserBuilder;
import com.viratech.cadastrocliente.model.builders.UserRequestDtoBuilder;
import com.viratech.cadastrocliente.model.builders.UserResponseDtoBuilder;
import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.model.enums.UserStatus;
import com.viratech.cadastrocliente.model.exceptions.CustomValidationException;
import com.viratech.cadastrocliente.model.mapper.AddressMapper;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static com.viratech.cadastrocliente.model.builders.UserBuilder.aUser;
import static com.viratech.cadastrocliente.model.builders.UserRequestDtoBuilder.aUserRequestDTO;
import static com.viratech.cadastrocliente.model.builders.UserResponseDtoBuilder.umUserResponseDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private UserService service;

    @Test
    @DisplayName("Deve salvar o usuário no banco")
    public void shouldSaveUser() throws MessagingException {

        UserRequestDTO request = aUserRequestDTO().now();
        UserResponseDTO response = umUserResponseDTO().now();

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
    public void shouldSetPendingVerificationStatus() throws MessagingException {

        UserRequestDTO request = aUserRequestDTO().now();
        UserResponseDTO response = umUserResponseDTO().now();

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

    @Test
    @DisplayName("Deve lançar exceção em caso de request Null")
    public void shouldExceptionRequestNull(){

        UserRequestDTO requestDTO = null;

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                ()-> service.userSave(requestDTO, Locale.of("pt", "BR")));

        assertEquals("Request body must not be null", ex.getMessage());
    }

    @ParameterizedTest(name = "{4}")
    @MethodSource("conflictDataProvider")
    @DisplayName("Deve validar dados existentes durante cadastro de usuário")
    public void shouldValidateFieldConflits(String email, String cpf, String rg, String field, String message){

        UserRequestDTO requestDTO = aUserRequestDTO()
                .email(email)
                .cpf(cpf)
                .rg(rg)
                .now();

        User user = aUser().now();

        when(repository.findConflicts(requestDTO.email(), requestDTO.cpf(), requestDTO.rg())).thenReturn(List.of(user));

        CustomValidationException ex = assertThrows(CustomValidationException.class,
                () -> service.userSave(requestDTO, Locale.getDefault()));

        assertEquals(field, ex.getMessage());
    }

    private static Stream<Arguments> conflictDataProvider(){
        return Stream.of(
                Arguments.of("usuario@email.com", "44054049096", "424284252", "Validation failed with 1 errors","e-mail já existe"),
                Arguments.of( "usuario1@email.com", "44054049095", "424284252", "Validation failed with 1 errors", "cpf já existe"),
                Arguments.of( "usuario1@email.com", "44054049096", "424284251", "Validation failed with 1 errors", "rg já existe"),

                Arguments.of( "usuario@email.com", "44054049095", "424284252", "Validation failed with 2 errors", "email e cpf "),
                Arguments.of( "usuario@email.com", "44054049096", "424284251", "Validation failed with 2 errors", "email e rg"),

                Arguments.of( "usuario1@email.com", "44054049095", "424284251", "Validation failed with 2 errors", "rg e cpf"),

                Arguments.of( "usuario@email.com", "44054049095", "424284251", "Validation failed with 3 errors", "rg, cpf e email")
        );
    }

    @Test
    @DisplayName("Deve mostrar a lista de usuários")
    public void shouldListedAllUsers(){

        User user1 = aUser().now();
        User user2 = aUser().now();
        User user3 = aUser().now();

        List<User> users = List.of(user1, user2, user3);

        UserResponseDTO userReponse1 = umUserResponseDTO().now();
        UserResponseDTO userReponse2 = umUserResponseDTO().now();
        UserResponseDTO userReponse3 = umUserResponseDTO().now();

        List<UserResponseDTO> usersResponse = List.of(userReponse1, userReponse2, userReponse3);

        when(userMapper.toListUserResponseDTO(users)).thenReturn(usersResponse);
        when(repository.findAll()).thenReturn(users);

        List<UserResponseDTO> result = service.findAllUsers();

        Assertions.assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsAnyElementsOf(usersResponse);

        verify(repository, times(1)).findAll();
        verify(repository).findAll();
        verify(userMapper).toListUserResponseDTO(users);
    }

}
