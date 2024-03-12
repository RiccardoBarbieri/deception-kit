package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.utils.CommandUtils;

public class AddCommand extends CommandWithOptions {

    public static final String COMMAND = "ADD";

    private String src;
    private String dest;

    public AddCommand add(String source, String destination) {
        this.src = source;
        this.dest = destination;
        return this;
    }

    public AddCommand src(String source) {
        this.src = source;
        return this;
    }

    public AddCommand dest(String destination) {
        this.dest = destination;
        return this;
    }

    @Override
    public String build() {
        if (src == null || dest == null) {
            throw new IllegalArgumentException("Source and destination must be set");
        }
        return CommandUtils.coupleShellOrSpaced(COMMAND, src, dest, options);
    }
}
