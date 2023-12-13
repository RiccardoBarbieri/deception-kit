package com.deceptionkit.registration;

import com.deceptionkit.model.Credential;
import com.deceptionkit.model.User;
import com.deceptionkit.spring.response.SimpleResponse;
import org.keycloak.admin.client.Keycloak;
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

    private UserRepresentation generateUserRep(User user) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(user.getUsername());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(user.getEnabled());
        userRep.setGroups(user.getGroups());
        List<CredentialRepresentation> tempCred = new ArrayList<>();
        for (Credential c : user.getCredentials()) {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(c.getType());
            credential.setValue(c.getValue());
            tempCred.add(credential);
        }
        userRep.setCredentials(tempCred);
        return userRep;
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
    @ResponseBody
    public SimpleResponse handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}
