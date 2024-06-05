package com.deceptionkit.yamlspecs.database;

import com.deceptionkit.yamlspecs.database.connection.ConnectionSpecification;
import com.deceptionkit.yamlspecs.database.databases.DatabasesSpecification;
import com.deceptionkit.yamlspecs.database.table.TableSpecification;
import com.deceptionkit.yamlspecs.database.user.UserSpecification;

public class DatabaseSpec {

    private ConnectionSpecification connection;
    private DatabasesSpecification databases;
    private TableSpecification tables;
    private UserSpecification users;

    public ConnectionSpecification getConnection() {
        return connection;
    }

    public void setConnection(ConnectionSpecification connection) {
        this.connection = connection;
    }

    public DatabasesSpecification getDatabases() {
        return databases;
    }

    public void setDatabases(DatabasesSpecification databases) {
        this.databases = databases;
    }

    public TableSpecification getTables() {
        return tables;
    }

    public void setTables(TableSpecification tables) {
        this.tables = tables;
    }

    public UserSpecification getUsers() {
        return users;
    }

    public void setUsers(UserSpecification users) {
        this.users = users;
    }
}
