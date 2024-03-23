package com.deceptionkit.spring.yaml;

import java.util.*;

public class YamlErrorMessageUtils {

    private Map<String, String> properties = new HashMap<>();
    private String message;

    public YamlErrorMessageUtils(String exceptionMessage) {
        List<String> lines = Arrays.asList(exceptionMessage.split("\n"));

        List<String> cannotLines = new ArrayList<>();
        List<String> valueLines = new ArrayList<>();

        int lastCannot = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("Cannot")) {
                cannotLines.add(lines.get(i));
                lastCannot = i;
            }
            if (lastCannot != -1 && (lastCannot + 2 == i)) {
                valueLines.add(lines.get(i));
            }
        }

        for (String line : cannotLines) {
            String clazzName = getClazzName(line);
            String property = getPropertyName(line);

            properties.put(clazzName, property);
        }
        String value = getValue(valueLines.get(valueLines.size() - 1));

        this.message = createMessage(properties, value);

    }

    private String getValue(String line) {
        String[] parts = line.split(":");
        String value = "";
        if (parts.length >= 2) {
            value = parts[1].strip();
        }
        return value.strip();
    }

    private String getPropertyName(String line) {
        String[] parts = line.split("=");
        String property = "";
        if (parts.length >= 2) {
            property = parts[1].split(" ")[0];
        }
        return property.strip();
    }

    private String getClazzName(String line) {
        String[] parts = line.split("JavaBean=");
        String clazzName = "";
        if (parts.length >= 2) {
            clazzName = parts[1].split("@")[0];
        }
        return clazzName.strip();
    }

    private String createMessage(Map<String, String> properties, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid YAML property: ");
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            sb.append(entry.getValue()).append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" = ").append(value);
        return sb.toString();
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getMessage() {
        return message;
    }
}
