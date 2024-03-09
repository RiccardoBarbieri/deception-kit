package com.deceptionkit.dockerfile.utils;

public enum PortProtocols {

    TCP("tcp"),
    UDP("udp");

    private final String protocol;

    PortProtocols(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }
}
