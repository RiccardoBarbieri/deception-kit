package com.deceptionkit.yamlspecs.idprovider;

import com.deceptionkit.model.Credential;
import com.deceptionkit.yamlspecs.utils.validation.ValidationUtils;

public class CredentialSpecification {

    private String type;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Credential getCredential() {
        Credential credential = new Credential();
        credential.setType(type);
        credential.setValue(value);
        return credential;
    }
}
