package com.deceptionkit.dockerfile.options;

import java.util.List;

public abstract class CommandOptions {

    protected final List<String> options;

    public CommandOptions() {
        this.options = new java.util.ArrayList<>();
    }

    public abstract String build();
}
