package com.deceptionkit.dockerfile.options.types;

public enum RunSecurityOptionTypes {

    SANDBOX("sandbox"),
    INSECURE("insecure");

    private final String type;

    RunSecurityOptionTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
