package com.example.projekt.repositories;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.Savable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IntegrationDataRepository extends MongoRepository<IntegrationData, String> {
    List<Savable> findByUsername(String username);
}
