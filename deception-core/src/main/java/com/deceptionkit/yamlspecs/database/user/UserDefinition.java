package com.deceptionkit.yamlspecs.database.user;

import java.util.List;

public class UserDefinition {

    private String username;
    private String password;
    private List<AccessibleDatabases> databases;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AccessibleDatabases> getDatabases() {
        return databases;
    }

    public void setDatabases(List<AccessibleDatabases> databases) {
        this.databases = databases;
    }
}
