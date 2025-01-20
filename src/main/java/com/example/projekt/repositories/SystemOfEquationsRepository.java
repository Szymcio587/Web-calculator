package com.example.projekt.repositories;

import com.example.projekt.model.data.Savable;
import com.example.projekt.model.data.SystemOfEquationsData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SystemOfEquationsRepository extends MongoRepository<SystemOfEquationsData, String>  {

    List<Savable> findByUsername(String username);
}
