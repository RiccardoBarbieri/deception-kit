package com.deceptionkit.dockerfile.commands;

public class StopsignalCommand extends Command {

    public static final String COMMAND = "STOPSIGNAL";

    private String signal;

    protected StopsignalCommand() {
    }

    public StopsignalCommand signal(Integer signal) {
        this.signal = signal.toString();
        return this;
    }

    public StopsignalCommand signal(String signal) {
        this.signal = signal;
        return this;
    }

    @Override
    public String build() {
        if (signal == null) {
            throw new IllegalArgumentException("STOPSIGNAL command must have a signal");
        }
        return COMMAND + " " + signal;
    }
}
