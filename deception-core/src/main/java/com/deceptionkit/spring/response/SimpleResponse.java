package com.deceptionkit.spring.response;

public class SimpleResponse {


    Integer status;
    String message;

    public SimpleResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
