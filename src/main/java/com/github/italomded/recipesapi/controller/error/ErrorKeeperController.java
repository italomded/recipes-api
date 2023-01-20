package com.github.italomded.recipesapi.controller.error;

import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ErrorKeeperController {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<ErrorResponseDTO>> error400Form(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getFieldErrors();
        List<ErrorResponseDTO> responseBody = fieldErrors.stream().map(ErrorResponseDTO::new).toList();
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler(value = {DataValidationException.class, BusinessRuleException.class})
    public ResponseEntity error400Service(RuntimeException exception) {
        ErrorResponseDTO errorResponseDTO;
        if (exception instanceof DataValidationException) {
            errorResponseDTO = new ErrorResponseDTO((DataValidationException) exception);
        } else {
            errorResponseDTO = new ErrorResponseDTO((BusinessRuleException) exception);
        }
        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity error404Repository() {
        return ResponseEntity.notFound().build();
    }
}
