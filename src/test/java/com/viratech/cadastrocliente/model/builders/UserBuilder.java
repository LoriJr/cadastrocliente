package com.viratech.cadastrocliente.model.builders;

import com.viratech.cadastrocliente.model.entity.Address;
import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.entity.UserVerificationToken;
import com.viratech.cadastrocliente.model.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserBuilder {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String cpf;
    private String rg;
    private LocalDate birthDate;
    private Address address;
    private LocalDateTime createdAt;
    private UserVerificationToken verificationToken;
    private UserCredential userCredential;
    private UserStatus userStatus;
    private UserBuilder(){}

    public static UserBuilder aUser(){
        UserBuilder builder = new UserBuilder();
        setDefaultValues(builder);
        return builder;
    }

    private static void setDefaultValues(UserBuilder builder) {
        builder.id = 1L;
        builder.name = "Usuario Valido";
        builder.email = "email@email";
        builder.phone = "11911112222";
        builder.cpf = "32112345678";
        builder.rg = "424284251";
        builder.birthDate = LocalDate.of(1990, 1, 1);
        builder.address = AddressBuilder.aAddress().now();
        builder.createdAt = LocalDateTime.now();
        builder.verificationToken = null;
        builder.userCredential = UserCredentialBuilder.aUserCredential().now(); //TODO adicionar builder
        builder.userStatus = UserStatus.PENDING_VERIFICATION;
    }

    public UserBuilder id(Long param){
        id = param;
        return this;
    }

    public UserBuilder name(String param){
        name = param;
        return this;
    }

    public UserBuilder email(String param){
        email = param;
        return this;
    }

    public UserBuilder cpf(String param){
        cpf = param;
        return this;
    }
    public UserBuilder rg(String param){
        rg = param;
        return this;
    }
    public UserBuilder phone(String param){
        phone = param;
        return this;
    }

    public UserBuilder birthDate(LocalDate param){
        birthDate = param;
        return this;
    }

    public UserBuilder createdAt(LocalDateTime param){
        createdAt = param;
        return this;
    }

    public UserBuilder address(Address param){
        address = param;
        return this;
    }

    public UserBuilder verificationToken(UserVerificationToken param){
        verificationToken = param;
        return this;
    }

    public UserBuilder userCredential(UserCredential param){
        userCredential = param;
        return this;
    }

    public UserBuilder status(UserStatus param){
        userStatus = param;
        return this;
    }

    public User now(){
        return new User(
                id, name, email, phone, cpf, rg, birthDate, createdAt, address, userCredential, verificationToken, userStatus
        );
    }
}
