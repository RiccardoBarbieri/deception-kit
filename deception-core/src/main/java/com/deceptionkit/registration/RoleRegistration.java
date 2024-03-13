package com.deceptionkit.registration;

import com.deceptionkit.model.Role;
import com.deceptionkit.spring.apiversion.ApiVersion;
import com.deceptionkit.spring.response.ErrorResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/registration")
@ApiVersion({"1", "1.1"})
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

    @PostMapping(value = "/roles", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ErrorResponse> registerRole(@RequestParam(name = "realm", defaultValue = "master") String realm, @RequestBody List<Role> roles) {
        for (Role role : roles) {
            RoleRepresentation roleRep = generateRoleRep(role);
            if (role.isClientRole()) {
                ClientResource clientRep = getClientResourceByName(keycloak.realm(realm).clients(), role.getClientName());
                if (clientRep == null) {
                    logger.error("Client not found: " + role.getClientName());
                    return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Client not found: " + role.getClientName()), HttpStatus.NOT_FOUND);
                }
                clientRep.roles().create(roleRep);
            } else if (role.isRealmRole()) {
                keycloak.realm(realm).roles().create(roleRep);
            } else {
                logger.error("Role type not specified: " + role.getName());
                return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Role type not specified: " + role.getName()), HttpStatus.BAD_REQUEST);
            }
        }
        logger.info("All roles registered");
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.CREATED.value(), roles.size() + "roles registered"), HttpStatus.CREATED);
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
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
