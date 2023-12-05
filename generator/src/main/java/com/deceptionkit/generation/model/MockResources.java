package com.deceptionkit.generation.model;


import com.deceptionkit.model.Client;
import com.deceptionkit.model.Group;
import com.deceptionkit.model.Role;
import com.deceptionkit.model.User;

import java.util.List;

public class MockResources {

    public final List<User> users;
    public final List<Group> groups;

    public final List<Role> roles;

    public final List<Client> clients;

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
}
