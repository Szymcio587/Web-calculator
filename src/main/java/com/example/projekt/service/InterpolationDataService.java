package com.example.projekt.service;

import com.example.projekt.model.Point;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.repositories.InterpolationDataRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InterpolationDataService {
    private final InterpolationDataRepository repository;

    public InterpolationDataService(InterpolationDataRepository repository) {
        this.repository = repository;
    }

    public InterpolationData saveData(InterpolationData interpolationData) {
        InterpolationData data = new InterpolationData(interpolationData.getUsername(), interpolationData.getPointsNumber(),
                interpolationData.getSearchedValue(), interpolationData.getPoints());
        return repository.save(data);
    }

    public List<InterpolationData> getDataByUserId(String username) {
        return repository.findByUsername(username);
    }
}
