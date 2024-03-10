package com.deceptionkit.dockerfile.commands;

public class OnbuildCommand extends Command {

    public static final String COMMAND = "ONBUILD";

    //TODO: Implement OnbuildCommand, nested with command? like:
    private Command command;

    @Override
    public String build() {
        return "";
    }
}
