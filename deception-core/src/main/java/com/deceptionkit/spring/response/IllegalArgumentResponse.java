package com.deceptionkit.spring.response;

public class IllegalArgumentResponse extends ErrorResponse {

    String parameter;
    String message;
    String requiredType;
    String providedValue;

    public IllegalArgumentResponse(String message, String parameter, String requiredType, String providedValue) {
        super(message);
        this.parameter = parameter;
        this.message = message;
        this.requiredType = requiredType;
        this.providedValue = providedValue;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequiredType() {
        return requiredType;
    }

    public void setRequiredType(String requiredType) {
        this.requiredType = requiredType;
    }

    public String getProvidedValue() {
        return providedValue;
    }

    public void setProvidedValue(String providedValue) {
        this.providedValue = providedValue;
    }
}
