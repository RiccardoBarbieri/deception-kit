package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.utils.CommandUtils;

import java.util.List;

public class CmdCommand extends Command {

    public static final String COMMAND = "CMD";

    private String executable;
    private List<String> args;
    private Boolean spaced = false;

    protected CmdCommand() {
        args = new java.util.ArrayList<>();
    }

    public CmdCommand cmd(String executable, List<String> args) {
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

    public CmdCommand executable(String executable) {
        this.executable = executable;
        if (executable.contains(" ")) {
            spaced = true;
        }
        return this;
    }

    public CmdCommand args(List<String> args) {
        this.args = args;
        for (String arg : args) {
            if (arg.contains(" ")) {
                spaced = true;
                break;
            }
        }
        return this;
    }

    public CmdCommand arg(String arg) {
        this.args.add(arg);
        if (arg.contains(" ")) {
            spaced = true;
        }
        return this;
    }

    @Override
    public String build() {
        if (executable == null) {
            throw new IllegalStateException("CMD command requires an executable");
        }
        return CommandUtils.argsShellOrSpaced(COMMAND, executable, args, spaced, null);
    }
}
