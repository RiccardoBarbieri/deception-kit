package com.deceptionkit.yamlspecs.idprovider;

import com.deceptionkit.yamlspecs.utils.validation.ValidationUtils;

public class ScopeSpecification {

    private String client;
    private String realm;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        if (!ValidationUtils.validateGenericString(client)) {
            throw new IllegalArgumentException("Invalid client name: " + client);
        }
        this.client = client;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        if (!ValidationUtils.validateGenericString(realm)) {
            throw new IllegalArgumentException("Invalid realm name: " + realm);
        }
        this.realm = realm;
    }
    
    public Boolean isClientScope() {
        return client != null;
    }
    
    public Boolean isRealmScope() {
        return realm != null;
    }
}
