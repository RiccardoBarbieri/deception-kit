package com.deceptionkit.registration;

import com.deceptionkit.model.idprovider.Credential;
import com.deceptionkit.model.idprovider.User;
import com.deceptionkit.spring.apiversion.ApiVersion;
import com.deceptionkit.spring.response.ErrorResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/registration")
@ApiVersion({"1", "1.1"})
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

    @PostMapping(value = "/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ErrorResponse> registerUser(@RequestParam(name = "realm", defaultValue = "master") String realm, @RequestBody List<User> users) {
        List<Response> responses = new ArrayList<>();
        for (User u : users) {
            UserRepresentation user = generateUserRep(u);

            Response response = keycloak.realm(realm).users().create(user);
            responses.add(response);

            if (response.getStatus() != 201) {
                logger.error("Failed to register user: " + u.getUsername());
                logger.error("User: " + u.toString());
                logger.error("Response: " + response.readEntity(String.class));
                return new ResponseEntity<>(new ErrorResponse("Failed to register user: " + u.getUsername()), HttpStatusCode.valueOf(response.getStatus()));
            } else {
                response.close();
            }
        }
        logger.info(responses.size() + " users registered");

        return new ResponseEntity<>(new ErrorResponse(responses.size() + " users registered"), HttpStatus.CREATED);
    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
