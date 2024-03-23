package com.deceptionkit.spring.configuration;

import com.deceptionkit.database.validation.model.ComponentName;
import com.deceptionkit.database.validation.repository.ComponentNameRepository;
import com.deceptionkit.database.validation.repository.TldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ComponentNameInit {

    private final Logger logger = LoggerFactory.getLogger(ComponentNameInit.class);
    private ComponentNameRepository compNameRepository;

    @Autowired
    private void setCompNameRepository(ComponentNameRepository compNameRepository) {
        this.compNameRepository = compNameRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updateComponents() {
        logger.debug("Updating components");

        List<ComponentName> componentNames = getSupportedComponents();
        logger.debug("Components: " + componentNames.size());
        componentNames.forEach(compName -> {
            if (!compNameRepository.existsByComponentName(compName.getComponentName())) {
                logger.debug("Adding component: " + compName.getComponentName());
                compNameRepository.insert(compName);
            }
        });

        logger.debug("Components updated");
    }

    private List<ComponentName> getSupportedComponents() {
        List<String> components = List.of("idprovider", "database");
        List<ComponentName> componentNames = new ArrayList<>();
        components.forEach(component -> {
            ComponentName componentName = new ComponentName();
            componentName.setComponentName(component);
            componentNames.add(componentName);
        });
        return componentNames;
    }

}
