package com.deceptionkit.componentspec.idmodel;

import com.deceptionkit.componentspec.idmodel.ClientSpecification;
import com.deceptionkit.componentspec.idmodel.GroupSpecification;
import com.deceptionkit.componentspec.idmodel.RoleSpecification;
import com.deceptionkit.componentspec.idmodel.UserSpecification;

public class IdProviderSpec {

    private GroupSpecification groups;
    private UserSpecification users;
    private ClientSpecification clients;
    private RoleSpecification roles;

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
