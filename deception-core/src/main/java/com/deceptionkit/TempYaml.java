package com.deceptionkit;

import com.deceptionkit.model.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class TempYaml {



    public static void main(String[] args) throws JsonProcessingException {
        String temp = "{\"group1\":[{\"name\":\"role2\",\"description\":\"Role for role2\",\"composite\":false,\"clientRole\":false,\"realmName\":\"master\",\"clientName\":null}],\"group2\":[{\"name\":\"role2\",\"description\":\"Role for role2\",\"composite\":false,\"clientRole\":false,\"realmName\":\"master\",\"clientName\":null}],\"Sales\":[{\"name\":\"role2\",\"description\":\"Role for role2\",\"composite\":false,\"clientRole\":false,\"realmName\":\"master\",\"clientName\":null}],\"Marketing\":[{\"name\":\"role3\",\"description\":\"Role for role3\",\"composite\":false,\"clientRole\":true,\"realmName\":null,\"clientName\":\"client1\"}],\"Research and Development\":[{\"name\":\"role3\",\"description\":\"Role for role3\",\"composite\":false,\"clientRole\":true,\"realmName\":null,\"clientName\":\"client1\"}]}\n";

        //how to convert temp json string to Role class
        Map<String, List<Role>> map = new ObjectMapper().readValue(temp, Map.class);

        System.out.println(map.get("group1").get(0).getName());
        //System.out.println(role.getName());


    }
}
