package com.deceptionkit.dockerfile.options.options;

public class MountBindOptions extends OptionOptions {

    public static final String TARGET = "target";
    public static final String SOURCE = "source";
    public static final String FROM = "from";
    public static final String READWRITE = "readwrite";

    public MountBindOptions target(String mountPath) {
        this.optionOptions.add(MountBindOptions.TARGET);
        this.optionOptionsValues.add(mountPath);
        return this;
    }

    public MountBindOptions source(String sourcePath) {
        this.optionOptions.add(MountBindOptions.SOURCE);
        this.optionOptionsValues.add(sourcePath);
        return this;
    }

    public MountBindOptions from(String from) {
        this.optionOptions.add(MountBindOptions.FROM);
        this.optionOptionsValues.add(from);
        return this;
    }

    public MountBindOptions readwrite() {
        this.optionOptions.add(MountBindOptions.READWRITE);
        this.optionOptionsValues.add("true");
        return this;
    }

}
