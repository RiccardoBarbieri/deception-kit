package com.deceptionkit.model.idprovider;

public class Credential {

    public static final String PASSWORD = "password";

    protected String type;

    protected String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
