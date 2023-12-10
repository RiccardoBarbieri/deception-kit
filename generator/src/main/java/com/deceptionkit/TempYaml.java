package com.deceptionkit;

import com.deceptionkit.generation.DefaultsProvider;

import java.util.Map;

public class TempYaml {



    public static void main(String[] args) {
        System.out.println(DefaultsProvider.getClientDefault("attributes", Map.class));
    }
}
