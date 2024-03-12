package com.deceptionkit.dockerfile.options;

public class CommandOptionsBuilder {

    private CommandOptionsBuilder() {
    }

    public static AddCommandOptions add() {
        return new AddCommandOptions();
    }

    public static CopyCommandOptions copy() {
        return new CopyCommandOptions();
    }

    public static FromCommandOptions from() {
        return new FromCommandOptions();
    }

    public static HealthcheckCommandOptions healthcheck() {
        return new HealthcheckCommandOptions();
    }

    public static RunCommandOptions run() {
        return new RunCommandOptions();
    }

}
