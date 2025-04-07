package com.example.projekt.service;

import com.example.projekt.calculations.*;
import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.IntegrationResult;
import com.example.projekt.model.results.InterpolationResult;
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
    private CramerSystemOfEquationsCalculator cramerSystemOfEquationsCalculator;

    @Autowired
    private GaussKronrodIntegrationCalculator gaussKronrodIntegrationCalculator;

    @Autowired
    private MultigridSystemOfEquationsCalculator multigridSystemOfEquationsCalculator;

    @Autowired
    private LUSystemOfEquationsCalculator luSystemOfEquationsCalculator;

    @Autowired
    private ChatService chatService;

    public InterpolationResult CalculateInterpolation(InterpolationData interpolationData) {
        InterpolationResult interpolationResult = new InterpolationResult();
        interpolationCalculator.Calculate(interpolationData, interpolationResult);
        interpolationResult.setCoefficients(interpolationCalculator.GenerateCoefficients(interpolationData.getPoints()));
        interpolationResult.setPrompt(interpolationData.isTest() ? chatService.GeneratePolynomialInterpolationResponse(interpolationData, interpolationResult.getResult()) : "");
        return interpolationResult;
    }

    public InterpolationResult CalculateTrigonometricInterpolation(InterpolationData interpolationData) {
        InterpolationResult interpolationResult = new InterpolationResult();
        trigonometricInterpolationCalculator.Calculate(interpolationData, interpolationResult);
        interpolationResult.setPrompt(interpolationData.isTest() ? chatService.GenerateTrigonometricInterpolationResponse(interpolationData, interpolationResult.getResult()) : "");
        return interpolationResult;
    }

    public IntegrationResult CalculateTrapezoidalIntegration(IntegrationData integrationData) {
        IntegrationResult integrationResult = new IntegrationResult();
        trapezoidalIntegrationCalculator.Calculate(integrationData, integrationResult);
        integrationResult.setPrompt(integrationData.isTest() ? chatService.GenerateTrapezoidalIntegrationResponse(integrationData, integrationResult.getResult()) : "");
        return integrationResult;
    }

    public IntegrationResult CalculateMidpointIntegration(IntegrationData integrationData) {
        IntegrationResult integrationResult = new IntegrationResult();
        midpointIntegrationCalculator.Calculate(integrationData, integrationResult);
        integrationResult.setPrompt(integrationData.isTest() ? chatService.GenerateMidpointIntegrationResponse(integrationData, integrationResult.getResult()) : "");
        return integrationResult;
    }

    public IntegrationResult CalculateSimpsonsIntegration(IntegrationData integrationData) {
        IntegrationResult integrationResult = new IntegrationResult();
        simpsonsIntegrationCalculator.Calculate(integrationData, integrationResult);
        integrationResult.setPrompt(integrationData.isTest() ? chatService.GenerateSimpsonsIntegrationResponse(integrationData, integrationResult.getResult()) : "");
        return integrationResult;
    }

    public IntegrationResult CalculateGaussKronrodIntegration(IntegrationData integrationData) {
        IntegrationResult integrationResult = new IntegrationResult();
        gaussKronrodIntegrationCalculator.Calculate(integrationData, integrationResult);
        integrationResult.setPrompt(integrationData.isTest() ? chatService.GenerateGaussKronrodIntegrationResponse(integrationData, integrationResult.getResult()) : "");
        return integrationResult;
    }

    public SystemOfEquationsResult CalculateCramerSystemOfEquations(SystemOfEquationsData systemOfEquationsData) {
        SystemOfEquationsResult systemOfEquationsResult = new SystemOfEquationsResult();
        cramerSystemOfEquationsCalculator.Calculate(systemOfEquationsData, systemOfEquationsResult);
        systemOfEquationsResult.setPrompt(systemOfEquationsData.isTest() ? chatService.GenerateCramerSystemOfEquationsResponse(systemOfEquationsData, systemOfEquationsResult.getSolutions()) : "");
        return systemOfEquationsResult;
    }

    public SystemOfEquationsResult CalculateMultigridSystemOfEquations(SystemOfEquationsData systemOfEquationsData) {
        SystemOfEquationsResult systemOfEquationsResult = new SystemOfEquationsResult();
        multigridSystemOfEquationsCalculator.Calculate(systemOfEquationsData, systemOfEquationsResult);
        systemOfEquationsResult.setPrompt(systemOfEquationsData.isTest() ? chatService.GenerateMultigridSystemOfEquationsResponse(systemOfEquationsData, systemOfEquationsResult.getSolutions()) : "");
        return systemOfEquationsResult;
    }

    public SystemOfEquationsResult CalculateLUSystemOfEquations(SystemOfEquationsData systemOfEquationsData) {
        SystemOfEquationsResult systemOfEquationsResult = new SystemOfEquationsResult();
        luSystemOfEquationsCalculator.Calculate(systemOfEquationsData, systemOfEquationsResult);
        systemOfEquationsResult.setPrompt(systemOfEquationsData.isTest() ? chatService.GenerateLUSystemOfEquationsResponse(systemOfEquationsData, systemOfEquationsResult.getSolutions()) : "");
        return systemOfEquationsResult;
    }
}
