package com.deceptionkit.registration;

import com.deceptionkit.model.Group;
import com.deceptionkit.spring.response.SimpleResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
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

    private GroupRepresentation generateGroupRep(Group group) {
        GroupRepresentation groupRep = new GroupRepresentation();
        groupRep.setName(group.getName());
        return groupRep;
    }

    private RoleResource getRoleResource(RolesResource rolesResource, String roleName) {
        List<RoleRepresentation> roles = rolesResource.list();
        for (RoleRepresentation role : roles) {
            if (role.getName().equals(roleName)) {
                return rolesResource.get(role.getName());
            }
        }
        return null;
    }

    private GroupResource getGroupResourceByName(org.keycloak.admin.client.resource.GroupsResource groupsResource, String groupName) {
        List<GroupRepresentation> groups = groupsResource.groups();
        for (GroupRepresentation group : groups) {
            if (group.getName().equals(groupName)) {
                return groupsResource.group(group.getId());
            }
        }
        return null;
    }

    @PostMapping(value = "/registerGroups", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SimpleResponse> registerUser(@RequestParam(name = "realm", defaultValue = "master") String realm, @RequestBody List<Group> groups) {
        List<Response> responses = new ArrayList<>();
        for (Group g : groups) {
            GroupRepresentation group = generateGroupRep(g);

            Response response = keycloak.realm(realm).groups().add(group);

            responses.add(response);

            if (response.getStatus() != 201) {
                logger.error("Error creating group: " + g.getName());
                logger.error("Group: " + g.toString());
                logger.error("Response: " + response.readEntity(String.class));
                return new ResponseEntity<>(new SimpleResponse(response.getStatus(), response.getStatusInfo().toString()), HttpStatus.valueOf(response.getStatus()));
            } else {
                response.close();
            }
        }
        logger.info(responses.size() + " groups registered");

        return new ResponseEntity<>(new SimpleResponse(HttpStatus.CREATED.value(), responses.size() + " groups registered"), HttpStatus.CREATED);

    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<SimpleResponse> handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return new ResponseEntity<>(new SimpleResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
