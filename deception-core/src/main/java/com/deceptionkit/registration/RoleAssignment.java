package com.deceptionkit.registration;

import com.deceptionkit.model.Role;
import com.deceptionkit.model.User;
import com.deceptionkit.spring.response.SimpleResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.*;

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
    @ResponseBody
    public SimpleResponse assignRoles(@RequestParam(defaultValue = "master") String realm, @RequestBody Map<String, List<Role>> roleMappings) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<Response> responses = new ArrayList<>();
        for (String email : roleMappings.keySet()) {
            List<Role> roles = roleMappings.get(email);
            UserResource userRes = getUserResourceByEmail(usersResource, email);
            if (userRes == null) {
                logger.error("User not found: " + email);
                return new SimpleResponse(HttpStatus.NOT_FOUND.value(), "User not found: " + email);
            }
            for (Role role : roles) {
                if (role.isRealmRole()) {
                    RoleRepresentation roleRep = keycloak.realm(realm).roles().get(role.getName()).toRepresentation();
                    userRes.roles().realmLevel().add(Collections.singletonList(roleRep));
                } else if (role.isClientRole()) {
                    ClientResource clientRes = getClientResourceByName(keycloak.realm(realm).clients(), role.getClientName());
                    if (clientRes == null) {
                        logger.error("Client not found: " + role.getClientName());
                        return new SimpleResponse(HttpStatus.NOT_FOUND.value(), "Client not found: " + role.getClientName());
                    }
                    ClientRepresentation clientRep = clientRes.toRepresentation();
                    RoleRepresentation roleRep = keycloak.realm(realm).clients().get(clientRep.getId()).roles().get(role.getName()).toRepresentation();
                    userRes.roles().clientLevel(clientRep.getId()).add(Collections.singletonList(roleRep));
                }
            }
        }

        return new SimpleResponse(HttpStatus.OK.value(), "Roles assigned");
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
    @ResponseBody
    public SimpleResponse handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
