package com.deceptionkit;

import com.deceptionkit.yamlspecs.database.DatabaseDefinition;
import com.deceptionkit.yamlspecs.idprovider.IdProviderDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TempYaml {

    public static void main(String[] args) {
        File file = new File("./src/main/resources/database_filedef.yaml");

        Constructor baseConstructor = new Constructor(DatabaseDefinition.class, new LoaderOptions());
//        baseConstructor.addTypeDescription(new TypeDescription(IdProviderDefinition.class, "!idprovider"));
//        baseConstructor.addTypeDescription(new TypeDescription(DatabaseDefinition.class, "!database"));

        Yaml parser = new Yaml(baseConstructor);

        DatabaseDefinition databaseDefinition = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            databaseDefinition = parser.loadAs(fis, DatabaseDefinition.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(databaseDefinition);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
