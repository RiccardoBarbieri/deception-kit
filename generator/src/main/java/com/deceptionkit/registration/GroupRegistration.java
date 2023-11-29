package com.deceptionkit.registration;

import com.deceptionkit.model.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GroupRegistration {

    private final Keycloak keycloak;

    @Autowired
    public GroupRegistration(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @PostMapping("/registerGroups")
    public ResponseEntity<String> registerUser(@RequestParam(defaultValue = "master") String realm, @RequestBody List<User> users) {
        UserRepresentation user = new UserRepresentation();
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        return ResponseEntity.ok("User " + " registered");
    }
}
