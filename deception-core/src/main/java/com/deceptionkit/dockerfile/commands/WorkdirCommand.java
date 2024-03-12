package com.deceptionkit.dockerfile.commands;

public class WorkdirCommand extends Command {

    public static final String COMMAND = "WORKDIR";

    private String path;
    private boolean spaced = false;

    protected WorkdirCommand() {
    }

    public WorkdirCommand path(String path) {
        this.path = path;
        if (path.contains(" ")) {
            spaced = true;
        }
        return this;
    }

    @Override
    public String build() {
        if (path == null) {
            throw new IllegalArgumentException("WORKDIR command must have a path");
        }
        String line;
        if (spaced) {
            line = COMMAND + " " + "[\"" + path + "\"]";
        }
        else {
            line = COMMAND + " " + path;
        }
        return line;
    }
}
