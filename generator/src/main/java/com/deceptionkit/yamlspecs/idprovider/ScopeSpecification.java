package com.deceptionkit.yamlspecs.idprovider;

public class ScopeSpecification {

    private String client;
    private String realm;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
    
    public Boolean isClientScope() {
        return client != null;
    }
    
    public Boolean isRealmScope() {
        return realm != null;
    }
}
