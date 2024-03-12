package com.deceptionkit.dockerfile.commands;

public class EnvCommand extends Command {

    public static final String COMMAND = "ENV";

    private String key;
    private String value;

    public EnvCommand env(String key, String value) {
        this.key = key;
        this.value = value;
        return this;
    }

    public EnvCommand key(String key) {
        this.key = key;
        return this;
    }

    public EnvCommand value(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String build() {
        if (key == null) {
            throw new IllegalArgumentException("ENV command requires a key");
        }
        if (key.contains(" ")) {
            throw new IllegalArgumentException("ENV key cannot contain spaces");
        }
        if (value == null) {
            throw new IllegalArgumentException("ENV command requires a value");
        }
        if (value.contains(" ")) {
            throw new IllegalArgumentException("ENV value cannot contain spaces");
        }

        return COMMAND + " " + key + "=" + value;
    }
}
