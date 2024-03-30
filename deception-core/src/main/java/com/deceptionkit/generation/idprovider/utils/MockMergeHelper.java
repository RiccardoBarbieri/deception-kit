package com.deceptionkit.generation.idprovider.utils;

import com.deceptionkit.model.idprovider.Group;
import com.deceptionkit.model.idprovider.Role;
import com.deceptionkit.model.idprovider.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockMergeHelper {

    private MockMergeHelper() {

    }

    public static List<Group> mergeGroups(List<Group> groups, List<Group> mockGroups) {
        groups.addAll(mockGroups);
        return groups;
    }

    public static List<User> assignGroups(List<User> users, List<Group> groups, int groupsPerUser) {
        for (User user : users) {
            List<Integer> ints = new Random().ints(1, groups.size()).distinct().limit(groupsPerUser).boxed().toList();
            List<String> tempGroups = new ArrayList<>();
            for (Integer i : ints) {
                tempGroups.add(groups.get(i).getName());
            }
            user.setGroups(tempGroups);
        }
        return users;
    }

    public static List<Group> assignRoles(List<Group> groups, List<Role> roles) {
        for (Group group : groups) {
            List<Integer> ints = new Random().ints(1, roles.size()).distinct().limit(1).boxed().toList();
            List<String> tempRoles = new ArrayList<>();
            for (Integer i : ints) {
                tempRoles.add(roles.get(i).getName());
            }
            group.setRoles(tempRoles);
        }
        return groups;
    }

    public static List<User> mergeUsers(List<User> users, List<User> mockUsers) {
        users.addAll(mockUsers);
        return users;
    }
}
