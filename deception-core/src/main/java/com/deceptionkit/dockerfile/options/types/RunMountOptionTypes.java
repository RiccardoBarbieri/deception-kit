package com.deceptionkit.dockerfile.options.types;

public enum RunMountOptionTypes {

    BIND("bind"),
    CACHE("cache"),
    TMPFS("tmpfs"),
    SECRET("secret"),
    SSH("ssh");

    private final String type;

    RunMountOptionTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
