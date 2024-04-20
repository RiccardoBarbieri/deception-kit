package com.deceptionkit.registration.idprovider;

import com.deceptionkit.model.idprovider.Role;
import com.deceptionkit.model.idprovider.RoleMap;
import com.deceptionkit.spring.apiversion.ApiVersion;
import com.deceptionkit.spring.response.ErrorResponse;
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

@Controller
@RequestMapping("/registration")
@ApiVersion({"1", "1.1"})
public class RoleAssignment {

    private final Keycloak keycloak;

    private final Logger logger;

    @Autowired
    public RoleAssignment(Keycloak keycloak) {
        this.keycloak = keycloak;
        this.logger = org.slf4j.LoggerFactory.getLogger(UserRegistration.class);
    }

    @PostMapping(value = "/assign/roles", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ErrorResponse> assignRoles(@RequestParam(name = "realm", defaultValue = "master") String realm, @RequestBody RoleMap roleMappings) {
        GroupsResource groupsResource = keycloak.realm(realm).groups();
        for (String groupName : roleMappings.keySet()) {
            List<Role> roles = roleMappings.get(groupName);
            GroupResource groupRes = getGroupResourceByName(groupsResource, groupName);
            if (groupRes == null) {
                logger.error("Group not found: " + groupName);
                return new ResponseEntity<>(new ErrorResponse("Group not found: " + groupName), HttpStatus.NOT_FOUND);
            }
            for (Role role : roles) {
                if (role.isRealmRole()) {
                    RoleRepresentation roleRep = keycloak.realm(realm).roles().get(role.getName()).toRepresentation();
                    groupRes.roles().realmLevel().add(Collections.singletonList(roleRep));
                } else if (role.isClientRole()) {
                    ClientResource clientRes = getClientResourceByName(keycloak.realm(realm).clients(), role.getClientName());
                    if (clientRes == null) {
                        logger.error("Client not found: " + role.getClientName());
                        return new ResponseEntity<>(new ErrorResponse("Client not found: " + role.getClientName()), HttpStatus.NOT_FOUND);
                    }
                    ClientRepresentation clientRep = clientRes.toRepresentation();
                    RoleRepresentation roleRep = keycloak.realm(realm).clients().get(clientRep.getId()).roles().get(role.getName()).toRepresentation();
                    groupRes.roles().clientLevel(clientRep.getId()).add(Collections.singletonList(roleRep));
                }
            }
        }
        logger.info("All roles assigned");

        return new ResponseEntity<>(new ErrorResponse("Roles assigned"), HttpStatus.OK);
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
    public ResponseEntity<ErrorResponse> handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
