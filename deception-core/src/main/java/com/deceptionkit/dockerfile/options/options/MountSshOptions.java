package com.deceptionkit.dockerfile.options.options;

public class MountSshOptions extends OptionOptions {

    public static final String ID = "id";
    public static final String TARGET = "target";
    public static final String REQUIRED = "required";
    public static final String MODE = "mode";
    public static final String uid = "uid";
    public static final String GID = "gid";

    public MountSshOptions id(String id) {
        this.optionOptions.add(MountSshOptions.ID);
        this.optionOptionsValues.add(id);
        return this;
    }

    public MountSshOptions target(String mountPath) {
        this.optionOptions.add(MountSshOptions.TARGET);
        this.optionOptionsValues.add(mountPath);
        return this;
    }

    public MountSshOptions required() {
        this.optionOptions.add(MountSshOptions.REQUIRED);
        this.optionOptionsValues.add("true");
        return this;
    }

    public MountSshOptions mode(String mode) {
        this.optionOptions.add(MountSshOptions.MODE);
        this.optionOptionsValues.add(mode);
        return this;
    }

    public MountSshOptions uid(Integer uid) {
        this.optionOptions.add(MountSshOptions.uid);
        this.optionOptionsValues.add(uid.toString());
        return this;
    }

    public MountSshOptions gid(Integer gid) {
        this.optionOptions.add(MountSshOptions.GID);
        this.optionOptionsValues.add(gid.toString());
        return this;
    }
}
