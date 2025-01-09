package com.example.projekt.repositories;

import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.Savable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface InterpolationDataRepository extends MongoRepository<InterpolationData, String> {
    List<Savable> findByUsername(String username);
}