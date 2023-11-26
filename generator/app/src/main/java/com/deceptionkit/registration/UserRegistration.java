package com.deceptionkit.registration;

import com.deceptionkit.model.User;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        for (User u : users) {
            System.out.println(u);
        }
//        UserRepresentation user = new UserRepresentation();
//        CredentialRepresentation credential = new CredentialRepresentation();
//        credential.setType(CredentialRepresentation.PASSWORD);
        return ResponseEntity.ok("User " + " registered");
    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleException(java.lang.Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
