package com.deceptionkit.registration;

import com.deceptionkit.model.Client;
import com.deceptionkit.model.Role;
import com.deceptionkit.spring.response.SimpleResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientRegistration {

    private final Logger logger;

    private final Keycloak keycloak;

    @Autowired
    public ClientRegistration(Keycloak keycloak) {
        this.keycloak = keycloak;
        this.logger = org.slf4j.LoggerFactory.getLogger(ClientRegistration.class);
    }

    private ClientRepresentation generateClientRep(Client client) {
        ClientRepresentation clientRep = new ClientRepresentation();
        clientRep.setClientId(client.getClientId());
        clientRep.setName(client.getName());
        clientRep.setDescription(client.getDescription());
        clientRep.setRootUrl(client.getRootUrl());
        clientRep.setAdminUrl(client.getAdminUrl());
        clientRep.setBaseUrl(client.getBaseUrl());
        clientRep.setEnabled(client.getEnabled());
        clientRep.setClientAuthenticatorType(client.getClientAuthenticatorType());
        clientRep.setRedirectUris(client.getRedirectUris());
        clientRep.setWebOrigins(client.getWebOrigins());
        clientRep.setProtocol(client.getProtocol());
        clientRep.setStandardFlowEnabled(client.getStandardFlowEnabled());
        clientRep.setImplicitFlowEnabled(client.getImplicitFlowEnabled());
        clientRep.setDirectAccessGrantsEnabled(client.getDirectAccessGrantsEnabled());
        clientRep.setPublicClient(client.getPublicClient());
        clientRep.setAttributes(client.getAttributes());
        return clientRep;
    }

    private RoleRepresentation getRoleRep(Role role) {
        RoleRepresentation roleRep = new RoleRepresentation();
        roleRep.setName(role.getName());
        roleRep.setDescription(role.getDescription());
        roleRep.setComposite(role.getComposite());
        roleRep.setClientRole(role.getClientRole());
        return roleRep;
    }

    @PostMapping(value = "/registerClients", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public SimpleResponse registerClient(@RequestParam(defaultValue = "master") String realm, @RequestBody List<Client> clients) {
        List<Response> responses = new ArrayList<>();
        for (Client c : clients) {
            ClientRepresentation client = generateClientRep(c);

            Response response = keycloak.realm(realm).clients().create(client);
            responses.add(response);

            if (response.getStatus() != 201) {
                logger.error("Error creating client: " + c.getClientId());
                return new SimpleResponse(response.getStatus(), response.getStatusInfo().toString());
            } else {
                response.close();
            }

            for (Role role : c.getRoles()) {
                keycloak.realm(realm).clients().get(client.getId()).roles().create(getRoleRep(role));
            }
        }
        return new SimpleResponse(HttpStatus.CREATED.value(), responses.size() + " clients registered");
    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SimpleResponse handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}
