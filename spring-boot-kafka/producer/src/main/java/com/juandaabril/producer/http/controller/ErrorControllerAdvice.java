package com.juandaabril.producer.http.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleError(MethodArgumentNotValidException exception) {
        List<FieldError> errorList = exception.getBindingResult().getFieldErrors();
        Map<String, String> errors = new HashMap<>();

        for(FieldError fieldError: errorList) {
            errors.put( fieldError.getField(), fieldError.getDefaultMessage());
        }

        logger.info("errorMessage: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
