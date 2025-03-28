package com.example.projekt.controllers;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.IntegrationResult;
import com.example.projekt.model.results.InterpolationResult;
import com.example.projekt.model.results.SystemOfEquationsResult;
import com.example.projekt.service.CalculationService;
import com.example.projekt.service.DataService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calculations")
@AllArgsConstructor
public class CalculationController {

    @Autowired
    private final CalculationService calculationService;

    @Autowired
    private final DataService dataService;

    @PostMapping("/")
    public String Test() {
        return ("Works!");
    }

    @PostMapping("/polynomial_interpolation")
    public ResponseEntity<InterpolationResult> TreatInterpolationData(@RequestBody InterpolationData interpolationData) {
        System.out.println("Received InterpolationData: " + interpolationData);
        InterpolationResult interpolationResult = calculationService.CalculateInterpolation(interpolationData);
        if(!interpolationData.isTest())
            //Do zapisu danych wymagane jest połączenie z bazą danych MongoDB
            dataService.saveInterpolation(interpolationData, interpolationResult);
        System.out.println("Calculated interpolation: " + interpolationResult.getResult());
        return ResponseEntity.ok(interpolationResult);
    }

    @PostMapping("/trigonometric_interpolation")
    public ResponseEntity<InterpolationResult> TreatTrigonometricInterpolationData(@RequestBody InterpolationData interpolationData) {
        System.out.println("Received InterpolationData: " + interpolationData);
        InterpolationResult interpolationResult = calculationService.CalculateTrigonometricInterpolation(interpolationData);
        if(!interpolationData.isTest())
            dataService.saveInterpolation(interpolationData, interpolationResult);
        System.out.println("Calculated interpolation: " + interpolationResult.getResult());
        return ResponseEntity.ok(interpolationResult);
    }

    @PostMapping("/trapezoidal_integration")
    public ResponseEntity<IntegrationResult> TreatTrapezoidalIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        IntegrationResult integrationResult = calculationService.CalculateTrapezoidalIntegration(integrationData);
        if(!integrationData.isTest())
            dataService.saveIntegrationData(integrationData, integrationResult);
        System.out.println("Calculated integration: " + integrationResult.getResult());
        return ResponseEntity.ok(integrationResult);
    }

    @PostMapping("/midpoint_integration")
    public ResponseEntity<IntegrationResult> TreatMidpointIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        IntegrationResult integrationResult = calculationService.CalculateMidpointIntegration(integrationData);
        if(!integrationData.isTest())
            dataService.saveIntegrationData(integrationData, integrationResult);
        System.out.println("Calculated integration: " + integrationResult.getResult());
        return ResponseEntity.ok(integrationResult);
    }

    @PostMapping("/simpson_integration")
    public ResponseEntity<IntegrationResult> TreatSimpsonsIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        IntegrationResult integrationResult = calculationService.CalculateSimpsonsIntegration(integrationData);
        if(!integrationData.isTest())
            dataService.saveIntegrationData(integrationData, integrationResult);
        System.out.println("Calculated integration: " + integrationResult.getResult());
        return ResponseEntity.ok(integrationResult);
    }

    @PostMapping("/gauss_kronrod_integration")
    public ResponseEntity<IntegrationResult> TreatGaussKronrodIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        IntegrationResult integrationResult = calculationService.CalculateGaussKronrodIntegration(integrationData);
        if(!integrationData.isTest())
            dataService.saveIntegrationData(integrationData, integrationResult);
        System.out.println("Calculated integration: " + integrationResult.getResult());
        return ResponseEntity.ok(integrationResult);
    }

    @PostMapping("/cramer_system_of_equations")
    public ResponseEntity<SystemOfEquationsResult> TreatCramerSystemOfEquations(@RequestBody SystemOfEquationsData systemOfEquationsData) {
        System.out.println("Received SystemOfEquationsData: " + systemOfEquationsData);
        SystemOfEquationsResult systemOfEquationsResult = calculationService.CalculateCramerSystemOfEquations(systemOfEquationsData);
        if(!systemOfEquationsData.isTest())
            dataService.saveSystemOfEquationsData(systemOfEquationsData, systemOfEquationsResult);
        System.out.println("Calculated system of equations: " + systemOfEquationsResult.getSolutions());
        return ResponseEntity.ok(systemOfEquationsResult);
    }

    @PostMapping("/multigrid_system_of_equations")
    public ResponseEntity<SystemOfEquationsResult> TreatMultigridSystemOfEquations(@RequestBody SystemOfEquationsData systemOfEquationsData) {
        System.out.println("Received SystemOfEquationsData: " + systemOfEquationsData);
        SystemOfEquationsResult systemOfEquationsResult = calculationService.CalculateMultigridSystemOfEquations(systemOfEquationsData);
        if(!systemOfEquationsData.isTest())
            dataService.saveSystemOfEquationsData(systemOfEquationsData, systemOfEquationsResult);
        System.out.println("Calculated system of equations: " + systemOfEquationsResult.getSolutions());
        return ResponseEntity.ok(systemOfEquationsResult);
    }

}
