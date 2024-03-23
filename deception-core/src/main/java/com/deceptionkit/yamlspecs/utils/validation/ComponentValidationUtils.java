package com.deceptionkit.yamlspecs.utils.validation;

import com.deceptionkit.database.validation.repository.ComponentNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComponentValidationUtils {

    private static ComponentNameRepository compNameRepository;

    public Boolean isComponentValid(String component) {
        return componentValid(component.toLowerCase());
    }

    private Boolean componentValid(String component) {
        return compNameRepository.existsByComponentName(component);
    }

    @Autowired
    public void setComponentRepository(ComponentNameRepository tldRepository) {
        ComponentValidationUtils.compNameRepository = tldRepository;
    }
}
