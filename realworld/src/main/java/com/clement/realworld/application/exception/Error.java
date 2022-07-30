package com.clement.realworld.application.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Getter
@JsonTypeName(value = "errors")
@JsonTypeInfo(use = NAME, include = WRAPPER_OBJECT)
public class Error {

    private List<String> body;

    private void append(String message) {
        this.body.add(message);
    }

}
