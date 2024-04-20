package com.deceptionkit.yamlspecs.database;

public class DatabaseDefinition {

    private String component;

    private DatabaseSpec specification;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public DatabaseSpec getSpecification() {
        return specification;
    }

    public void setSpecification(DatabaseSpec specification) {
        this.specification = specification;
    }
}
