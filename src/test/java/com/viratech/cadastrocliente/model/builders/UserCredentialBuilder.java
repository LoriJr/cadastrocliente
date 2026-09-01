package com.viratech.cadastrocliente.model.builders;

import com.viratech.cadastrocliente.model.entity.Role;
import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.enums.RoleName;

import java.util.Set;

public class UserCredentialBuilder {

    private Long id;
    private String password;
    private Set<Role> roles;
    private User user;
    private UserCredentialBuilder(){}

    public static UserCredentialBuilder aUserCredential(){
        UserCredentialBuilder builder = new UserCredentialBuilder();
        Role role = new Role();
        role.setRoleName(RoleName.USER);

        builder.id = builder.user.getId();
        builder.roles = Set.of(role);

        builder.user = UserBuilder.aUser().now();

        return builder;
    }

    public UserCredentialBuilder id(Long param){
        id=param;
        return this;
    }

    public UserCredentialBuilder password(String param){
        password =  param;
        return this;
    }

    public UserCredentialBuilder roles(Set<Role> param){
        roles = param;
        return this;
    }

    public UserCredentialBuilder user(User param){
        user = param;
        return this;
    }

    public UserCredential now(){
        return new UserCredential(id, password, roles, user);
    }

}
