package com.deceptionkit.yamlspecs.idprovider;

import java.util.List;

public class RoleSpecification {

    private List<RoleDefinition> definitions;

    public List<RoleDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<RoleDefinition> definitions) {
        this.definitions = definitions;
    }
}
