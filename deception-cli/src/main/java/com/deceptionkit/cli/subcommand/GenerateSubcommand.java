package com.deceptionkit.cli.subcommand;

import com.deceptionkit.cli.docker.DockerUtils;
import com.deceptionkit.cli.logic.IdProviderLogic;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;

@Command(name = "generate",
        description = "Generate a new deception component",
        abbreviateSynopsis = true,
        version = "deception-cli 0.1",
        mixinStandardHelpOptions = true)
public class GenerateSubcommand implements Runnable {

    @CommandLine.Option(names = {"-c", "--component"},
            description = "The component to generate",
            required = true)
    private String component;

    @CommandLine.Option(names = {"-d", "--definition"},
            description = "The definition of the component",
            required = true)
    private File definitionFile;

    @Override
    public void run() {
//        DockerConfig.checkAndRestartDaemon(false);
//        DockerUtils.createNewKeycloakDev();

        IdProviderLogic idProviderLogic = new IdProviderLogic();

        JsonNode baseNode = idProviderLogic.generateIdProviderResources(definitionFile);

        ArrayNode clients = (ArrayNode) baseNode.get("clients");
        ArrayNode groups = (ArrayNode) baseNode.get("groups");
        ArrayNode users = (ArrayNode) baseNode.get("users");
        ArrayNode roles = (ArrayNode) baseNode.get("roles");

        if (clients == null) {
            System.exit(1);
        }
        if (idProviderLogic.registerClients(clients)) {
            System.out.println("Clients registered successfully");
        } else {
            System.out.println("Clients failed to register");
            System.exit(1);
        }

        if (groups == null) {
            System.exit(1);
        }
        if (idProviderLogic.registerGroups(groups)) {
            System.out.println("Groups registered successfully");
        } else {
            System.out.println("Groups failed to register");
            System.exit(1);
        }

        if (users == null) {
            System.exit(1);
        }
        if (idProviderLogic.registerUsers(users)) {
            System.out.println("Users registered successfully");
        } else {
            System.out.println("Users failed to register");
            System.exit(1);
        }

        if (roles == null) {
            System.exit(1);
        }
        if (idProviderLogic.registerRoles(roles)) {
            System.out.println("Roles registered successfully");
        } else {
            System.out.println("Roles failed to register");
            System.exit(1);
        }

        ObjectNode groupToRolesMap = new ObjectNode(new ObjectMapper().getNodeFactory());

        for (JsonNode group : groups) {
            String groupName = group.get("name").asText();
            ArrayNode tempRoleNames = (ArrayNode) group.get("roles");
            if (tempRoleNames == null) {
                System.exit(1);
            }
            ArrayNode roleArray = new ObjectMapper().createArrayNode();
            for (JsonNode roleName : tempRoleNames) {
                ObjectNode roleObject = IdProviderLogic.getRoleNodeByName(roles, roleName.asText());
                if (roleObject == null) {
                    System.exit(1);
                }
                roleObject.remove("realmRole");
                roleArray.add(roleObject);
            }
            groupToRolesMap.set(groupName, roleArray);
        }

        if (idProviderLogic.assignRoles(groupToRolesMap)) {
            System.out.println("Roles mapped successfully");
        } else {
            System.out.println("Roles failed to map");
        }

        DockerUtils.exportKeycloakConfig("keycloak-config.json");

        DockerUtils.createKeycloakDockerfile("keycloak-config.json");

        //TODO: create also compose file?

        DockerUtils.stopKeycloakDev();
    }
}
