package com.deceptionkit.registration;

import com.deceptionkit.model.Credential;
import com.deceptionkit.model.User;
import com.deceptionkit.response.model.SimpleResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserRegistration {

    private final Keycloak keycloak;

    private final Logger logger;

    @Autowired
    public UserRegistration(Keycloak keycloak) {
        this.keycloak = keycloak;
        this.logger = org.slf4j.LoggerFactory.getLogger(UserRegistration.class);
    }

    private UserRepresentation generateUserRep(User u) {
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
//            if (c.getType().equals("secret")) {
//                credential.setType(CredentialRepresentation.SECRET);
//            }
            credential.setType(c.getType());
            credential.setValue(c.getValue());
            tempCred.add(credential);
        }
        user.setCredentials(tempCred);
        return user;
    }

    @PostMapping(value = "/registerUsers", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public SimpleResponse registerUser(@RequestParam(defaultValue = "master") String realm, @RequestBody List<User> users) {
        List<Response> responses = new ArrayList<>();
        for (User u : users) {
            UserRepresentation user = generateUserRep(u);

            Response response = keycloak.realm(realm).users().create(user);
            responses.add(response);

            if (response.getStatus() != 201) {
                logger.error("Failed to register user: " + u.getUsername());
                return new SimpleResponse(response.getStatus(), "Failed to register user: " + u.getUsername());
            } else {
                response.close();
            }
        }
        logger.info(responses.size() + " users registered");

        return new SimpleResponse(HttpStatus.CREATED.value(), responses.size() + " users registered");
    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public SimpleResponse handleException(java.lang.Exception e) {
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
