package com.deceptionkit.cli.subcommand;

import com.deceptionkit.cli.docker.DockerUtils;
import com.deceptionkit.cli.logic.DatabaseLogic;
import com.deceptionkit.cli.logic.IdProviderLogic;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Command(name = "generate",
        description = "Generate a new deception component",
        abbreviateSynopsis = true,
        version = "deception-cli 0.1",
        mixinStandardHelpOptions = true)
public class GenerateSubcommand implements Runnable {

    @CommandLine.ArgGroup(exclusive = false, multiplicity = "1..*")
    List<ComponentSpec> componentSpec;

    private void databaseGeneration(String component, File definitionFile) {
        System.out.println("Generating " + component + "\nfrom " + definitionFile.getAbsolutePath());

        DatabaseLogic databaseLogic = new DatabaseLogic();


        ArrayNode databases = (ArrayNode) databaseLogic.generateDatabases(definitionFile);
        System.out.println("Databases generated successfully.");

        ArrayNode tables = (ArrayNode) databaseLogic.generateTables(definitionFile);
        System.out.println("Tables generated successfully.");

        ArrayNode users = (ArrayNode) databaseLogic.generateUsers(definitionFile);
        System.out.println("Users generated successfully.");

        String dockerfile = databaseLogic.generateDockerfile(definitionFile);
        System.out.println("Dockerfile generated successfully.");


        if (databases == null) {
            System.exit(1);
        }
        if (tables == null) {
            System.exit(1);
        }
        if (users == null) {
            System.exit(1);
        }


        List<String> databaseSqlFileLines = new ArrayList<>();
        for (JsonNode database : databases) {
            databaseSqlFileLines.add(database.get("createDatabaseStm").asText());
        }


        List<String> tableSqlFileLines = new ArrayList<>();
        for (JsonNode table : tables) {
            tableSqlFileLines.add("\\c " + table.get("databaseName").asText() + ";");
            tableSqlFileLines.add(table.get("tableCreateStm").asText());
            ArrayNode inserts = (ArrayNode) table.get("inserts");
            if (inserts != null) {
                for (JsonNode insert : inserts) {
                    tableSqlFileLines.add(insert.asText());
                }
            }
        }


        List<String> userSqlFileLines = new ArrayList<>();
        for (JsonNode user : users) {
            userSqlFileLines.add(user.get("createUserStm").asText());
            ArrayNode grants = (ArrayNode) user.get("grantPrivilegesStm");
            if (grants != null) {
                for (JsonNode grant : grants) {
                    userSqlFileLines.add(grant.asText());
                }
            }
        }

        File directory = new File(component);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File databaseSqlFile = new File(component + File.separator + "database.sql");
        try (FileOutputStream fos = new FileOutputStream(databaseSqlFile)) {
            for (String line : databaseSqlFileLines) {
                fos.write((line + "\n").getBytes());
            }
        } catch (Exception e) {
            System.out.println("Failed to write database.sql");
            System.exit(1);
        }


        File tableSqlFile = new File(component + File.separator + "tables.sql");
        try (FileOutputStream fos = new FileOutputStream(tableSqlFile)) {
            for (String line : tableSqlFileLines) {
                fos.write((line + "\n").getBytes());
            }
        } catch (Exception e) {
            System.out.println("Failed to write tables.sql");
            System.exit(1);
        }


        File userSqlFile = new File(component + File.separator + "users.sql");
        try (FileOutputStream fos = new FileOutputStream(userSqlFile)) {
            for (String line : userSqlFileLines) {
                fos.write((line + "\n").getBytes());
            }
        } catch (Exception e) {
            System.out.println("Failed to write users.sql");
            System.exit(1);
        }


        File dockerfileFile = new File(component + File.separator + "Dockerfile-" + component);
        try (FileOutputStream fos = new FileOutputStream(dockerfileFile)) {
            fos.write(dockerfile.getBytes());
        } catch (Exception e) {
            System.out.println("Failed to write Dockerfile");
            System.exit(1);
        }


        System.out.println("Run the following command to build the image:\n");
        System.out.println("docker build -f " + "Dockerfile-" + component + " -t [repository/]" + component + " .\n");
        System.out.println("Run the following command to create and run a container:\n");
        System.out.println("docker run -d -p 5432:5432 --name " + component + " [repository/]" + component + "\n");

    }

    private void idproviderGeneration(String component, File definitionFile) {
//        DockerConfig.checkAndRestartDaemon(false);
        System.out.println("Starting temporary keycloak dev instance...");
        DockerUtils.createNewKeycloakDev();

        System.out.println("Finished starting temporary keycloak dev instance...");

        System.out.println("Generating " + component + "\nfrom " + definitionFile.getAbsolutePath());

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

        File directory = new File(component);
        if (!directory.exists()) {
            directory.mkdir();
        }

        DockerUtils.exportKeycloakConfig(component + File.separator + "keycloak-config.json");

        Boolean defaultCredentials = true;
        String dockerfile = idProviderLogic.generateDockerfile(
                false,
                true,
                true,
                defaultCredentials,
                "localhost",
                "keycloak-config.json"
        );

        File dockerfileFile = new File(component + File.separator + "Dockerfile-" + component);
        try (FileOutputStream fos = new FileOutputStream(dockerfileFile)) {
            fos.write(dockerfile.getBytes());
        } catch (Exception e) {
            System.out.println("Failed to write Dockerfile");
            System.exit(1);
        }

        System.out.println("Run the following command to build the image:\n");
        System.out.println("docker build -f " + "Dockerfile-" + component + " -t [repository/]" + component + " .\n");
        System.out.println("Run the following command to create and run a container:\n");
        if (defaultCredentials) {
            System.out.println("docker run -d -p 8443:8443 --name " + component + " [repository/]" + component + "\n");
        } else {
            System.out.println("docker run -d -p 8443:8443 -e KEYCLOAK_USER=<username> -e KEYCLOAK_PASSWORD=<password> --name " + component + " [repository/]" + component + "\n");
        }

        DockerUtils.removeKeycloakDev();
    }

    @Override
    public void run() {

        for (ComponentSpec spec : componentSpec) {

            if (spec.component.equals("idprovider")) {
                System.out.println("Generating idprovider with definition file: " + spec.definitionFile);
                idproviderGeneration(spec.component, spec.definitionFile);
            } else if (spec.component.equals("database")) {
                System.out.println("Generating database with definition file: " + spec.definitionFile);
                databaseGeneration(spec.component, spec.definitionFile);
            } else {
                System.out.println("Component " + spec.component + " not recognized");
                System.exit(1);
            }
            if (componentSpec.indexOf(spec) < componentSpec.size() - 1) {
                System.out.println("------------------------------------------------------------------------------------------");
            }
        }

        System.exit(0);
    }

    static class ComponentSpec {
        @CommandLine.Option(names = {"-c", "--component"},
                description = "The component to generate",
                required = true)
        private String component;

        @CommandLine.Option(names = {"-d", "--definition"},
                description = "The definition of the component",
                required = true)
        private File definitionFile;
    }
}
