package com.viratech.cadastrocliente.model.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String email){
        super("Resource not found for the provided email: " + email);
    }
}
