package com.deceptionkit.dockerfile.commands;

public class FromCommand extends CommandWithOptions {

    public static final String COMMAND = "FROM";

    private String image;
    private String tag;
    private String digest;
    private String name;

    public FromCommand from(String image) {
        this.image = image;
        return this;
    }

    public FromCommand tag(String tag) {
        this.tag = tag;
        return this;
    }

    public FromCommand digest(String digest) {
        this.digest = digest;
        return this;
    }

    public FromCommand name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String build() {
        if (image == null) {
            throw new IllegalArgumentException("Image must be set");
        }
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(COMMAND).append(" ");

        if (options != null) {
            lineBuilder.append(options.build()).append(" ");
        }

        lineBuilder.append(image);
        if (tag != null) {
            lineBuilder.append(":").append(tag);
        }
        if (digest != null) {
            lineBuilder.append("@").append(digest);
        }
        if (name != null) {
            lineBuilder.append(" AS ").append(name);
        }
        return lineBuilder.toString();
    }
}
