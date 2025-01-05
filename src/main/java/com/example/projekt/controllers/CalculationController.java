package com.example.projekt.controllers;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.SystemOfEquationsResult;
import com.example.projekt.service.CalculationService;
import com.example.projekt.service.InterpolationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/calculations")
public class CalculationController {

    @Autowired
    private final CalculationService calculationService;

    @Autowired
    private final InterpolationDataService interpolationDataService;

    public CalculationController(CalculationService calculationService, InterpolationDataService interpolationDataService) {
        this.calculationService = calculationService;
        this.interpolationDataService = interpolationDataService;
    }

    @PostMapping("/")
    public String Test() {
        return ("Works!");
    }

    @PostMapping("/polynomial_interpolation")
    public ResponseEntity<Double> TreatInterpolationData(@RequestBody InterpolationData interpolationData) {
        System.out.println("Received InterpolationData: " + interpolationData);
        interpolationDataService.saveData(interpolationData);
        double result = calculationService.CalculateInterpolation(interpolationData);
        System.out.println("Calculated interpolation: " + result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/trigonometric_interpolation")
    public ResponseEntity<Double> TreatTrigonometricInterpolationData(@RequestBody InterpolationData interpolationData) {
        System.out.println("Received InterpolationData: " + interpolationData);
        double result = calculationService.CalculateTrigonometricInterpolation(interpolationData);
        System.out.println("Calculated interpolation: " + result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/trapezoidal_integration")
    public ResponseEntity<Double> TreatTrapezoidalIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        double result = calculationService.CalculateTrapezoidalIntegration(integrationData);
        System.out.println("Calculated integration: " + result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/midpoint_integration")
    public ResponseEntity<Double> TreatMidpointIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        double result = calculationService.CalculateMidpointIntegration(integrationData);
        System.out.println("Calculated integration: " + result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/simpson_integration")
    public ResponseEntity<Double> TreatSimpsonsIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        double result = calculationService.CalculateSimpsonsIntegration(integrationData);
        System.out.println("Calculated integration: " + result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/solve_system")
    public ResponseEntity<SystemOfEquationsResult> TreatSystemOfEquations(@RequestBody SystemOfEquationsData systemOfEquationsData) {
        System.out.println("Received SystemOfEquationsData: " + systemOfEquationsData);
        SystemOfEquationsResult result = calculationService.CalculateSystemOfEquations(systemOfEquationsData);
        System.out.println("Calculated system of equations: " + result);
        return ResponseEntity.ok(result);
    }
}
