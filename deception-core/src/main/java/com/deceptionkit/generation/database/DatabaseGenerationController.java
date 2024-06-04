package com.deceptionkit.generation.database;

import com.deceptionkit.dockerfile.DockerfileBuilder;
import com.deceptionkit.dockerfile.commands.CommandBuilder;
import com.deceptionkit.generation.database.model.DatabaseResources;
import com.deceptionkit.generation.database.model.TableResources;
import com.deceptionkit.generation.database.model.UserResources;
import com.deceptionkit.generation.database.utils.RandomUtils;
import com.deceptionkit.generation.database.utils.SqlStatementUtils;
import com.deceptionkit.mockaroo.MockFactory;
import com.deceptionkit.spring.apiversion.ApiVersion;
import com.deceptionkit.spring.response.ErrorResponse;
import com.deceptionkit.spring.yaml.YamlErrorMessageUtils;
import com.deceptionkit.yamlspecs.database.DatabaseDefinition;
import com.deceptionkit.yamlspecs.database.databases.DatabasesDefinition;
import com.deceptionkit.yamlspecs.database.table.TableDefinition;
import com.deceptionkit.yamlspecs.database.user.AccessibleDatabases;
import com.deceptionkit.yamlspecs.database.user.UserDefinition;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.constructor.ConstructorException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/generation/database")
@ApiVersion({"1", "1.1"})
public class DatabaseGenerationController {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseGenerationController.class);


    @PostMapping(value = "/databases", consumes = "application/yaml", produces = "application/json")
    @ResponseBody
    public List<DatabaseResources> generateDatabases(@RequestBody DatabaseDefinition databaseDefinition) {
        List<DatabasesDefinition> databases = databaseDefinition.getSpecification().getDatabases().getDefinitions();

        List<DatabaseResources> databaseResources = new ArrayList<>(databases.size());

        for (DatabasesDefinition database : databases) {
            DatabaseResources databaseResource = new DatabaseResources();
            databaseResource.setDatabaseName(database.getName());
            databaseResource.setCreateDatabaseStm(SqlStatementUtils.getCreateDatabaseStatement(database.getName()));

            databaseResources.add(databaseResource);
        }

        return databaseResources;
    }

    private boolean checkDatabaseExists(String databaseName, List<DatabasesDefinition> databases) {
        for (DatabasesDefinition database : databases) {
            if (database.getName().equals(databaseName)) {
                return true;
            }
        }
        return false;
    }

    @PostMapping(value = "/tables", consumes = "application/yaml", produces = "application/json")
    @ResponseBody
    public List<TableResources> generateTables(@RequestBody DatabaseDefinition databaseDefinition) {
        List<TableDefinition> tables = databaseDefinition.getSpecification().getTables().getDefinitions();

        // checking that databases are declared
        List<DatabasesDefinition> databases = databaseDefinition.getSpecification().getDatabases().getDefinitions();

        int minCols = databaseDefinition.getSpecification().getTables().getMin_columns_per_table();
        int maxCols = databaseDefinition.getSpecification().getTables().getMax_columns_per_table();

        int minRows = databaseDefinition.getSpecification().getTables().getMin_rows_per_table();
        int maxRows = databaseDefinition.getSpecification().getTables().getMax_rows_per_table();

        List<TableResources> tableResources = new ArrayList<>(tables.size());

        for (TableDefinition tableDefinition : tables) {
            if (!checkDatabaseExists(tableDefinition.getDatabase(), databases)) {
                throw new IllegalArgumentException("Database " + tableDefinition.getDatabase() + " does not exist");
            }

            int numCols = RandomUtils.generateRandomNumber(minCols, maxCols);
            int numRow = RandomUtils.generateRandomNumber(minRows, maxRows);

            ArrayNode columnSchema = MockFactory.getTableTypes(tableDefinition.getPrompt(), numCols);
            String tableCreateStm = MockFactory.getTableDefinition(columnSchema, tableDefinition.getName());

            List<String> inserts = MockFactory.getMockSql(columnSchema, numRow, tableDefinition.getName());

            TableResources tableResource = new TableResources();

            tableResource.setDatabaseName(tableDefinition.getDatabase());
            tableResource.setTableName(tableDefinition.getName());
            tableResource.setTableCreateStm(tableCreateStm);
            tableResource.setInserts(inserts);

            tableResources.add(tableResource);
        }

        return tableResources;
    }

    @PostMapping(value = "/users", consumes = "application/yaml", produces = "application/json")
    @ResponseBody
    public List<UserResources> generateUsers(@RequestBody DatabaseDefinition databaseDefinition) {
        List<UserDefinition> users = databaseDefinition.getSpecification().getUsers().getDefinitions();

        List<UserResources> userResources = new ArrayList<>(users.size());

        for (UserDefinition userDefinition : users) {
            String username = userDefinition.getUsername();
            String password = userDefinition.getPassword();

            String createUserStm = SqlStatementUtils.getCreateUserStatement(username, password);

            List<String> grantPrivilegesStm = new ArrayList<>();
            for (AccessibleDatabases accDb : userDefinition.getDatabases()) {
                List<String> permissions = accDb.getPermissions();
                String grantPrivileges = SqlStatementUtils.getGrantPrivilegesStatement(username, accDb.getName(), permissions);
            }

            UserResources userResource = new UserResources();
            userResource.setUsername(username);
            userResource.setCreateUserStm(createUserStm);
            userResource.setGrantPrivilegesStm(grantPrivilegesStm);

            userResources.add(userResource);
        }

        return userResources;
    }

    @PostMapping(value = "/dockerfile", produces = "text/plain")
    @ResponseBody
    public String generateDockerfile(@RequestBody DatabaseDefinition databaseDefinition) {
        String baseImage = "FROM postgres:alpine";
        String user = databaseDefinition.getSpecification().getUsers().getMain_user().getUsername();
        String password = databaseDefinition.getSpecification().getUsers().getMain_user().getPassword();
        String database = databaseDefinition.getSpecification().getDatabases().getDefinitions().get(0).getName();

        DockerfileBuilder builder = new DockerfileBuilder();

        builder.addCommand(CommandBuilder.from(baseImage, "latest"));
        builder.addCommand(CommandBuilder.env("POSTGRES_USER", user));
        builder.addCommand(CommandBuilder.env("POSTGRES_PASSWORD", password));
        builder.addCommand(CommandBuilder.env("POSTGRES_DB", database));


        builder.addCommand(CommandBuilder.add("databases.sql", "/docker-entrypoint-initdb.d/databases.sql"));
        builder.addCommand(CommandBuilder.add("tables.sql", "/docker-entrypoint-initdb.d/tables.sql"));
        builder.addCommand(CommandBuilder.add("users.sql", "/docker-entrypoint-initdb.d/users.sql"));


        return builder.build();
    }

    @ExceptionHandler({java.lang.Exception.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(java.lang.Exception e) {
        logger.error("Exception: ", e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ConstructorException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(ConstructorException e) {
        logger.error("Exception: ", e);
        String message = (new YamlErrorMessageUtils(e.getMessage()).getMessage());
        return new ErrorResponse(message);
    }

}
