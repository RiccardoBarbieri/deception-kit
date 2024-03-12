package com.deceptionkit.dockerfile.options;

import java.util.List;

public class AddCommandOptions extends CommandOptions {

    protected static final String KEEP_GIT_DIR = "--keep-git-dir";
    protected static final String CHECKSUM = "--checksum";
    protected static final String CHOWN = "--chown";
    protected static final String CHMOD = "--chmod";
    protected static final String LINK = "--link";
    protected static final String EXCLUDE = "--exclude";

    protected AddCommandOptions() {
        super();
    }

    public AddCommandOptions keepGitDir() {
        this.options.add(AddCommandOptions.KEEP_GIT_DIR + "=true");
        return this;
    }

    public AddCommandOptions checksum(String checksum) {
        this.options.add(AddCommandOptions.CHECKSUM + "=" + checksum);
        return this;
    }

    public AddCommandOptions chown(String user, String group) {
        this.options.add(AddCommandOptions.CHOWN + "=" + user + ":" + group);
        return this;
    }

    public AddCommandOptions chown(String user) {
        this.options.add(AddCommandOptions.CHOWN + "=" + user);
        return this;
    }

    public AddCommandOptions chown(Integer user, Integer group) {
        this.options.add(AddCommandOptions.CHOWN + "=" + user.toString() + ":" + group.toString());
        return this;
    }

    public AddCommandOptions chown(Integer user) {
        this.options.add(AddCommandOptions.CHOWN + "=" + user.toString());
        return this;
    }

    public AddCommandOptions chmod(String mode) {
        this.options.add(AddCommandOptions.CHMOD + "=" + mode);
        return this;
    }

    public AddCommandOptions link() {
        this.options.add(AddCommandOptions.LINK);
        return this;
    }

    public AddCommandOptions exclude(String pattern) {
        this.options.add(AddCommandOptions.EXCLUDE + "=" + pattern);
        return this;
    }
}