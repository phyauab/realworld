package com.clement.realworld.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{

    private String field;

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }
}
