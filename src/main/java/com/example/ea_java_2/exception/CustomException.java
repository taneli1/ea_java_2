package com.example.ea_java_2.exception;

/**
 * A base exception used for the application exceptions.
 */
public class CustomException extends Exception {
    public CustomException(String msg) {
        super(msg);
    }
}
