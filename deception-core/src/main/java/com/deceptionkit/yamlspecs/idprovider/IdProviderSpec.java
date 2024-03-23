package com.deceptionkit.yamlspecs.idprovider;

import com.deceptionkit.yamlspecs.idprovider.client.ClientSpecification;
import com.deceptionkit.yamlspecs.idprovider.group.GroupSpecification;
import com.deceptionkit.yamlspecs.idprovider.role.RoleSpecification;
import com.deceptionkit.yamlspecs.idprovider.user.UserSpecification;
import com.deceptionkit.yamlspecs.utils.validation.ValidationUtils;

public class IdProviderSpec {

    private String domain;
    private GroupSpecification groups;
    private UserSpecification users;
    private ClientSpecification clients;
    private RoleSpecification roles;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        if (!ValidationUtils.validateDomain(domain)) {
            throw new IllegalArgumentException("Invalid domain: " + domain);
        }
        this.domain = domain;
    }

    public GroupSpecification getGroups() {
        return groups;
    }

    public void setGroups(GroupSpecification groups) {
        this.groups = groups;
    }

    public UserSpecification getUsers() {
        return users;
    }

    public void setUsers(UserSpecification users) {
        this.users = users;
    }

    public ClientSpecification getClients() {
        return clients;
    }

    public void setClients(ClientSpecification clients) {
        this.clients = clients;
    }

    public RoleSpecification getRoles() {
        return roles;
    }

    public void setRoles(RoleSpecification roles) {
        this.roles = roles;
    }
}
