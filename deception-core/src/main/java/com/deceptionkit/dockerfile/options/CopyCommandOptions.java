package com.deceptionkit.dockerfile.options;

public class CopyCommandOptions extends CommandOptions {

    protected static final String CHOWN = "--chown";
    protected static final String CHMOD = "--chmod";
    protected static final String LINK = "--link";
    protected static final String PARENTS = "--parents";
    protected static final String EXCLUDE = "--exclude";
    protected static final String FROM = "--from";

    public CopyCommandOptions chown(String user, String group) {
        this.options.add(CopyCommandOptions.CHOWN + "=" + user + ":" + group);
        return this;
    }

    public CopyCommandOptions chown(String user) {
        this.options.add(CopyCommandOptions.CHOWN + "=" + user);
        return this;
    }

    public CopyCommandOptions chown(Integer user, Integer group) {
        this.options.add(CopyCommandOptions.CHOWN + "=" + user.toString() + ":" + group.toString());
        return this;
    }

    public CopyCommandOptions chown(Integer user) {
        this.options.add(CopyCommandOptions.CHOWN + "=" + user.toString());
        return this;
    }

    public CopyCommandOptions chmod(String mode) {
        this.options.add(CopyCommandOptions.CHMOD + "=" + mode);
        return this;
    }

    public CopyCommandOptions link() {
        this.options.add(CopyCommandOptions.LINK);
        return this;
    }

    public CopyCommandOptions parents() {
        this.options.add(CopyCommandOptions.PARENTS);
        return this;
    }

    public CopyCommandOptions exclude(String pattern) {
        this.options.add(CopyCommandOptions.EXCLUDE + "=" + pattern);
        return this;
    }

    public CopyCommandOptions from(String stage) {
        this.options.add(CopyCommandOptions.FROM + "=" + stage);
        return this;
    }
}
