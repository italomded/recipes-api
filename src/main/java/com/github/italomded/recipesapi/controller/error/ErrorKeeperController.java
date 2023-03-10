package com.github.italomded.recipesapi.controller.error;

import com.github.italomded.recipesapi.dto.error.ErrorResponseDTO;
import com.github.italomded.recipesapi.dto.error.SimpleErrorResponseDTO;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @ExceptionHandler(value = {EntityNotFoundException.class, PropertyReferenceException.class})
    public ResponseEntity error404Many(Exception exception) {
        String message = exception.getMessage();

        Optional<String> optionalFullyQualifiedName = Arrays.stream(message.split(" "))
                .filter(s -> s.contains("com.github.italomded"))
                .findAny();

        if (optionalFullyQualifiedName.isPresent()) {
            try {
                String fullyQualifiedName = optionalFullyQualifiedName.get();
                String classSimpleName = Class.forName(fullyQualifiedName).getSimpleName();
                message = message.replace(fullyQualifiedName, classSimpleName);
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleErrorResponseDTO(message));
    }
}
