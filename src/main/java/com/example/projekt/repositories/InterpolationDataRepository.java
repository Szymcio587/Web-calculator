package com.example.projekt.repositories;

import com.example.projekt.model.data.Savable;
import com.example.projekt.model.database.InterpolationRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface InterpolationDataRepository extends MongoRepository<InterpolationRecord, String> {
    List<Savable> findByUsername(String username);
}