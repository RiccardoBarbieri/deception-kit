package com.deceptionkit.dockerfile.utils;

import com.deceptionkit.dockerfile.options.CommandOptions;

import java.util.List;

public class CommandUtils {

    public static String argsShellOrSpaced(String command, String executable, List<String> args, Boolean spaced) {

        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(command).append(" ");

        if (!executable.isEmpty()) {
            if (spaced) {
                lineBuilder.append("[\"").append(executable).append("\",");
            } else {
                lineBuilder.append(executable).append(" ");
            }
        } else {
            if (spaced) {
                lineBuilder.append("[");
            }
        }

        for (String arg : args) {
            if (spaced) {
                lineBuilder.append("\"").append(arg).append("\",");
            } else {
                lineBuilder.append(arg).append(" ");
            }
        }

        if (spaced) {
            lineBuilder.deleteCharAt(lineBuilder.length() - 1);
            lineBuilder.append("]");
        }

        return lineBuilder.toString();
    }

    public static String coupleShellOrSpaced(String command, String src, String dest, CommandOptions options) {
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(command).append(" ");

        if (options != null) {
            lineBuilder.append(options.build()).append(" ");
        }

        if (src.contains(" ") || dest.contains(" ")) {
            lineBuilder.append("[\"").append(src).append("\"").append(", \"").append(dest).append("\"]");
        } else {
            lineBuilder.append(src).append(" ").append(dest);
        }

        return lineBuilder.toString();
    }
}
