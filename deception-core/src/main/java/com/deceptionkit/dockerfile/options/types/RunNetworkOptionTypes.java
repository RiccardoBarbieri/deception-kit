package com.deceptionkit.dockerfile.options.types;

public enum RunNetworkOptionTypes {

    DEFAULT("default"),
    NONE("none"),
    HOST("host");

    private final String type;

    RunNetworkOptionTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
