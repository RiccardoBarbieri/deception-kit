package com.deceptionkit.generation.database.model;

import java.util.List;

public class UserResources {

    private String username;
    private String createUserStm;
    private List<String> grantPrivilegesStm;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateUserStm() {
        return createUserStm;
    }

    public void setCreateUserStm(String createUserStm) {
        this.createUserStm = createUserStm;
    }

    public List<String> getGrantPrivilegesStm() {
        return grantPrivilegesStm;
    }

    public void setGrantPrivilegesStm(List<String> grantPrivilegesStm) {
        this.grantPrivilegesStm = grantPrivilegesStm;
    }
}
