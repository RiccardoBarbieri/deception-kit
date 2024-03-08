package com.deceptionkit.dockerfile.options;

public class CopyCommandOptions extends CommandOptions {

    protected static final String CHOWN = "--chown";
    protected static final String CHMOD = "--chmod";
    protected static final String LINK = "--link";
    protected static final String PARENTS = "--parents";
    protected static final String EXCLUDE = "--exclude";

    public CopyCommandOptions chown(String user, String group) {
        this.options.add(CopyCommandOptions.CHOWN + "=" + user + ":" + group);
        return this;
    }

    public CopyCommandOptions chown(String user) {
        this.options.add(CopyCommandOptions.CHOWN + "=" + user);
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

    @Override
    public String build() {
        return String.join(" ", options);
    }
}
