package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserRequestDTO;
import com.viratech.cadastrocliente.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.model.enums.UserStatus;
import com.viratech.cadastrocliente.model.exceptions.CustomValidationException;
import com.viratech.cadastrocliente.model.mapper.AddressMapper;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.*;
import java.util.stream.Stream;

import static com.viratech.cadastrocliente.model.builders.UserBuilder.aUser;
import static com.viratech.cadastrocliente.model.builders.UserRequestDtoBuilder.aUserRequestDTO;
import static com.viratech.cadastrocliente.model.builders.UserResponseDtoBuilder.aUserResponseDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private Validator validator;

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

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve salvar o usuário no banco")
    public void shouldSaveUser() throws MessagingException {

        UserRequestDTO request = aUserRequestDTO().now();
        UserResponseDTO response = aUserResponseDTO().now();

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
        UserResponseDTO response = aUserResponseDTO().now();

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

        UserResponseDTO userReponse1 = aUserResponseDTO().now();
        UserResponseDTO userReponse2 = aUserResponseDTO().now();
        UserResponseDTO userReponse3 = aUserResponseDTO().now();

        List<UserResponseDTO> usersResponse = List.of(userReponse1, userReponse2, userReponse3);

        when(userMapper.toListUserResponseDTO(users)).thenReturn(usersResponse);
        when(repository.findAll()).thenReturn(users);

        List<UserResponseDTO> result = service.findAllUsers();

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsAnyElementsOf(usersResponse);

        verify(repository, times(1)).findAll();
        verify(repository).findAll();
        verify(userMapper).toListUserResponseDTO(users);
    }

    @Test
    @DisplayName("Deve consultar usuário usando email")
    public void shouldFindUserByEmail(){

        String email = "usuario@gmail.com";
        User user = aUser().email(email).now();

        UserResponseDTO response = aUserResponseDTO().now();

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        when(userMapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO result = service.findUserByEmail(email);

        assertThat(result).isNotNull().isEqualTo(response);

        verify(repository, times(1)).findByEmail(email);

    }

    @ParameterizedTest(name = "{1}")
    @CsvSource(textBlock = """
                    '', 'espaço vazio'
                    ' ', 'espaço em branco'
                    NULL, 'nulo'
                    """, nullValues = "NULL"
    )
    public void shouldThrowExceptionWhenEmailIsEmpty(String email, String message){

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                ()-> service.deleteUserByEmail(email));

        assertEquals("Email can not be empty", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado por e-mail")
    public void shoulThrowdExceptionWhenUserNotFoundByEmail(){

        String email = "emailInexistente@gmail.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                ()-> service.deleteUserByEmail(email));

        assertThat(ex).isNotNull();

        assertEquals("User not found for this email: " + email, ex.getMessage());

        verify(repository, times(1)).findByEmail(email);
        verify(userMapper, never()).toResponseDTO(any(User.class));
    }

    @Test
    public void shoudToDeleteUserByEmail(){



    }

    @Test
    public void shouldThrowExceptionWhenIdNotFound(){

        Long invalidId = 200L;

        User user = aUser().id(invalidId).now();
        UserRequestDTO requestDTO = aUserRequestDTO().now();

        when(repository.findById(user.getId())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                ()-> service.updateUser(requestDTO, user.getId()));

        assertThat(ex).isNotNull();
        assertEquals("User not found", ex.getMessage());

        verify(repository, times(1)).findById(invalidId);
        verify(userMapper, never()).toResponseDTO(user);
    }

    @Test
    @DisplayName("Deve atualizar dados do usuário")
    public void shouldUpdateUser(){

        String newEmail = "novoEmail@gmail.com";

        UserRequestDTO requestDTO = aUserRequestDTO().email(newEmail).now();
        User user = aUser().now();

        User userUpdated = aUser().email(newEmail).now();
        UserResponseDTO responseDTO = aUserResponseDTO().email(newEmail).now();

        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(userUpdated);
        when(userMapper.toResponseDTO(userUpdated)).thenReturn(responseDTO);

        UserResponseDTO result = service.updateUser(requestDTO, user.getId());

        assertNotNull(result);
        assertEquals(newEmail, result.email());

        verify(repository).findById(user.getId());
        verify(userMapper).updateUser(requestDTO, user);
        verify(repository).save(user);
        verify(userMapper).toResponseDTO(userUpdated);
    }

}
