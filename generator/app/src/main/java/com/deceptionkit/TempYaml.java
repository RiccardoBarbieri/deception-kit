package com.deceptionkit;

import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class TempYaml {

    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        Map<String, Object> map;
        try {
            map = yaml.load(new FileReader("filedef.yaml"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println(map);

    }
}
