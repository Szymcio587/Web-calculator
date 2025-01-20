package com.example.projekt.service;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.Savable;
import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.repositories.IntegrationDataRepository;
import com.example.projekt.repositories.InterpolationDataRepository;
import com.example.projekt.repositories.SystemOfEquationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {
    private final InterpolationDataRepository interpolationDataRepository;
    private final IntegrationDataRepository integrationDataRepository;
    private final SystemOfEquationsRepository systemOfEquationsRepository;

    public DataService(InterpolationDataRepository interpolationDataRepository, IntegrationDataRepository integrationDataRepository, SystemOfEquationsRepository systemOfEquationsRepository) {
        this.interpolationDataRepository = interpolationDataRepository;
        this.integrationDataRepository = integrationDataRepository;
        this.systemOfEquationsRepository = systemOfEquationsRepository;
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

    public void saveSystemOfEquationsData(SystemOfEquationsData systemOfEquationsData) {
        if(systemOfEquationsData.getUsername() == null || systemOfEquationsData.getUsername().isEmpty())
            return;
        SystemOfEquationsData data = new SystemOfEquationsData(systemOfEquationsData.getUsername(), systemOfEquationsData.getCoefficients(), systemOfEquationsData.getConstants());
        systemOfEquationsRepository.save(data);
    }

    public List<Savable> getDataByUserId(String username) {
        List<Savable> results = new ArrayList<>();
        results.addAll(interpolationDataRepository.findByUsername(username));
        results.addAll(integrationDataRepository.findByUsername(username));
        results.addAll(systemOfEquationsRepository.findByUsername(username));
        return results;
    }
}
