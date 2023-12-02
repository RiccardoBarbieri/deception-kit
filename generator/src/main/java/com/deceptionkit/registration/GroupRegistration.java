package com.deceptionkit.registration;

import com.deceptionkit.model.Group;
import com.deceptionkit.model.User;
import com.deceptionkit.response.model.SimpleResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GroupRegistration {

    private final Logger logger;

    private final Keycloak keycloak;

    @Autowired
    public GroupRegistration(Keycloak keycloak) {
        this.keycloak = keycloak;
        this.logger = LoggerFactory.getLogger(GroupRegistration.class);
    }

    private GroupRepresentation generateGroupRep(Group g) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(g.getName());
        return group;
    }

    @PostMapping("/registerGroups")
    @ResponseBody
    public SimpleResponse registerUser(@RequestParam(defaultValue = "master") String realm, @RequestBody List<Group> groups) {
        List<Response> responses = new ArrayList<>();
        for (Group g : groups) {
            GroupRepresentation group = generateGroupRep(g);

            Response response = keycloak.realm(realm).groups().add(group);
            responses.add(response);

            if (response.getStatus() != 201) {
                logger.error("Error creating group: " + g.getName());
                return new SimpleResponse(response.getStatus(), response.getStatusInfo().toString());
            } else {
                response.close();
            }
        }
        logger.info(responses.size() + " groups registered");

        return new SimpleResponse(HttpStatus.CREATED.value(), responses.size() + " groups registered");

    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public SimpleResponse handleException(java.lang.Exception e) {
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
