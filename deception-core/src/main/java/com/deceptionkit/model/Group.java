package com.deceptionkit.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Group {

    protected String name;

    protected List<String> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return (new ObjectMapper()).valueToTree(this).toString();
    }
}
