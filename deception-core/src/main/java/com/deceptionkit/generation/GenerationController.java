package com.deceptionkit.generation;

import com.deceptionkit.dockerfile.DockerfileBuilder;
import com.deceptionkit.dockerfile.commands.CommandBuilder;
import com.deceptionkit.dockerfile.options.CommandOptionsBuilder;
import com.deceptionkit.dockerfile.options.CopyCommandOptions;
import com.deceptionkit.generation.model.MockResources;
import com.deceptionkit.generation.utils.MockMergeHelper;
import com.deceptionkit.mockaroo.MockFactory;
import com.deceptionkit.model.Client;
import com.deceptionkit.model.Group;
import com.deceptionkit.model.Role;
import com.deceptionkit.model.User;
import com.deceptionkit.spring.apiversion.ApiVersion;
import com.deceptionkit.spring.response.IllegalArgumentResponse;
import com.deceptionkit.yamlspecs.idprovider.IdProviderDefinition;
import com.deceptionkit.yamlspecs.idprovider.client.ClientDefinition;
import com.deceptionkit.yamlspecs.idprovider.group.GroupDefinition;
import com.deceptionkit.yamlspecs.idprovider.role.RoleDefinition;
import com.deceptionkit.yamlspecs.idprovider.user.UserDefinition;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/generation")
@ApiVersion({"1", "1.1"})
public class GenerationController {

    private final Logger logger;


    public GenerationController() {
        this.logger = org.slf4j.LoggerFactory.getLogger(GenerationController.class);
    }

    @PostMapping(value = "/idprovider/resources", consumes = {"application/yaml", "application/yml", "text/yaml", "text/yml"}, produces = "application/json")
    @ResponseBody
    public MockResources generateIdProviderResources(@RequestBody IdProviderDefinition idProviderDefinition) {

        Integer totalGroups = idProviderDefinition.getSpecification().getGroups().getTotal();
        Integer totalUsers = idProviderDefinition.getSpecification().getUsers().getTotal();
        Integer credentialsPerUser = idProviderDefinition.getSpecification().getUsers().getCredentials_per_user();
        Integer groupsPerUser = idProviderDefinition.getSpecification().getUsers().getGroups_per_user();
        String domain = idProviderDefinition.getSpecification().getDomain();

        List<RoleDefinition> roleDefintions = idProviderDefinition.getSpecification().getRoles().getDefinitions();
        List<ClientDefinition> clientDefinitions = idProviderDefinition.getSpecification().getClients().getDefinitions();
        List<GroupDefinition> groupDefinitions = idProviderDefinition.getSpecification().getGroups().getDefinitions();
        List<UserDefinition> userDefinitions = idProviderDefinition.getSpecification().getUsers().getDefinitions();

        //consistency checks
        if (!clientRolesExist(clientDefinitions, roleDefintions)) {
            throw new RuntimeException("Client roles do not exist in role definitions");
        }
        if (!userGroupsExist(userDefinitions, groupDefinitions)) {
            throw new RuntimeException("User groups do not exist in group definitions");
        }
        if (!groupRolesExist(groupDefinitions, roleDefintions)) {
            throw new RuntimeException("Group roles do not exist in role definitions");
        }
        if (!clientRolesAreClientScoped(clientDefinitions, roleDefintions)) {
            throw new RuntimeException("Client roles are not client scoped");
        }
        if (groupsPerUser > totalGroups) {
            throw new RuntimeException("Groups per user cannot be greater than total groups");
        }
        //check uniqueness of client ids, role names, group names

        List<Group> mockGroups = MockFactory.getGroups(totalGroups - groupDefinitions.size());
        List<Group> allGroups = MockMergeHelper.mergeGroups(idProviderDefinition.getSpecification().getGroups().convertGroups(), mockGroups);

        List<User> mockUsers = MockFactory.getUsers(totalUsers - userDefinitions.size(), credentialsPerUser, domain);
        List<User> groupedUsers = MockMergeHelper.assignGroups(mockUsers, allGroups, groupsPerUser);
        List<User> allUsers = MockMergeHelper.mergeUsers(idProviderDefinition.getSpecification().getUsers().convertUsers(), groupedUsers);

        List<Role> allRoles = idProviderDefinition.getSpecification().getRoles().convertRoles();

        List<Client> allClients = idProviderDefinition.getSpecification().getClients().convertClients(allRoles);

        allGroups = MockMergeHelper.assignRoles(allGroups, allRoles);

        MockResources mockResources = new MockResources();
        mockResources.setGroups(allGroups);
        mockResources.setUsers(allUsers);
        mockResources.setClients(allClients);
        mockResources.setRoles(allRoles);
        return mockResources;
    }

