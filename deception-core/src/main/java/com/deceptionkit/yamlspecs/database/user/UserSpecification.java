package com.deceptionkit.yamlspecs.database.user;

import java.util.List;

public class UserSpecification {

    private UserDefinition main_user;
    private List<UserDefinition> definitions;

    public UserDefinition getMain_user() {
        return main_user;
    }

    public void setMain_user(UserDefinition main_user) {
        this.main_user = main_user;
    }

    public List<UserDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<UserDefinition> definitions) {
        this.definitions = definitions;
    }
}
