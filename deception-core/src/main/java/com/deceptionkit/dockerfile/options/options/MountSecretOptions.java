package com.deceptionkit.dockerfile.options.options;

public class MountSecretOptions extends OptionOptions {

    public static final String ID = "id";
    public static final String TARGET = "target";
    public static final String REQUIRED = "required";
    public static final String MODE = "mode";
    public static final String uid = "uid";
    public static final String GID = "gid";

    public MountSecretOptions id(String id) {
        this.optionOptions.add(MountSecretOptions.ID);
        this.optionOptionsValues.add(id);
        return this;
    }

    public MountSecretOptions target(String mountPath) {
        this.optionOptions.add(MountSecretOptions.TARGET);
        this.optionOptionsValues.add(mountPath);
        return this;
    }

    public MountSecretOptions required() {
        this.optionOptions.add(MountSecretOptions.REQUIRED);
        this.optionOptionsValues.add("true");
        return this;
    }

    public MountSecretOptions mode(String mode) {
        this.optionOptions.add(MountSecretOptions.MODE);
        this.optionOptionsValues.add(mode);
        return this;
    }

    public MountSecretOptions uid(Integer uid) {
        this.optionOptions.add(MountSecretOptions.uid);
        this.optionOptionsValues.add(uid.toString());
        return this;
    }

    public MountSecretOptions gid(Integer gid) {
        this.optionOptions.add(MountSecretOptions.GID);
        this.optionOptionsValues.add(gid.toString());
        return this;
    }
}
