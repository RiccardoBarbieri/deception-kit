package com.deceptionkit.componentspec.idmodel;

import java.util.List;

public class ClientSpecification {

    private Integer roles_per_client;

    private List<ClientDefinition> definitions;

    public Integer getRoles_per_client() {
        return roles_per_client;
    }

    public void setRoles_per_client(Integer roles_per_client) {
        this.roles_per_client = roles_per_client;
    }

    public List<ClientDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<ClientDefinition> definitions) {
        this.definitions = definitions;
    }
}
