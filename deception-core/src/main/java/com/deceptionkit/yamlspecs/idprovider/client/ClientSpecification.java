package com.deceptionkit.yamlspecs.idprovider.client;

import com.deceptionkit.model.Client;
import com.deceptionkit.model.Role;
import com.deceptionkit.yamlspecs.utils.validation.ValidationUtils;

import java.util.List;

public class ClientSpecification {

    private Integer roles_per_client;

    private List<ClientDefinition> definitions;

    public Integer getRoles_per_client() {
        return roles_per_client;
    }

    public void setRoles_per_client(Integer roles_per_client) {
        if (!ValidationUtils.validatePositiveInteger(roles_per_client)) {
            throw new IllegalArgumentException("Invalid roles_per_client: " + roles_per_client);
        }
        this.roles_per_client = roles_per_client;
    }

    public List<ClientDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<ClientDefinition> definitions) {
        this.definitions = definitions;
    }

    public List<Client> convertClients(List<Role> roles) {
        List<Client> clients = new java.util.ArrayList<>();
        for (ClientDefinition definition : definitions) {
            clients.add(definition.convertClient(roles));
        }
        return clients;
    }
}
