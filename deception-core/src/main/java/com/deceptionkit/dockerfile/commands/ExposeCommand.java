package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.options.PortProtocols;

public class ExposeCommand extends Command {

    private static final String COMMAND = "EXPOSE";

    private Integer port;
    private PortProtocols protocol;

    public ExposeCommand expose(int port) {
        this.port = port;
        return this;
    }

    public ExposeCommand protocol(PortProtocols protocol) {
        this.protocol = protocol;
        return this;
    }

    @Override
    public String build() {
        if (port == null) {
            throw new IllegalArgumentException("EXPOSE command requires a port");
        }
        if (port < 1) {
            throw new IllegalArgumentException("EXPOSE command requires a port");
        }
        if (protocol != null) {
            return COMMAND + " " + port + "/" + protocol;
        }
        return COMMAND + " " + port;
    }
}
