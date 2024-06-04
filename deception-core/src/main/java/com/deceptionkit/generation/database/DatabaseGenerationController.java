package com.deceptionkit.generation.database;

import com.deceptionkit.generation.database.utils.RandomUtils;
import com.deceptionkit.spring.apiversion.ApiVersion;
import com.deceptionkit.yamlspecs.database.DatabaseDefinition;
import com.deceptionkit.yamlspecs.database.table.TableDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/generation/database")
@ApiVersion({"1", "1.1"})
public class DatabaseGenerationController {

    @PostMapping(value = "/tables", consumes = "application/yaml", produces = "application/json")
    @ResponseBody
    public void generateTables(@RequestBody DatabaseDefinition databaseDefinition) {
        List<TableDefinition> tables = databaseDefinition.getSpecification().getTables().getDefinitions();

        int minCols = databaseDefinition.getSpecification().getTables().getMin_columns_per_table();
        int maxCols = databaseDefinition.getSpecification().getTables().getMax_columns_per_table();

        for (TableDefinition table : tables) {
            int numCols = RandomUtils.generateRandomNumber(minCols, maxCols);


        }


        // Generate tables
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
