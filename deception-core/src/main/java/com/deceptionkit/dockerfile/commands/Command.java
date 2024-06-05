package com.deceptionkit.dockerfile.commands;

public abstract class Command {
    
    public static final String COMMAND = "";

    public Command() {
    }

    public abstract String build();
}
