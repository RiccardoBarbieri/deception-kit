package com.deceptionkit.mockaroo.exchange.model;

public class Parameter {

    private String name;
    private String type;
    private String description;
    private String defaultValue;

    public Parameter() {
        this.name = "";
        this.type = "";
        this.description = "";
        this.defaultValue = "";
    }

    public Parameter(String name, String type, String description, String defaultValue) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefault() {
        return defaultValue;
    }

    public void setDefault(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
