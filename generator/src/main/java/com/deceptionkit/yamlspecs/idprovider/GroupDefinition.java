package com.deceptionkit.yamlspecs.idprovider;

import com.deceptionkit.model.Group;

public class GroupDefinition {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        Group group = new Group();
        group.setName(name);
        return group;
    }
}
