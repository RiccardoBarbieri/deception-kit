package com.deceptionkit.yamlspecs.idprovider.group;

import com.deceptionkit.model.idprovider.Group;
import com.deceptionkit.yamlspecs.utils.validation.ValidationUtils;

import java.util.List;

public class GroupDefinition {

    private String name;
    private List<String> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!ValidationUtils.validateGenericString(name)) {
            throw new IllegalArgumentException("Invalid name: " + name);
        }
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        if (!ValidationUtils.validateGenericStrings(roles)) {
            throw new IllegalArgumentException("Invalid roles: " + roles);
        }
        this.roles = roles;
    }

    public Group convertGroup() {
        Group group = new Group();
        group.setName(name);
        group.setRoles(roles);
        return group;
    }
}
