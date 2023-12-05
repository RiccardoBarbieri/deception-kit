package com.deceptionkit.componentspec.idmodel;

import java.util.List;

public class UserDefinition {

    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private Boolean enabled;
    private List<String> groups;
    private List<CredentialSpecification> credentials;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<CredentialSpecification> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialSpecification> credentials) {
        this.credentials = credentials;
    }
}