    private boolean userGroupsExist(List<UserDefinition> userDefinitions, List<GroupDefinition> groupDefinitions) {
        boolean groupsExist = true;
        Map<String, List<String>> referencedGroups = new HashMap<>();
        for (UserDefinition userDefinition : userDefinitions) {
            referencedGroups.put(userDefinition.getEmail(), userDefinition.getGroups());
        }
        Set<String> definedGroups = groupDefinitions.stream().map(GroupDefinition::getName).collect(Collectors.toSet());
        for (List<String> groups : referencedGroups.values()) {
            if (!definedGroups.containsAll(groups)) {
                groupsExist = false;
                break;
            }
        }
        return groupsExist;
    }

    private boolean clientRolesExist(List<ClientDefinition> clientDefinitions, List<RoleDefinition> roleDefinitions) {
        boolean rolesExist = true;
        Map<String, List<String>> referencedRoles = new HashMap<>();
        for (ClientDefinition clientDefinition : clientDefinitions) {
            referencedRoles.put(clientDefinition.getId(), clientDefinition.getRoles());
        }
        Set<String> definedRoles = roleDefinitions.stream().map(RoleDefinition::getName).collect(Collectors.toSet());
        for (List<String> roles : referencedRoles.values()) {
            if (!definedRoles.containsAll(roles)) {
                rolesExist = false;
                break;
            }
        }
        return rolesExist;
    }

    private boolean clientRolesAreClientScoped(List<ClientDefinition> clientDefinitions, List<RoleDefinition> roleDefinitions) {
        boolean rolesAreClientScoped = true;
        for (ClientDefinition clientDefinition : clientDefinitions) {
            for (String roleName : clientDefinition.getRoles()) {
                RoleDefinition roleDefinition = roleDefinitions.stream().filter(r -> r.getName().equals(roleName)).toList().get(0);
                if (roleDefinition != null && !roleDefinition.getScope().isClientScope()) {
                    rolesAreClientScoped = false;
                    break;
                }
            }
        }
        return rolesAreClientScoped;
    }

    private boolean groupRolesExist(List<GroupDefinition> groupDefinitions, List<RoleDefinition> roleDefinitions) {
        boolean rolesExist = true;
        Map<String, List<String>> referencedRoles = new HashMap<>();
        for (GroupDefinition groupDefinition : groupDefinitions) {
            referencedRoles.put(groupDefinition.getName(), groupDefinition.getRoles());
        }
        Set<String> definedRoles = roleDefinitions.stream().map(RoleDefinition::getName).collect(Collectors.toSet());
        for (List<String> roles : referencedRoles.values()) {
            if (!definedRoles.containsAll(roles)) {
                rolesExist = false;
                break;
            }
        }
        return rolesExist;
    }

    private Yaml getYamlParser() {
        DumperOptions options = new DumperOptions();
        options.setWidth(50);
        options.setIndent(2);
        Constructor baseConstructor = new Constructor(IdProviderDefinition.class, new LoaderOptions());
        return new Yaml(baseConstructor, new Representer(options), options);
    }

