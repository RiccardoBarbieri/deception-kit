package com.deceptionkit.spring.exception;

import com.deceptionkit.spring.yaml.YamlErrorMessageUtils;

public class YamlFormatException extends RuntimeException {


    public YamlFormatException(String message) {
        super(message);

    }

    public YamlFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
