package com.viratech.cadastrocliente.model.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseError> handlerResourceNotFoundException(ResourceNotFoundException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;

        log.info("[ResourceNotFoundException lançada]");
        ApiResponseError error = new ApiResponseError(
                status.value(),
                LocalDateTime.now(),
                "Usuário não encontrado",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(status).body(error);
    }
}
