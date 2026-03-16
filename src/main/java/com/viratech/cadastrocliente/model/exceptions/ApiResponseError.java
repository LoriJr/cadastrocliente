package com.viratech.cadastrocliente.model.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseError(
        int status,
        LocalDateTime timestamp,
        String title,
        String detail,
        List<ObjectError> errors
) {
    public record ObjectError(String name, String userMessage){}
}
