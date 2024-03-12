package com.deceptionkit.dockerfile.options.options;

import java.util.List;

public abstract class OptionOptions {

    protected final List<String> optionOptions;
    protected final List<String> optionOptionsValues;

    public OptionOptions() {
        this.optionOptions = new java.util.ArrayList<>();
        this.optionOptionsValues = new java.util.ArrayList<>();
    }

    public String build() {
        if (optionOptions.size() != optionOptionsValues.size()) {
            throw new IllegalArgumentException("Option options and values must be the same size");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < optionOptions.size(); i++) {
            builder.append(",").append(optionOptions.get(i));
            builder.append("=");
            builder.append(optionOptionsValues.get(i));
        }
        return builder.toString();
    }
}
