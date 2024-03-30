package com.deceptionkit.yamlspecs.idprovider.user;

import com.deceptionkit.model.idprovider.User;
import com.deceptionkit.yamlspecs.utils.validation.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    private Integer total;
    private Integer groups_per_user;
    private Integer credentials_per_user;
    private List<UserDefinition> definitions;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        if (!ValidationUtils.validatePositiveInteger(total)) {
            throw new IllegalArgumentException("Invalid total: " + total);
        }
        this.total = total;
    }

    public Integer getGroups_per_user() {
        return groups_per_user;
    }

    public void setGroups_per_user(Integer groups_per_user) {
        if (!ValidationUtils.validatePositiveInteger(groups_per_user)) {
            throw new IllegalArgumentException("Invalid groups_per_user: " + groups_per_user);
        }
        this.groups_per_user = groups_per_user;
    }

    public Integer getCredentials_per_user() {
        return credentials_per_user;
    }

    public void setCredentials_per_user(Integer credentials_per_user) {
        if (!ValidationUtils.validatePositiveInteger(credentials_per_user)) {
            throw new IllegalArgumentException("Invalid credentials_per_user: " + credentials_per_user);
        }
        this.credentials_per_user = credentials_per_user;
    }

    public List<UserDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<UserDefinition> definitions) {
        this.definitions = definitions;
    }

    public List<User> convertUsers() {
        List<User> users = new ArrayList<>();
        for (UserDefinition userDefinition : definitions) {
            users.add(userDefinition.convertUser());
        }
        return users;
    }
}
