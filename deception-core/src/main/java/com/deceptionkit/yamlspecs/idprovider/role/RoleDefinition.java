package com.deceptionkit.yamlspecs.idprovider.role;

import com.deceptionkit.model.idprovider.Role;
import com.deceptionkit.yamlspecs.idprovider.ScopeSpecification;
import com.deceptionkit.yamlspecs.utils.validation.ValidationUtils;

public class RoleDefinition {

    private String name;
    private ScopeSpecification scope;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!ValidationUtils.validateGenericString(name)) {
            throw new IllegalArgumentException("Invalid role name: " + name);
        }
        this.name = name;
    }

    public ScopeSpecification getScope() {
        return scope;
    }

    public void setScope(ScopeSpecification scope) {
        this.scope = scope;
    }

    public Role convertRole() {
        Role role = new Role();
        role.setName(name);
        if (scope.isClientScope()) {
            role.setClientRole(true);
        } else if (scope.isRealmScope()) {
            role.setClientRole(false);
        }
        role.setComposite(false);
        role.setDescription("Role for " + name);
        role.setClientName(scope.getClient());
        role.setRealmName(scope.getRealm());
        return role;
    }
}
