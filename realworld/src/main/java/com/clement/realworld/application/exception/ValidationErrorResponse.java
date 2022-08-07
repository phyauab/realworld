package com.clement.realworld.application.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            this.errors.put(key, arrayList);
        } else {
            arrayList = this.errors.get(key);
        }
            arrayList.add(message);
    }

}
