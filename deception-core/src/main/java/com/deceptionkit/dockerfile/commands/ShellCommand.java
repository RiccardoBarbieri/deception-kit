package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.utils.CommandUtils;

import java.util.List;

public class ShellCommand extends Command {

    public static final String COMMAND = "SHELL";

    private String executable;
    private List<String> args;

    protected ShellCommand() {
        args = new java.util.ArrayList<>();
    }

    public ShellCommand shell(String shell, List<String> args) {
        this.executable = shell;
        return this;
    }

    public ShellCommand args(List<String> args) {
        this.args = args;
        return this;
    }

    public ShellCommand arg(String arg) {
        this.args.add(arg);
        return this;
    }

    @Override
    public String build() {
        if (executable == null) {
            throw new IllegalArgumentException("SHELL command must have an executable");
        }
        return CommandUtils.argsShellOrSpaced(COMMAND, executable, args, true, null);
    }
}
