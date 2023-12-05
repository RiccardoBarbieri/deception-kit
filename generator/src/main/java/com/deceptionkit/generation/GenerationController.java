package com.deceptionkit.generation;

import com.deceptionkit.componentspec.idmodel.IdProviderDefinition;
import com.deceptionkit.spring.response.SimpleResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Controller
public class GenerationController {

    private final Logger logger;


    public GenerationController() {
        this.logger = org.slf4j.LoggerFactory.getLogger(GenerationController.class);
    }

    @PostMapping(value = "/generateIdProviderResources", consumes = {"application/yaml", "application/yml"}, produces = "application/json")
    @ResponseBody
    public String generateResources(@RequestBody String idProviderDefinition) {

        Constructor baseConstructor = new Constructor(IdProviderDefinition.class, new LoaderOptions());
        Yaml yaml = new Yaml(baseConstructor);

        IdProviderDefinition idProvider = yaml.load(idProviderDefinition);

        System.out.println("__________________________________________________________");
        System.out.println(idProvider);
        System.out.println("__________________________________________________________");

        return "Hello World";
//        return new MockResources();
    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public SimpleResponse handleException(java.lang.Exception e) {
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
