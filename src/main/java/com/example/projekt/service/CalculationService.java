package com.example.projekt.service;

import com.example.projekt.calculations.*;
import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.SystemOfEquationsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    @Autowired
    private InterpolationCalculator interpolationCalculator;

    @Autowired
    private TrigonometricInterpolationCalculator trigonometricInterpolationCalculator;

    @Autowired
    private TrapezoidalIntegrationCalculator trapezoidalIntegrationCalculator;

    @Autowired
    private MidpointIntegrationCalculator midpointIntegrationCalculator;

    @Autowired
    private SimpsonsIntegrationCalculator simpsonsIntegrationCalculator;
    @Autowired
    private SystemOfEquationsCalculator systemOfEquationsCalculator;

    public double CalculateInterpolation(InterpolationData interpolationData) {
        return interpolationCalculator.Calculate(interpolationData);
    }

    public double CalculateTrigonometricInterpolation(InterpolationData interpolationData) {
        return trigonometricInterpolationCalculator.Calculate(interpolationData);
    }

    public double CalculateTrapezoidalIntegration(IntegrationData integrationData) {
        return trapezoidalIntegrationCalculator.Calculate(integrationData);
    }

    public double CalculateMidpointIntegration(IntegrationData integrationData) {
        return midpointIntegrationCalculator.Calculate(integrationData);
    }

    public double CalculateSimpsonsIntegration(IntegrationData integrationData) {
        return simpsonsIntegrationCalculator.Calculate(integrationData);
    }

    public SystemOfEquationsResult CalculateSystemOfEquations(SystemOfEquationsData data) {
        return systemOfEquationsCalculator.calculate(data);
    }
}
