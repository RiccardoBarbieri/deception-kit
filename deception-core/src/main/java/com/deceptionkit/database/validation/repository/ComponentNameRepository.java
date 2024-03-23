package com.deceptionkit.database.validation.repository;

import com.deceptionkit.database.validation.model.ComponentName;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ComponentNameRepository extends MongoRepository<ComponentName, String> {

    Boolean existsByComponentName(String component);
}
