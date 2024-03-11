package com.deceptionkit.dockerfile.commands;

import java.util.ArrayList;
import java.util.List;

public class LabelCommand extends Command {

    public static final String COMMAND = "LABEL";

    private List<String> keys;
    private List<String> values;
    private Boolean spaced = false;

    protected LabelCommand() {
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public LabelCommand label(String key, String value) {
        if (key.contains(" ") || value.contains(" ")) {
            this.spaced = true;
        }
        this.keys.add(key);
        this.values.add(value);
        return this;
    }

    @Override
    public String build() {
        if (keys.isEmpty() || values.isEmpty()) {
            throw new IllegalArgumentException("LABEL command must have at least one label");
        }
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(COMMAND).append(" ");

        for (int i = 0; i < keys.size(); i++) {
            if (spaced) {
                lineBuilder.append("\"").append(keys.get(i)).append("\"").append("=");
            } else {
                lineBuilder.append(keys.get(i)).append("=");
            }

            if (spaced) {
                lineBuilder.append("\"").append(values.get(i)).append("\"");
            } else {
                lineBuilder.append(values.get(i));
            }
            lineBuilder.append(" ");
        }
        lineBuilder.deleteCharAt(lineBuilder.length() - 1);
        return lineBuilder.toString();

    }
}
