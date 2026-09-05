package com.viratech.cadastrocliente.dto;

import com.viratech.cadastrocliente.model.builders.AddressBuilder;
import com.viratech.cadastrocliente.model.builders.UserRequestDtoBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.util.Strings;
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
import static com.viratech.cadastrocliente.model.builders.UserRequestDtoBuilder.aUserRequestDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRequesDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve aceitar request válido")
    public void shouldCreateValidUserRequest() {
        UserRequestDTO requestDTO = aUserRequestDTO().now();

        var violations = validator.validate(requestDTO);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest(name = "{6}")
    @MethodSource("blankFieldProvider")
    @DisplayName("Deve Validar Campos @NotBlank")
    public void shouldValidateFieldBlank(String name, String email, String phone, String cpf, String rg, String fieldName, String message) {

        UserRequestDTO requestDTO = aUserRequestDTO()
                .name(name)
                .email(email)
                .phone(phone)
                .cpf(cpf)
                .rg(rg)
                .birthDate(LocalDate.of(1900, 1, 1))
                .address(AddressBuilder.aAddress().nowDTO())
                .now();

        Set<ConstraintViolation<UserRequestDTO>> violations =
                validator.validate(requestDTO);

        assertThat(violations)
                        .anyMatch(v ->
                                v.getPropertyPath().toString().equals(fieldName)
        );
    }

    private static Stream<Arguments> blankFieldProvider() {

        return Stream.of(
                Arguments.of(null, "usuario@email.com", "11911112222", "52998224725", "424214181","name", "name null"),
                Arguments.of("", "usuario@email.com", "11911112222", "52998224725", "424214181", "name", "name empty"),
                Arguments.of(" ", "usuario@email.com", "11911112222", "52998224725", "424214181", "name", "name blank"),

                Arguments.of("Usuario Valido", null, "11911112222", "52998224725", "424214181", "email", "email null"),
                Arguments.of("Usuario Valido", "", "11911112222", "52998224725", "424214181", "email", "email empty"),
                Arguments.of("Usuario Valido", " ", "11911112222", "52998224725", "424214181", "email", "email blank"),

                Arguments.of("Usuario Valido", "usuario@email.com", null, "52998224725", "424214181", "phone", "phone null"),
                Arguments.of("Usuario Valido", "usuario@email.com", "", "52998224725", "424214181", "phone", "phone empty"),
                Arguments.of("Usuario Valido", "usuario@email.com", " ", "52998224725", "424214181", "phone", "phone blank"),

                Arguments.of("Usuario Valido", "usuario@email.com", "11911112222", null, "424214181", "cpf", "cpf null"),
                Arguments.of("Usuario Valido", "usuario@email.com", "11911112222", "", "424214181", "cpf", "cpf empty"),
                Arguments.of("Usuario Valido", "usuario@email.com", "11911112222", " ", "424214181", "cpf", "cpf blank"),

                Arguments.of("Usuario Valido", "usuario@email.com", "11911112222", "52998224725", null, "rg", "rg null"),
                Arguments.of("Usuario Valido", "usuario@email.com", "11911112222", "52998224725", "", "rg", "rg empty"),
                Arguments.of("Usuario Valido", "usuario@email.com", "11911112222", "52998224725", " ", "rg", "rg blank")
        );
    }

    @Test
    @DisplayName("Deve validar campos @NotNull")
    public void shouldValidateFieldNull(){

        UserRequestDTO requestDTO = aUserRequestDTO().birthDate(null).now();

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(requestDTO);

        assertThat(violations).anyMatch(v-> v.getPropertyPath().toString().equals("birthDate"));
    }

    @ParameterizedTest(name="{3}")
    @MethodSource("nullFieldProvider")
    @DisplayName("Deve validar campos @NotNull")
    public void shouldValidateNullField(LocalDate birthDate, AddressDTO addressDTO, String fieldName, String message){

         UserRequestDTO requestDTO = aUserRequestDTO().birthDate(birthDate).address(addressDTO).now();

        Set<ConstraintViolation<UserRequestDTO>> violations =
                validator.validate(requestDTO);

        assertThat(violations).anyMatch(v-> v.getPropertyPath().toString().equals(fieldName));
    }

    public static Stream<Arguments> nullFieldProvider(){
        return Stream.of(
                Arguments.of(null, AddressBuilder.aAddress().nowDTO(), "birthDate", "birthDate Null"),
                Arguments.of(LocalDate.of(1900, 1, 1), null, "address", "address Null"));
    }
}



