package com.deceptionkit.database.validation.repository;

import com.deceptionkit.database.validation.model.Tld;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TldRepository extends MongoRepository<Tld, String> {

    Boolean existsByTld(String tld);

}
