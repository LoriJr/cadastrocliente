package com.viratech.cadastrocliente.model.exceptions;

import jakarta.validation.ConstraintViolationException;
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

        ApiResponseError error = new ApiResponseError(
                status.value(),
                LocalDateTime.now(),
                "Resource not found",
                ex.getMessage(),
                null
        );

        log.warn("[NOT_FOUND] Resource not found {}.", ex.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseError> handlerConstraintViolationException(ConstraintViolationException ex){

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getConstraintViolations().iterator().next().getMessage();

        ApiResponseError error = new ApiResponseError(
                status.value(),
                LocalDateTime.now(),
                "Invalid Parameter",
                message,
                null
        );
        return ResponseEntity.status(status).body(error);
    }

}
