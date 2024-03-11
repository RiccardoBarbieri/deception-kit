package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.utils.CommandUtils;

import java.util.List;

public class RunCommand extends CommandWithOptions {

    public static final String COMMAND = "RUN";

    private String command;
    private String executable;
    private List<String> args;
    private Boolean spaced = false;

    protected RunCommand() {
        args = new java.util.ArrayList<>();
    }

    public RunCommand command(String command) {
        this.command = command;
        return this;
    }

    public RunCommand executable(String executable) {
        this.command = executable;
        if (executable.contains(" ")) {
            spaced = true;
        }
        return this;
    }

    public RunCommand args(List<String> args) {
        this.args = args;
        for (String arg : args) {
            if (arg.contains(" ")) {
                spaced = true;
                break;
            }
        }
        return this;
    }

    public RunCommand arg(String arg) {
        this.args.add(arg);
        if (arg.contains(" ")) {
            spaced = true;
        }
        return this;
    }

    @Override
    public String build() {
        if (command == null && executable == null) {
            throw new IllegalArgumentException("RUN command must have a full command or an executable");
        }
        if (command != null && executable != null) {
            throw new IllegalArgumentException("RUN command cannot have both a full command and an executable");
        }
        StringBuilder lineBuilder = new StringBuilder();

        if (command != null) {
            lineBuilder.append(COMMAND).append(" ");
            lineBuilder.append(command);
        } else {
            lineBuilder.append(CommandUtils.argsShellOrSpaced(COMMAND, executable, args, spaced, options));
        }
        return lineBuilder.toString();
    }
}
