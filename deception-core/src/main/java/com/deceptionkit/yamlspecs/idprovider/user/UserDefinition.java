package com.deceptionkit.yamlspecs.idprovider.user;

import com.deceptionkit.model.idprovider.Credential;
import com.deceptionkit.model.idprovider.User;
import com.deceptionkit.yamlspecs.idprovider.CredentialSpecification;
import com.deceptionkit.yamlspecs.utils.validation.ValidationUtils;

import javax.mail.internet.AddressException;
import java.util.ArrayList;
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
        if (!ValidationUtils.validateUsername(username))
            throw new IllegalArgumentException("Invalid username: " + username);
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        if (!ValidationUtils.validateGenericStringSpaces(firstname))
            throw new IllegalArgumentException("Invalid firstname: " + firstname);
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        if (!ValidationUtils.validateGenericStringSpaces(lastname))
            throw new IllegalArgumentException("Invalid lastname: " + lastname);
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws AddressException {
        if (!ValidationUtils.validateEmail(email))
            throw new IllegalArgumentException("Invalid email: " + email);
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
        if (!ValidationUtils.validateGenericStrings(groups))
            throw new IllegalArgumentException("Invalid groups: " + groups);
        this.groups = groups;
    }

    public List<CredentialSpecification> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialSpecification> credentials) {
        this.credentials = credentials;
    }

    public User convertUser() {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setEnabled(enabled);
        user.setGroups(groups);
        List<Credential> credentials = new ArrayList<>();
        for (CredentialSpecification credentialSpecification : this.credentials) {
            credentials.add(credentialSpecification.getCredential());
        }
        user.setCredentials(credentials);
        return user;
    }
}
