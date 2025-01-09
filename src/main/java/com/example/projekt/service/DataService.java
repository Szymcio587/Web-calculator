package com.example.projekt.service;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.Savable;
import com.example.projekt.repositories.IntegrationDataRepository;
import com.example.projekt.repositories.InterpolationDataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {
    private final InterpolationDataRepository interpolationDataRepository;

    private final IntegrationDataRepository integrationDataRepository;

    public DataService(InterpolationDataRepository interpolationDataRepository, IntegrationDataRepository integrationDataRepository) {
        this.interpolationDataRepository = interpolationDataRepository;
        this.integrationDataRepository = integrationDataRepository;
    }

    public void saveInterpolationData(InterpolationData interpolationData) {
        if(interpolationData.getUsername() == null || interpolationData.getUsername().isEmpty())
            return;
        InterpolationData data = new InterpolationData(interpolationData.getUsername(), interpolationData.getPointsNumber(),
                interpolationData.getSearchedValue(), interpolationData.getPoints());
        interpolationDataRepository.save(data);
    }

    public void saveIntegrationData(IntegrationData integrationData) {
        if(integrationData.getUsername() == null || integrationData.getUsername().isEmpty())
            return;
        IntegrationData data = new IntegrationData(integrationData.getUsername(), integrationData.getDegree(), integrationData.getFactors(), integrationData.getSections(),
                integrationData.getXp(), integrationData.getXk());
        integrationDataRepository.save(data);
    }

    public List<Savable> getDataByUserId(String username) {
        List<Savable> results = new ArrayList<>();
        results.addAll(interpolationDataRepository.findByUsername(username));
        results.addAll(integrationDataRepository.findByUsername(username));
        return results;
    }
}
