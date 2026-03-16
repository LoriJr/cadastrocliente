package com.viratech.cadastrocliente.model.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String email){
        super("Não foi encontrado usuário com o e-mail: " + email);
    }
}
