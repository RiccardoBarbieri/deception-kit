package com.deceptionkit.generation.model;


import com.deceptionkit.model.idprovider.Client;
import com.deceptionkit.model.idprovider.Group;
import com.deceptionkit.model.idprovider.Role;
import com.deceptionkit.model.idprovider.User;

import java.util.List;

public class MockResources {

    public List<User> users;
    public List<Group> groups;
    public List<Role> roles;
    public List<Client> clients;

    public MockResources(List<User> users, List<Group> groups, List<Role> roles, List<Client> clients) {
        this.users = users;
        this.groups = groups;
        this.roles = roles;
        this.clients = clients;
    }

    public MockResources() {
        this.users = null;
        this.groups = null;
        this.roles = null;
        this.clients = null;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
