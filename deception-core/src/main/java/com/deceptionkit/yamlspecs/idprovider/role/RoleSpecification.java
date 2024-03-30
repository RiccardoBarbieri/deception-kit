package com.deceptionkit.yamlspecs.idprovider.role;

import com.deceptionkit.model.idprovider.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleSpecification {

    private List<RoleDefinition> definitions;

    public List<RoleDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<RoleDefinition> definitions) {
        this.definitions = definitions;
    }

    public List<Role> convertRoles() {
        List<Role> roles = new ArrayList<>();
        for (RoleDefinition definition : definitions) {
            roles.add(definition.convertRole());
        }
        return roles;
    }
}
