package com.deceptionkit.registration;

import com.deceptionkit.model.Role;
import com.deceptionkit.spring.response.SimpleResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoleRegistration {

    private final Logger logger;

    private final Keycloak keycloak;

    @Autowired
    public RoleRegistration(Keycloak keycloak) {
        this.keycloak = keycloak;
        this.logger = org.slf4j.LoggerFactory.getLogger(RoleRegistration.class);
    }

    private RoleRepresentation generateRoleRep(Role role) {
        RoleRepresentation roleRep = new RoleRepresentation();
        roleRep.setName(role.getName());
        roleRep.setDescription(role.getDescription());
        roleRep.setComposite(role.getComposite());
        roleRep.setClientRole(role.getClientRole());
        return roleRep;
    }

    @PostMapping(value = "/registerRole", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public SimpleResponse registerRole(@RequestParam(defaultValue = "master") String realm, @RequestBody List<Role> roles) {
        for (Role role : roles) {
            RoleRepresentation roleRep = generateRoleRep(role);
            if (role.isClientRole()) {
                ClientResource clientRep = getClientResourceByName(keycloak.realm(realm).clients(), role.getClientName());
                if (clientRep == null) {
                    logger.error("Client not found: " + role.getClientName());
                    return new SimpleResponse(HttpStatus.NOT_FOUND.value(), "Client not found: " + role.getClientName());
                }
                clientRep.roles().create(roleRep);
            } else if (role.isRealmRole()) {
                keycloak.realm(realm).roles().create(roleRep);
            } else {
                logger.error("Role type not specified: " + role.getName());
                return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), "Role type not specified: " + role.getName());
            }
        }
        return new SimpleResponse(HttpStatus.OK.value(), roles.size() + "roles registered");
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
