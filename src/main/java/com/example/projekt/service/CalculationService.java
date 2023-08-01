package com.example.projekt.service;

import com.example.projekt.calculations.IntegrationCalculator;
import com.example.projekt.calculations.InterpolationCalculator;
import com.example.projekt.model.IntegrationData;
import com.example.projekt.model.InterpolationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    @Autowired
    private InterpolationCalculator interpolationCalculator;

    @Autowired
    private IntegrationCalculator integrationCalculator;

    public double CalculateInterpolation(InterpolationData interpolationData) {
        return interpolationCalculator.Calculate(interpolationData);
    }

    public double CalculateIntegration(IntegrationData integrationData) {
        return integrationCalculator.Calculate(integrationData);
    }
}
