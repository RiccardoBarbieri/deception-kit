package com.deceptionkit.generation;

import com.deceptionkit.generation.model.MockResources;
import com.deceptionkit.mockaroo.MockarooApi;
import com.deceptionkit.mockaroo.model.GroupMock;
import com.deceptionkit.mockaroo.model.UserMock;
import com.deceptionkit.model.Group;
import com.deceptionkit.model.User;
import com.deceptionkit.spring.response.SimpleResponse;
import com.deceptionkit.yamlspecs.idprovider.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GenerationController {

    private final Logger logger;


    public GenerationController() {
        this.logger = org.slf4j.LoggerFactory.getLogger(GenerationController.class);
    }

    @PostMapping(value = "/generateIdProviderResources", consumes = {"application/yaml", "application/yml"}, produces = "application/json")
    @ResponseBody
    public MockResources generateResources(@RequestBody String componentDefinition) {
        Yaml yaml = getYamlParser();

        IdProviderDefinition idProviderDefinition = yaml.load(componentDefinition);

        Integer totalGroups = idProviderDefinition.getSpecification().getGroups().getTotal();
        Integer totalUsers = idProviderDefinition.getSpecification().getUsers().getTotal();
        Integer credentialsPerUser = idProviderDefinition.getSpecification().getUsers().getCredentials_per_user();
        Integer groupsPerUser = idProviderDefinition.getSpecification().getUsers().getGroups_per_user();
        String domain = idProviderDefinition.getSpecification().getDomain();

        List<RoleDefinition> roleDefintions = idProviderDefinition.getSpecification().getRoles().getDefinitions();
        List<ClientDefinition> clientDefinitions = idProviderDefinition.getSpecification().getClients().getDefinitions();
        List<GroupDefinition> groupDefinitions = idProviderDefinition.getSpecification().getGroups().getDefinitions();
        List<UserDefinition> userDefinitions = idProviderDefinition.getSpecification().getUsers().getDefinitions();

        if (!clientRolesExist(clientDefinitions, roleDefintions)) {
            throw new RuntimeException("Client roles do not exist in role definitions");
        }
        if (!userGroupsExist(userDefinitions, groupDefinitions)) {
            throw new RuntimeException("User groups do not exist in group definitions");
        }

        List<Group> mockGroups = (new GroupMock()).getMocks(new MockarooApi(), totalGroups - groupDefinitions.size());
        List<Group> allGroups = new ArrayList<>();
        allGroups.addAll(mockGroups);
        allGroups.addAll(idProviderDefinition.getSpecification().getGroups().getGroups());

        List<User> mockUsers = (new UserMock(credentialsPerUser, domain)).getMocks(new MockarooApi(), totalUsers - userDefinitions.size());
        List<User> allUsers = new ArrayList<>();
        for (User user : mockUsers) {
            List<String> userGroups = new ArrayList<>();
            for (int i = 0; i < groupsPerUser; i++) {
                //do not reuse already assigned groups for the same user
                userGroups.add(mockGroups.get(new Random().nextInt(mockGroups.size())).getName());
            }
            user.setGroups(userGroups);
        }
        allUsers.addAll(mockUsers);
        allUsers.addAll(idProviderDefinition.getSpecification().getUsers().getUsers());



        MockResources mockResources = new MockResources();
        mockResources.setGroups(allGroups);
        mockResources.setUsers(allUsers);
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

    private Yaml getYamlParser() {
        DumperOptions options = new DumperOptions();
        options.setWidth(50);
        options.setIndent(2);
        Constructor baseConstructor = new Constructor(IdProviderDefinition.class, new LoaderOptions());
        return new Yaml(baseConstructor, new Representer(options), options);
    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SimpleResponse handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
