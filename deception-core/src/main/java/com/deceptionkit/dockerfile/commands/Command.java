package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.options.CommandOptions;

public abstract class Command {
    
    public static final String COMMAND = "";

    public Command() {
    }

    public abstract String build();
}
