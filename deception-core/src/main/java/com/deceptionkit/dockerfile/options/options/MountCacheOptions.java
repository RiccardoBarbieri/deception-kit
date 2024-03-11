package com.deceptionkit.dockerfile.options.options;

public class MountCacheOptions extends OptionOptions {

    public static final String ID = "id";
    public static final String TARGET = "target";
    public static final String READONLY = "readonly";
    public static final String SHARING = "sharing";
    public static final String FROM = "from";
    public static final String SOURCE = "source";
    public static final String MODE = "mode";
    public static final String UID = "uid";
    public static final String GID = "gid";

    public MountCacheOptions id(String id) {
        this.optionOptions.add(MountCacheOptions.ID);
        this.optionOptionsValues.add(id);
        return this;
    }

    public MountCacheOptions target(String mountPath) {
        this.optionOptions.add(MountCacheOptions.TARGET);
        this.optionOptionsValues.add(mountPath);
        return this;
    }

    public MountCacheOptions readonly() {
        this.optionOptions.add(MountCacheOptions.READONLY);
        this.optionOptionsValues.add("true");
        return this;
    }

    public MountCacheOptions sharing(String sharingType) {
        this.optionOptions.add(MountCacheOptions.SHARING);
        this.optionOptionsValues.add(sharingType);
        return this;
    }

    public MountCacheOptions from(String from) {
        this.optionOptions.add(MountCacheOptions.FROM);
        this.optionOptionsValues.add(from);
        return this;
    }

    public MountCacheOptions source(String sourcePath) {
        this.optionOptions.add(MountCacheOptions.SOURCE);
        this.optionOptionsValues.add(sourcePath);
        return this;
    }

    public MountCacheOptions mode(String mode) {
        this.optionOptions.add(MountCacheOptions.MODE);
        this.optionOptionsValues.add(mode);
        return this;
    }

    public MountCacheOptions uid(Integer uid) {
        this.optionOptions.add(MountCacheOptions.UID);
        this.optionOptionsValues.add(uid.toString());
        return this;
    }

    public MountCacheOptions gid(Integer gid) {
        this.optionOptions.add(MountCacheOptions.GID);
        this.optionOptionsValues.add(gid.toString());
        return this;
    }
}
