package com.springboot.librarymanagement.config;


import com.springboot.librarymanagement.dto.response.ErrorDto;
import com.springboot.librarymanagement.exception.OwnershipException;
import com.springboot.librarymanagement.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger log = LoggerFactory.getLogger("GlobalExceptioHandler.class");

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException e){
        log.warn("ID not found in DB");
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage(), "ID not found in DB", Instant.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ){
        Map<String, String> map = new HashMap<>();

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> list = bindingResult.getFieldErrors();
        list.forEach(fieldError -> {
                    map.put(fieldError.getField(), fieldError.getDefaultMessage());
                    log.warn("Field {} , failed validation: {}", fieldError.getField(),fieldError.getDefaultMessage());
                }
        );

        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(OwnershipException.class)
    public ResponseEntity<ErrorDto> handleOwnershipException(OwnershipException e){
        return ResponseEntity.badRequest().body(
                new ErrorDto(e.getMessage(), "Logged-in user cannot perform this operation", Instant.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(
            Exception e
    ){
        return ResponseEntity.badRequest().body(
                new ErrorDto(e.getMessage(), "Invalid Data passed", Instant.now())
        );
    }
}
