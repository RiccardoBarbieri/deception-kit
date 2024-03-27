package com.deceptionkit.mockaroo;

import com.deceptionkit.mockaroo.model.idprovider.GroupMock;
import com.deceptionkit.mockaroo.model.idprovider.UserMock;
import com.deceptionkit.model.Group;
import com.deceptionkit.model.User;

import java.util.List;

public class MockFactory {

    private final static MockarooApi api = new MockarooApi();


    private MockFactory() {

    }

    public static List<Group> getGroups(int count) {
        GroupMock groupMock = new GroupMock();
        return groupMock.getMocks(api, count);
    }

    public static List<User> getUsers(int count, int credentialsCount, String domain) {
        UserMock userMock = new UserMock(credentialsCount, domain);
        return userMock.getMocks(api, count);
    }
}
