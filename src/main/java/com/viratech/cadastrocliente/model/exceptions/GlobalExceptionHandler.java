package com.viratech.cadastrocliente.model.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    private static final Map<String, ApiResponseError.ObjectError> CONSTRAINTS_MAPPING = Map.of(
            "uk_user_email", new ApiResponseError.ObjectError("email", "error.email.violation"),
            "uk_user_cpf", new ApiResponseError.ObjectError("cpf", "error.cpf.violation"),
            "uk_user_rg", new ApiResponseError.ObjectError("rg", "error.rg.violation")
    );

    public static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseError> handlerResourceNotFoundException(ResourceNotFoundException ex){

        log.warn("[NOT_FOUND] Resource not found {}.", ex.getMessage());

        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), null);
    }

    // Erros de constraint (Service/URL)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseError> handlerConstraintViolationException(ConstraintViolationException ex){

        log.error("[DATABASE_ERROR] Integrity violation: {}", ex.getMessage());

        List<ApiResponseError.ObjectError> errors = ex.getConstraintViolations().stream()
                .map(v -> new ApiResponseError.ObjectError(v.getPropertyPath().toString(), v.getMessage()))
                .toList();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Parameters", "Constraint violation in submitted data", errors);
    }

    // Erros de DTO no Controller
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ApiResponseError.ObjectError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(m -> new ApiResponseError.ObjectError(m.getField(), m.getDefaultMessage()))
                .toList();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Parameters", "Constraint violation in submitted data", errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseError> handlerDataIntegrityViolationException(DataIntegrityViolationException ex, Locale locale){

        String rootMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "";

        List<ApiResponseError.ObjectError> errors = CONSTRAINTS_MAPPING.entrySet().stream()
                .filter(entry ->  rootMessage.contains(entry.getKey()))
                .map(entry -> {
                    String translatedMessage = messageSource.getMessage(
                            entry.getValue().userMessage(),
                            null,
                            locale
                    );
                    return new ApiResponseError.ObjectError(entry.getValue().name(), translatedMessage);
                })
                .toList();

        return buildErrorResponse(HttpStatus.CONFLICT, "Data conflicts", "Violation error", errors);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ApiResponseError> handleCustomValidationException(CustomValidationException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "Validation Error",
                "One or more fields are already registered",
                ex.getErrors()
        );
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
