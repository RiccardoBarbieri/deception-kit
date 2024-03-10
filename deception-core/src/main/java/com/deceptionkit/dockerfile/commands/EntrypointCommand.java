package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.utils.CommandUtils;

import java.util.List;

public class EntrypointCommand extends Command {

    public static final String COMMAND = "ENTRYPOINT";

    private String executable;
    private List<String> args;
    private Boolean spaced = false;

    protected EntrypointCommand() {
        args = new java.util.ArrayList<>();
    }

    public EntrypointCommand entrypoint(String executable, List<String> args) {
        this.executable = executable;
        if (executable.contains(" ")) {
            spaced = true;
        }
        this.args = args;
        for (String arg : args) {
            if (arg.contains(" ")) {
                spaced = true;
                break;
            }
        }
        return this;
    }

    public EntrypointCommand executable(String executable) {
        this.executable = executable;
        if (executable.contains(" ")) {
            spaced = true;
        }
        return this;
    }

    public EntrypointCommand args(List<String> args) {
        this.args = args;
        for (String arg : args) {
            if (arg.contains(" ")) {
                spaced = true;
                break;
            }
        }
        return this;
    }

    @Override
    public String build() {
        if (executable == null || executable.isEmpty()) {
            throw new IllegalArgumentException("ENTRYPOINT command requires an executable");
        }

        return CommandUtils.argsShellOrSpaced(COMMAND, executable, args, spaced, null);
    }
}
