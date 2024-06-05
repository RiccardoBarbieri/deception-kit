package com.deceptionkit.database.validation.repository;

import com.deceptionkit.database.validation.model.Tld;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TldRepository extends MongoRepository<Tld, String> {

    Boolean existsByTld(String tld);

}
