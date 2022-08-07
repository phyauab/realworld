package com.clement.realworld.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericErrorResponse> handleException(Exception e) {
        return new ResponseEntity<>(new GenericErrorResponse("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
