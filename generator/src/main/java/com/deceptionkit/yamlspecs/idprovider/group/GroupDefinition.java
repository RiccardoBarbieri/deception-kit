package com.deceptionkit.yamlspecs.idprovider.group;

import com.deceptionkit.model.Group;

import java.util.List;

public class GroupDefinition {

    private String name;
    private List<String> roles;

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

    public Group convertGroup() {
        Group group = new Group();
        group.setName(name);
        return group;
    }
}
