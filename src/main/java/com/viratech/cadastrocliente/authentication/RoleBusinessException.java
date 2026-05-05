package com.viratech.cadastrocliente.authentication;

public class RoleBusinessException extends RuntimeException{
    public RoleBusinessException(String message){
        super("Error to create toke JWT!");
    }
}
