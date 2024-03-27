package com.deceptionkit.mockaroo.exchange.model;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class Type {

    private String name;
    private String type;
    private List<Parameter> parameters;

    public Type() {
        this.name = "";
        this.type = "";
        this.parameters = new ArrayList<>();
    }

    public Type(String name, String type, List<Parameter> parameters) {
        this.name = name;
        this.type = type;
        this.parameters = parameters;
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

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
