package com.viratech.cadastrocliente.dto;

import com.viratech.cadastrocliente.model.builders.AddressBuilder;
import com.viratech.cadastrocliente.model.builders.UserRequestDtoBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static com.viratech.cadastrocliente.model.builders.AddressBuilder.aAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRequesDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() { ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); validator = factory.getValidator(); }

    @ParameterizedTest
    @MethodSource("dataProvider")
    @DisplayName("Deve Validar Campos em Branco")
    public void shouldValidateFieldBlank(String name, String email, String phone, String cpf, String rg, String field) {

        UserRequestDTO requestDTO = UserRequestDtoBuilder.aUserRequestDTO().name(name).email(email).phone(phone).cpf(cpf).rg(rg).birthDate(LocalDate.of(1900, 1, 1)).address(AddressBuilder.aAddress().nowDTO()).now();

        Set<ConstraintViolation<UserRequestDTO>> violations =
                validator.validate(requestDTO);

        assertTrue(
                violations.stream()
                        .anyMatch(v ->
                                v.getPropertyPath().toString().equals(field)
                        )
        );


    }

    private static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(null, "email@email", "11912341234", "12345678912", "121234567", LocalDate.of(1900, 1, 1), aAddress().nowDTO()),
                Arguments.of("Usuario Valido", null, "11912341234", "12345678912", "121234567", LocalDate.of(1900, 1, 1), aAddress().nowDTO()),
                Arguments.of("Usuario Valido", "email@email", null, "12345678912", "121234567", LocalDate.of(1900, 1, 1), aAddress().nowDTO()),
                Arguments.of("Usuario Valido", "email@email", "11912341234", null, "121234567", LocalDate.of(1900, 1, 1), aAddress().nowDTO()),
                Arguments.of("Usuario Valido", "email@email", "11912341234", "12345678912", null, LocalDate.of(1900, 1, 1), aAddress().nowDTO()),
                Arguments.of("Usuario Valido", "email@email", "11912341234", "12345678912", "121234567", null, aAddress().nowDTO()),
                Arguments.of("Usuario Valido", "email@email", "11912341234", "12345678912", "121234567", LocalDate.of(1900, 1, 1), null)
        );

    }

    @Test
    public void shouldValidateNameBlank(){
        UserRequestDTO requestDTO = UserRequestDtoBuilder
                .aUserRequestDTO()
                .name(" ")
                .now();

        var violations = validator.validate(requestDTO);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    @DisplayName("Deve criar usuário com todos os campos válidos")
    public void shouldCreateValidUserRequest(){

        UserRequestDTO requestDTO = UserRequestDtoBuilder.aUserRequestDTO().now();
        var violations = validator.validate(requestDTO);
        assertThat(violations).isEmpty();
    }


}