    @GetMapping(value = "/idprovider/dockerfile", produces = "text/plain")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String generateDockerfile(
            @RequestParam(name = "enableHttp", required = false, defaultValue = "false") Boolean enableHttp,
            @RequestParam(name = "enableMetrics", required = false, defaultValue = "true") Boolean enableMetrics,
            @RequestParam(name = "enableHealth", required = false, defaultValue = "true") Boolean enableHealth,
            @RequestParam(name = "defaultCredentials") Boolean defaultCredentials,
            @RequestParam(name = "hostname", defaultValue = "localhost") String hostname,
            @RequestParam(name = "configFile") String configFile
    ) {


        String baseImage = "quay.io/keycloak/keycloak";
        DockerfileBuilder builder = new DockerfileBuilder();

        builder.addCommand(CommandBuilder.from(baseImage, "22.0.5").name("builder"));
        builder.addCommand(CommandBuilder.env("KC_HEALTH_ENABLED", enableHealth.toString()));
        builder.addCommand(CommandBuilder.env("KC_METRICS_ENABLED", enableMetrics.toString()));
        builder.addCommand(CommandBuilder.env("KC_HTTP_ENABLED", enableHttp.toString()));
        builder.addCommand(CommandBuilder.workdir("/opt/keycloak"));
        builder.addCommand(CommandBuilder.workdir("/opt/keycloak"));
        builder.addCommand(CommandBuilder.run("keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname \"CN=server\" -alias server -ext \"SAN:c=DNS:localhost,IP:127.0.0.1\" -keystore conf/server.keystore"));
        builder.addCommand(CommandBuilder.run("/opt/keycloak/bin/kc.sh", "build"));
        builder.addCommand(CommandBuilder.from(baseImage, "latest"));
        builder.addCommand(CommandBuilder.copy("opt/keycloak/", "/opt/keycloak/"), CommandOptionsBuilder.copy().from("builder"));
        builder.addCommand(CommandBuilder.copy(configFile, "/opt/keycloak/export.json"));
        builder.addCommand(CommandBuilder.run("/opt/keycloak/bin/kc.sh", "import", "--file", "/opt/keycloak/export.json"));
        if (defaultCredentials) {
            builder.addCommand(CommandBuilder.env("KEYCLOAK_ADMIN", "admin"));
            builder.addCommand(CommandBuilder.env("KEYCLOAK_ADMIN_PASSWORD", "admin"));
        }
        builder.addCommand(CommandBuilder.env("KC_HOSTNAME", hostname));
        builder.addCommand(CommandBuilder.entrypoint("/opt/keycloak/bin/kc.sh", "start", "--optimized"));

        return builder.build();

//        String Dockerfile;
//        Dockerfile = "FROM " + baseImage + " as builder\n" +
//                "ENV KC_HEALTH_ENABLED=" + enableHealth.toString() + "\n" +
//                "ENV KC_METRICS_ENABLED=" + enableMetrics.toString() + "\n" +
//                "ENV KC_HTTP_ENABLED=" + enableHttp.toString() + "\n" +
//                "WORKDIR /opt/keycloak\n" +
//                "RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname \"CN=server\" -alias server -ext \"SAN:c=DNS:localhost,IP:127.0.0.1\" -keystore conf/server.keystore\n" +
//                "RUN /opt/keycloak/bin/kc.sh build\n" +
//
//                "FROM quay.io/keycloak/keycloak:latest\n" +
//                "COPY --from=builder /opt/keycloak/ /opt/keycloak/\n" +
//                "COPY " + configFile + " /opt/keycloak/export.json\n" +
//                "RUN [\"/opt/keycloak/bin/kc.sh\", \"import\", \"--file\", \"/opt/keycloak/export.json\"]\n";
//        if (defaultCredentials) {
//            Dockerfile += "ENV KEYCLOAK_ADMIN=admin\n" +
//                    "ENV KEYCLOAK_ADMIN_PASSWORD=admin\n";
//        }
//        Dockerfile += "ENV KC_HOSTNAME=" + hostname + "\n" +
//                "ENTRYPOINT [\"/opt/keycloak/bin/kc.sh\", \"start\", \"--optimized\"]\n";
//
//        return new ResponseEntity<>(Dockerfile, HttpStatus.OK);
    }

    @ExceptionHandler({java.lang.Exception.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return e.getMessage();
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public IllegalArgumentResponse handleException(MethodArgumentTypeMismatchException e) {
        logger.error("Exception: ", e);
        return new IllegalArgumentResponse(
                e.getName(),
                e.getMessage(),
                Objects.requireNonNull(e.getRequiredType()).getSimpleName(),
                Objects.requireNonNull(e.getValue()).toString()
        );
    }
}
