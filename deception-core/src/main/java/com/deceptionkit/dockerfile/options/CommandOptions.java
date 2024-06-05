package com.deceptionkit.dockerfile.options;

import java.util.List;

public abstract class CommandOptions {

    protected final List<String> options;
//    protected final Map<String, List<OptionType>> optionOptions;

    public CommandOptions() {
        this.options = new java.util.ArrayList<>();
//        this.optionOptions = new java.util.HashMap<>();
    }

    public String build() {
        return String.join(" ", options);
    }
}
