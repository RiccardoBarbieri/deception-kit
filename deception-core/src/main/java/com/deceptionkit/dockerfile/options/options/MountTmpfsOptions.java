package com.deceptionkit.dockerfile.options.options;

public class MountTmpfsOptions extends OptionOptions {

    public static final String TARGET = "target";
    public static final String SIZE = "size";

    public MountTmpfsOptions target(String mountPath) {
        this.optionOptions.add(MountTmpfsOptions.TARGET);
        this.optionOptionsValues.add(mountPath);
        return this;
    }

    public MountTmpfsOptions size(Integer size) {
        this.optionOptions.add(MountTmpfsOptions.SIZE);
        this.optionOptionsValues.add(size.toString());
        return this;
    }
}
