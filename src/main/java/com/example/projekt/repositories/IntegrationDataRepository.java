package com.example.projekt.repositories;

import com.example.projekt.model.data.Savable;
import com.example.projekt.model.database.IntegrationRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IntegrationDataRepository extends MongoRepository<IntegrationRecord, String> {
    List<Savable> findByUsername(String username);
}
