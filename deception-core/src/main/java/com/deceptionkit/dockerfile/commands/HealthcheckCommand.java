package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.utils.CommandUtils;

import java.util.List;

public class HealthcheckCommand extends CommandWithOptions {

    public static final String COMMAND = "HEALTHCHECK";

    private String executable;
    private List<String> args;
    private Boolean spaced = false;
    private Boolean none = false;

    protected HealthcheckCommand() {
        args = new java.util.ArrayList<>();
    }

    public HealthcheckCommand healthcheck(String executable, List<String> args) {
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

    public HealthcheckCommand executable(String executable) {
        this.executable = executable;
        if (executable.contains(" ")) {
            spaced = true;
        }
        return this;
    }

    public HealthcheckCommand args(List<String> args) {
        this.args = args;
        for (String arg : args) {
            if (arg.contains(" ")) {
                spaced = true;
                break;
            }
        }
        return this;
    }

    public HealthcheckCommand arg(String arg) {
        this.args.add(arg);
        if (arg.contains(" ")) {
            spaced = true;
        }
        return this;
    }

    public HealthcheckCommand none() {
        none = true;
        return this;
    }

    @Override
    public String build() {
        if (executable == null) {
            throw new IllegalStateException("HEALTHCHECK command requires an executable");
        }
        if (none) {
            return COMMAND + " NONE";
        }
        return CommandUtils.argsShellOrSpacedInter(COMMAND, "CMD", executable, args, spaced, options);

    }
}
