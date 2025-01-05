package com.example.projekt.repositories;

import com.example.projekt.model.data.InterpolationData;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface InterpolationDataRepository extends MongoRepository<InterpolationData, String> {
    List<InterpolationData> findByUsername(String username);
}