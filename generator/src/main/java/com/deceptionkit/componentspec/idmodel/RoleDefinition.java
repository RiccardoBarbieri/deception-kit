package com.deceptionkit.componentspec.idmodel;

public class RoleDefinition {

    private String name;
    private ScopeSpecification scope;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScopeSpecification getScope() {
        return scope;
    }

    public void setScope(ScopeSpecification scope) {
        this.scope = scope;
    }
}
