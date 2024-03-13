package com.deceptionkit.spring.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    Integer status;
    String message;

    public ErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
