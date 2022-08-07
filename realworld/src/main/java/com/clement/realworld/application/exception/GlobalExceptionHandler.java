package com.clement.realworld.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        for(FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            response.append(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericErrorResponse> handleException(Exception e) {
        return new ResponseEntity<>(new GenericErrorResponse("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
