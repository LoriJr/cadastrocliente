package com.viratech.cadastrocliente.model.exceptions;

import java.util.ArrayList;
import java.util.List;

public class CustomValidationException extends RuntimeException {

    List<ApiResponseError.ObjectError> errors;

    public CustomValidationException(List<ApiResponseError.ObjectError>errors){
        super("Validation failed with " + errors.size() + " errors");
        this.errors = errors;
    }

    public List<ApiResponseError.ObjectError> getErrors() {
        return errors;
    }
}
