package com.deceptionkit.dockerfile.commands;

public class UserCommand extends Command {

    public static final String COMMAND = "USER";

    private String user;
    private String group;

    protected UserCommand() {
    }

    public UserCommand user(String user, String group) {
        this.user = user;
        this.group = group;
        return this;
    }

    public UserCommand user(String user) {
        this.user = user;
        return this;
    }

    public UserCommand group(String group) {
        this.group = group;
        return this;
    }

    public UserCommand user(Integer user, Integer group) {
        this.user = user.toString();
        this.group = group.toString();
        return this;
    }

    public UserCommand user(Integer user) {
        this.user = user.toString();
        return this;
    }

    public UserCommand group(Integer group) {
        this.group = group.toString();
        return this;
    }

    @Override
    public String build() {
        if (user == null) {
            throw new IllegalArgumentException("USER command must have a user");
        }
        return COMMAND + " " + user + (group != null ? ":" + group : "");
    }
}
