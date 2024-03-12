package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.utils.CommandUtils;

public class ArgCommand extends Command {

    public static final String COMMAND = "ARG";

    private String name;
    private String defaultValue;

    public ArgCommand arg(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        return this;
    }

    public ArgCommand name(String name) {
        this.name = name;
        return this;
    }

    public ArgCommand defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public String build() {
        if (name == null) {
            throw new IllegalArgumentException("ARG command requires a name");
        }
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(COMMAND).append(" ");

        lineBuilder.append(name);

        if (defaultValue != null) {
            lineBuilder.append("=").append(defaultValue);
        }

        return lineBuilder.toString();
    }
}
