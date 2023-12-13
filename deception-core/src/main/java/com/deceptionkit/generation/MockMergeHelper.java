package com.deceptionkit.generation;

import com.deceptionkit.model.Group;
import com.deceptionkit.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    public static List<User> mergeUsers(List<User> users, List<User> mockUsers) {
        users.addAll(mockUsers);
        return users;
    }
}
