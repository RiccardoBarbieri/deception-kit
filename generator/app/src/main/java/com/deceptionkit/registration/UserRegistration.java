package com.deceptionkit.registration;

import com.deceptionkit.model.Credential;
import com.deceptionkit.model.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserRegistration {

    private final Keycloak keycloak;

    @Autowired
    public UserRegistration(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @PostMapping("/registerUsers")
    public ResponseEntity<String> registerUser(@RequestParam(defaultValue = "master") String realm, @RequestBody List<User> users) {
        List<Response> responses = new ArrayList<>();
        for (User u : users) {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(u.getUsername());
            user.setFirstName(u.getFirstName());
            user.setLastName(u.getLastName());
            user.setEmail(u.getEmail());
            user.setEnabled(u.getEnabled());
            user.setGroups(u.getGroups());
            List<CredentialRepresentation> tempCred = new ArrayList<>();
            for (Credential c : u.getCredentials()) {
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(c.getType());
                credential.setValue(c.getValue());
                tempCred.add(credential);
            }
            user.setCredentials(tempCred);
            responses.add(keycloak.realm(realm).users().create(user));
        }

        //TODO: funzione controllo risposte corrette (log quante hanno avuto successo?)

        return ResponseEntity.ok("User " + " registered");
    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleException(java.lang.Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
