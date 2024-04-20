package com.deceptionkit.generation.database;

import com.deceptionkit.spring.apiversion.ApiVersion;
import com.deceptionkit.yamlspecs.database.DatabaseDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/generation/database")
@ApiVersion({"1", "1.1"})
public class DatabaseGenerationController {

    @PostMapping(value = "/tables", consumes = "application/yaml", produces = "application/json")
    @ResponseBody
    public void generateTables(@RequestBody DatabaseDefinition databaseDefinition) {
        // Generate tables
    }

    @PostMapping(value = "/users", consumes = "application/yaml", produces = "application/json")
    @ResponseBody
    public void generateUsers(@RequestBody DatabaseDefinition databaseDefinition) {
        // Generate users
    }

    @PostMapping(value = "/data", consumes = "application/yaml", produces = "application/json")
    @ResponseBody
    public void generateData(@RequestBody DatabaseDefinition databaseDefinition) {
        // Generate data
    }

    @GetMapping(value = "/dockerfile", produces = "text/plain")
    @ResponseBody
    public String generateDockerfile() {
        return "FROM openjdk:8-jdk-alpine";
    }

}
