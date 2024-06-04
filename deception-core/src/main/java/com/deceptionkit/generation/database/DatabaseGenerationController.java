package com.deceptionkit.generation.database;

import com.deceptionkit.generation.database.model.TableResources;
import com.deceptionkit.generation.database.utils.RandomUtils;
import com.deceptionkit.mockaroo.MockFactory;
import com.deceptionkit.spring.apiversion.ApiVersion;
import com.deceptionkit.yamlspecs.database.DatabaseDefinition;
import com.deceptionkit.yamlspecs.database.table.TableDefinition;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/generation/database")
@ApiVersion({"1", "1.1"})
public class DatabaseGenerationController {

    @PostMapping(value = "/tables", consumes = "application/yaml", produces = "application/json")
    @ResponseBody
    public List<TableResources> generateTables(@RequestBody DatabaseDefinition databaseDefinition) {
        List<TableDefinition> tables = databaseDefinition.getSpecification().getTables().getDefinitions();

        int minCols = databaseDefinition.getSpecification().getTables().getMin_columns_per_table();
        int maxCols = databaseDefinition.getSpecification().getTables().getMax_columns_per_table();

        int minRows = databaseDefinition.getSpecification().getTables().getMin_rows_per_table();
        int maxRows = databaseDefinition.getSpecification().getTables().getMax_rows_per_table();

        List<TableResources> tableResources = new ArrayList<>(tables.size());

        for (TableDefinition tableDefinition : tables) {
            int numCols = RandomUtils.generateRandomNumber(minCols, maxCols);
            int numRow = RandomUtils.generateRandomNumber(minRows, maxRows);

            ArrayNode columnSchema = MockFactory.getTableTypes(tableDefinition.getPrompt(), numCols);
            String tableCreateStm = MockFactory.getTableDefinition(columnSchema, tableDefinition.getName());

            List<String> inserts = MockFactory.getMockSql(columnSchema, numRow, tableDefinition.getName());

            TableResources tableResource = new TableResources();

            tableResource.setDatabaseName(tableDefinition.getDatabase());
            tableResource.setTableName(tableDefinition.getName());
            tableResource.setTableDefinition(tableCreateStm);
            tableResource.setInserts(inserts);

            tableResources.add(tableResource);
        }

        return tableResources;
    }

    @PostMapping(value = "/users", consumes = "application/yaml", produces = "application/json")
    @ResponseBody
    public void generateUsers(@RequestBody DatabaseDefinition databaseDefinition) {
        // Generate users
    }

//    @PostMapping(value = "/data", consumes = "application/yaml", produces = "application/json")
//    @ResponseBody
//    public void generateData(@RequestBody DatabaseDefinition databaseDefinition) {
//        // Generate data
//    }

    @PostMapping(value = "/dockerfile", produces = "text/plain")
    @ResponseBody
    public String generateDockerfile(@RequestBody DatabaseDefinition databaseDefinition) {
        return "FROM openjdk:8-jdk-alpine";
    }

}
