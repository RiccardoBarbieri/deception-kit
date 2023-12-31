package com.deceptionkit.registration;

import com.deceptionkit.model.Role;
import com.deceptionkit.model.RoleMap;
import com.deceptionkit.spring.response.SimpleResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class RoleAssignment {

    private final Keycloak keycloak;

    private final Logger logger;

    @Autowired
    public RoleAssignment(Keycloak keycloak) {
        this.keycloak = keycloak;
        this.logger = org.slf4j.LoggerFactory.getLogger(UserRegistration.class);
    }

    @PostMapping(value = "/assignRoles", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SimpleResponse> assignRoles(@RequestParam(name = "realm", defaultValue = "master") String realm, @RequestBody RoleMap roleMappings) {
        GroupsResource groupsResource = keycloak.realm(realm).groups();
        for (String groupName : roleMappings.keySet()) {
            List<Role> roles = roleMappings.get(groupName);
            GroupResource groupRes = getGroupResourceByName(groupsResource, groupName);
            if (groupRes == null) {
                logger.error("Group not found: " + groupName);
                return new ResponseEntity<>(new SimpleResponse(HttpStatus.NOT_FOUND.value(), "Group not found: " + groupName), HttpStatus.NOT_FOUND);
            }
            for (Role role : roles) {
                if (role.isRealmRole()) {
                    RoleRepresentation roleRep = keycloak.realm(realm).roles().get(role.getName()).toRepresentation();
                    groupRes.roles().realmLevel().add(Collections.singletonList(roleRep));
                } else if (role.isClientRole()) {
                    ClientResource clientRes = getClientResourceByName(keycloak.realm(realm).clients(), role.getClientName());
                    if (clientRes == null) {
                        logger.error("Client not found: " + role.getClientName());
                        return new ResponseEntity<>(new SimpleResponse(HttpStatus.NOT_FOUND.value(), "Client not found: " + role.getClientName()), HttpStatus.NOT_FOUND);
                    }
                    ClientRepresentation clientRep = clientRes.toRepresentation();
                    RoleRepresentation roleRep = keycloak.realm(realm).clients().get(clientRep.getId()).roles().get(role.getName()).toRepresentation();
                    groupRes.roles().clientLevel(clientRep.getId()).add(Collections.singletonList(roleRep));
                }
            }
        }
        logger.info("All roles assigned");

        return new ResponseEntity<>(new SimpleResponse(HttpStatus.OK.value(), "Roles assigned"), HttpStatus.OK);
    }

    private UserResource getUserResourceByEmail(UsersResource usersResource, String email) {
        List<UserRepresentation> users = usersResource.list();
        for (UserRepresentation user : users) {
            if (user.getEmail().equals(email)) {
                return usersResource.get(user.getId());
            }
        }
        return null;
    }

    private GroupResource getGroupResourceByName(GroupsResource groupsResource, String groupName) {
        List<GroupRepresentation> groups = groupsResource.groups();
        for (GroupRepresentation group : groups) {
            if (group.getName().equals(groupName)) {
                return groupsResource.group(group.getId());
            }
        }
        return null;
    }

    private ClientResource getClientResourceByName(ClientsResource clientsResource, String name) {
        List<ClientRepresentation> clients = clientsResource.findAll();
        for (ClientRepresentation client : clients) {
            if (client.getClientId().equals(name)) {
                return clientsResource.get(client.getId());
            }
        }
        return null;
    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<SimpleResponse> handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return new ResponseEntity<>(new SimpleResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
