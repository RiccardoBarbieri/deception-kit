package com.deceptionkit.dockerfile.options;

public class FromCommandOptions extends CommandOptions {

    protected static final String PLATFORM = "--platform";

    public FromCommandOptions platform(String platform) {
        this.options.add(FromCommandOptions.PLATFORM + "=" + platform);
        return this;
    }
}
