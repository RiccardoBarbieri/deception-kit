package com.deceptionkit.dockerfile.commands;

@Deprecated
public class MaintainerCommand extends Command {

    public static final String COMMAND = "MAINTAINER";

    private String maintainer;

    public MaintainerCommand maintainer(String maintainer) {
        this.maintainer = maintainer;
        return this;
    }

    @Override
    public String build() {
        if (maintainer == null) {
            throw new IllegalArgumentException("MAINTAINER command requires a maintainer");
        }
        return COMMAND + " " + maintainer;
    }
}
