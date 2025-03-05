package com.example.projekt.service;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.Savable;
import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.database.IntegrationRecord;
import com.example.projekt.model.database.InterpolationRecord;
import com.example.projekt.model.database.SystemOfEquationsRecord;
import com.example.projekt.model.results.IntegrationResult;
import com.example.projekt.model.results.InterpolationResult;
import com.example.projekt.model.results.SystemOfEquationsResult;
import com.example.projekt.repositories.IntegrationDataRepository;
import com.example.projekt.repositories.InterpolationDataRepository;
import com.example.projekt.repositories.SystemOfEquationsRepository;
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

    public void saveInterpolation(InterpolationData interpolationData, InterpolationResult interpolationResult) {
        if(interpolationData.getUsername() == null || interpolationData.getUsername().isEmpty())
            return;
        InterpolationRecord record = new InterpolationRecord(interpolationData, interpolationResult.getResult(), interpolationResult.getCoefficients(), interpolationResult.getExplanation());
        interpolationDataRepository.save(record);
    }

    public void saveIntegrationData(IntegrationData integrationData, IntegrationResult integrationResult) {
        if(integrationData.getUsername() == null || integrationData.getUsername().isEmpty())
            return;
        IntegrationRecord record = new IntegrationRecord(integrationData, integrationResult.getResult(), integrationResult.getExplanation());
        integrationDataRepository.save(record);
    }

    public void saveSystemOfEquationsData(SystemOfEquationsData systemOfEquationsData, SystemOfEquationsResult systemOfEquationsResult) {
        if(systemOfEquationsData.getUsername() == null || systemOfEquationsData.getUsername().isEmpty())
            return;
        SystemOfEquationsRecord record = new SystemOfEquationsRecord(systemOfEquationsData, systemOfEquationsResult.getSolutions(), systemOfEquationsResult.getExplanation());
        systemOfEquationsRepository.save(record);
    }

    public List<Savable> getDataByUserId(String username) {
        List<Savable> results = new ArrayList<>();
        results.addAll(interpolationDataRepository.findByUsername(username));
        System.out.println(results);
        results.addAll(integrationDataRepository.findByUsername(username));
        System.out.println(results);
        results.addAll(systemOfEquationsRepository.findByUsername(username));
        System.out.println(results);
        return results;
    }
}
