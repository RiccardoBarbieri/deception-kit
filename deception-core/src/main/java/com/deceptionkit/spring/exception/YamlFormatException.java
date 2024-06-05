package com.deceptionkit.spring.exception;

public class YamlFormatException extends RuntimeException {


    public YamlFormatException(String message) {
        super(message);

    }

    public YamlFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
