package com.viratech.cadastrocliente.model.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseError> handlerResourceNotFoundException(ResourceNotFoundException ex){

        log.warn("[NOT_FOUND] Resource not found {}.", ex.getMessage());

        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), null);
    }

    // Erros de constraint (Service/URL)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseError> handlerConstraintViolationException(ConstraintViolationException ex){

        List<ApiResponseError.ObjectError> errors = ex.getConstraintViolations().stream()
                .map(v -> new ApiResponseError.ObjectError(v.getPropertyPath().toString(), v.getMessage()))
                .toList();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Parâmetros inválidos", "Violação de restrição nos dados enviados", errors);
    }

    // Erros de DTO no Controller
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ApiResponseError.ObjectError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(m -> new ApiResponseError.ObjectError(m.getField(), m.getDefaultMessage()))
                .toList();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Parâmetros Inválidos", "Violação de restrição nos dados enviados", errors);
    }


    private ResponseEntity<ApiResponseError> buildErrorResponse(HttpStatus status, String title, String message, List<ApiResponseError.ObjectError> errors){
        ApiResponseError error = new ApiResponseError(
                status.value(),
                LocalDateTime.now(),
                title,
                message,
                errors
        );
        return ResponseEntity.status(status).body(error);
    }


}
