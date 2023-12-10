package com.deceptionkit;

import com.deceptionkit.yamlspecs.idprovider.IdProviderDefinition;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class TempYaml {

    public static void main(String[] args) {
        DumperOptions options = new DumperOptions();
        options.setWidth(50);
        options.setIndent(2);
//        Yaml yaml = new Yaml(options);

        Constructor baseConstructor = new Constructor(IdProviderDefinition.class, new LoaderOptions());

        Yaml yaml = new Yaml(baseConstructor, new Representer(options), options);

        IdProviderDefinition idProviderDefinition = new IdProviderDefinition();
        try {
            idProviderDefinition = yaml.load(new FileReader("src/main/resources/filedef.yaml"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("_________________________");
        System.out.println(yaml.dump(idProviderDefinition));
    }
}
