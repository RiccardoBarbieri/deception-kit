package com.deceptionkit.dockerfile.commands;

public class OnbuildCommand extends Command {

    public static final String COMMAND = "ONBUILD";

    private Command command;

    public OnbuildCommand onbuild(Command command) {
        this.command = command;
        return this;
    }


    @Override
    public String build() {
        if (command == null) {
            throw new IllegalArgumentException("ONBUILD command must have a command to execute");
        }
        String commandString = command.build();
        String commandName = commandString.split(" ")[0];
        if (commandName.equals(COMMAND) || commandName.equals(FromCommand.COMMAND) || commandName.equals(MaintainerCommand.COMMAND)) {
            throw new IllegalArgumentException("ONBUILD command cannot contain ONBUILD, FROM, or MAINTAINER commands");
        }

        return COMMAND + " " + commandString;
    }
}
