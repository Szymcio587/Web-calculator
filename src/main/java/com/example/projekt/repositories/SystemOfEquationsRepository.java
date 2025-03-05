package com.example.projekt.repositories;

import com.example.projekt.model.data.Savable;
import com.example.projekt.model.database.SystemOfEquationsRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SystemOfEquationsRepository extends MongoRepository<SystemOfEquationsRecord, String>  {

    List<Savable> findByUsername(String username);
}
