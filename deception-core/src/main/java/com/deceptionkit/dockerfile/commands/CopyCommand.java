package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.utils.CommandUtils;

public class CopyCommand extends CommandWithOptions {

    public static final String COMMAND = "COPY";

    private String src;
    private String dest;

    public CopyCommand add(String source, String destination) {
        this.src = source;
        this.dest = destination;
        return this;
    }

    public CopyCommand src(String source) {
        this.src = source;
        return this;
    }

    public CopyCommand dest(String destination) {
        this.dest = destination;
        return this;
    }

    @Override
    public String build() {
        if (src == null || dest == null) {
            throw new IllegalArgumentException("COPY command requires a source and destination");
        }
        return CommandUtils.coupleShellOrSpaced(COMMAND, src, dest, options);
    }
}
