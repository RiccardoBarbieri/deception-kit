package com.deceptionkit.yamlspecs.idprovider.client;

import com.deceptionkit.generation.DefaultsProvider;
import com.deceptionkit.model.Client;
import com.deceptionkit.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientDefinition {

    private String id;
    private String name;
    private String description;
    private String rootUrl;
    private List<String> redirectUris;
    private String baseUrl;
    private String adminUrl;
    private String protocol;
    private String authenticatorType;
    private Boolean standardFlowEnabled;
    private Boolean implicitFlowEnabled;
    private Boolean directAccessGrantsEnabled;
    private List<String> roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAuthenticatorType() {
        return authenticatorType;
    }

    public void setAuthenticatorType(String authenticatorType) {
        this.authenticatorType = authenticatorType;
    }

    public Boolean getStandardFlowEnabled() {
        return standardFlowEnabled;
    }

    public void setStandardFlowEnabled(Boolean standardFlowEnabled) {
        this.standardFlowEnabled = standardFlowEnabled;
    }

    public Boolean getImplicitFlowEnabled() {
        return implicitFlowEnabled;
    }

    public void setImplicitFlowEnabled(Boolean implicitFlowEnabled) {
        this.implicitFlowEnabled = implicitFlowEnabled;
    }

    public Boolean getDirectAccessGrantsEnabled() {
        return directAccessGrantsEnabled;
    }

    public void setDirectAccessGrantsEnabled(Boolean directAccessGrantsEnabled) {
        this.directAccessGrantsEnabled = directAccessGrantsEnabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Client convertClient(List<Role> roles) {
        Client client = new Client();
        client.setClientId(this.id);
        client.setName(this.name);
        client.setDescription(this.description);
        client.setRootUrl(this.rootUrl);
        client.setAdminUrl(this.adminUrl);
        client.setBaseUrl(this.baseUrl);
        client.setEnabled(DefaultsProvider.getClientDefault("enabled", Boolean.class));
        client.setClientAuthenticatorType(this.authenticatorType);
        client.setRedirectUris(this.redirectUris);
        client.setWebOrigins(new ArrayList<>());
        client.setStandardFlowEnabled(this.standardFlowEnabled);
        client.setImplicitFlowEnabled(this.implicitFlowEnabled);
        client.setDirectAccessGrantsEnabled(this.directAccessGrantsEnabled);
        client.setPublicClient(DefaultsProvider.getClientDefault("publicClient", Boolean.class));
        client.setProtocol(this.protocol);
        client.setAttributes(DefaultsProvider.getClientDefault("attributes", Map.class));
        List<Role> tempRoles = new ArrayList<>();
        for (Role role : roles) {
            if (this.roles.contains(role.getName())) {
                tempRoles.add(role);
            }
        }
        client.setRoles(tempRoles);
        return client;
    }
}