package com.maverickbank.config;


import com.maverickbank.dto.response.ErrorDto;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.OwnershipException;
import com.maverickbank.exception.ResourceNotFoundException;
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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(
            ResourceNotFoundException e
    ){
        //always reply the object not the string

        return ResponseEntity
                .badRequest()
                .body(
                        new ErrorDto(e.getMessage(),
                                "Id not found in DB",
                                Instant.now())
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ){
        Map<String,String> map = new HashMap<>();

        BindingResult bindingResult =  e.getBindingResult();
        List<FieldError> list = bindingResult.getFieldErrors();
        list.forEach(fieldError -> map.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(map );
    }

    @ExceptionHandler(InvalidCallException.class)
    public ResponseEntity<ErrorDto> handleInvalidCallException(
            InvalidCallException e
    ){
        //always reply the object not the string

        return ResponseEntity
                .badRequest()
                .body(
                        new ErrorDto(e.getMessage(),
                                "Call is Invalid due to malformed data",
                                Instant.now())
                );
    }

    @ExceptionHandler(OwnershipException.class)
    public ResponseEntity<ErrorDto> handleOwnershipException(
            OwnershipException e
    ){
        //always reply the object not the string

        return ResponseEntity
                .badRequest()
                .body(
                        new ErrorDto(e.getMessage(),
                                "Logged-in User cannot perform this operation",
                                Instant.now())
                );
    }
}
