package com.deceptionkit.spring.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleResponse {


    Integer status;
    String message;

    public SimpleResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
