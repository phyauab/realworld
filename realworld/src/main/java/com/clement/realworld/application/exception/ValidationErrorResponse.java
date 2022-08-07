package com.clement.realworld.application.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Getter
public class ValidationErrorResponse {

    private Map<String, ArrayList<String>> errors;

    public ValidationErrorResponse() {
        errors = new HashMap<>();
    }

    public void append(String key, String message) {
        ArrayList<String> arrayList = null;
        if(errors.get(key) == null) {
            arrayList = new ArrayList<>();
            arrayList.add(message);
            this.errors.put(key, arrayList);
        } else {
            arrayList = this.errors.get(key);
            arrayList.add(message);
        }
    }

}
