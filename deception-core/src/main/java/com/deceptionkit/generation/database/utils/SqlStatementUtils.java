package com.deceptionkit.generation.database.utils;

import java.util.List;

public class SqlStatementUtils {

    public static String getCreateUserStatement(String username, String password) {
        return "CREATE USER " + username + " WITH PASSWORD '" + password + "';";
    }

    public static String getGrantPrivilegesStatement(String username, String databaseName, List<String> privileges) {
        return "GRANT " + String.join(", ", privileges) + " ON DATABASE " + databaseName + " TO " + username + ";";
    }

    public static String getCreateDatabaseStatement(String databaseName) {
        return "CREATE DATABASE " + databaseName + ";";
    }


}
