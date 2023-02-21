package com.example.ea_java_2.exception;

import com.example.ea_java_2.models.Customer;

public class CustomException extends Exception {
    public CustomException(String msg) {
        super(msg);
    }
}
