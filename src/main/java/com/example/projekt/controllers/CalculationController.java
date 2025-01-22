package com.example.projekt.controllers;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.InterpolationResult;
import com.example.projekt.model.results.SystemOfEquationsResult;
import com.example.projekt.service.CalculationService;
import com.example.projekt.service.ChatService;
import com.example.projekt.service.DataService;
import com.example.projekt.service.UtilityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/calculations")
@AllArgsConstructor
public class CalculationController {

    @Autowired
    private final CalculationService calculationService;

    @Autowired
    private final DataService dataService;

    @Autowired
    private final UtilityService utilityService;

    @Autowired
    private final ChatService chatService;

    @PostMapping("/")
    public String Test() {
        return ("Works!");
    }

    @PostMapping("/polynomial_interpolation")
    public ResponseEntity<InterpolationResult> TreatInterpolationData(@RequestBody InterpolationData interpolationData) {
        System.out.println("Received InterpolationData: " + interpolationData);
        InterpolationResult interpolationResult = calculationService.CalculateInterpolation(interpolationData);
        if(!interpolationData.isTest())
            dataService.saveInterpolation(interpolationData, interpolationResult);
        System.out.println("Calculated interpolation: " + interpolationResult);
        return ResponseEntity.ok(interpolationResult);
    }

    @PostMapping("/trigonometric_interpolation")
    public ResponseEntity<Double> TreatTrigonometricInterpolationData(@RequestBody InterpolationData interpolationData) {
        System.out.println("Received InterpolationData: " + interpolationData);
        //dataService.saveInterpolationData(interpolationData);
        double result = calculationService.CalculateTrigonometricInterpolation(interpolationData);
        System.out.println("Calculated interpolation: " + result);
        return ResponseEntity.ok(utilityService.Round(result, 3));
    }

    @PostMapping("/trapezoidal_integration")
    public ResponseEntity<Double> TreatTrapezoidalIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        dataService.saveIntegrationData(integrationData);
        double result = calculationService.CalculateTrapezoidalIntegration(integrationData);
        System.out.println("Calculated integration: " + result);
        return ResponseEntity.ok(utilityService.Round(result, 3));
    }

    @PostMapping("/midpoint_integration")
    public ResponseEntity<Double> TreatMidpointIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        dataService.saveIntegrationData(integrationData);
        double result = calculationService.CalculateMidpointIntegration(integrationData);
        System.out.println("Calculated integration: " + result);
        return ResponseEntity.ok(utilityService.Round(result, 3));
    }

    @PostMapping("/simpson_integration")
    public ResponseEntity<Double> TreatSimpsonsIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        dataService.saveIntegrationData(integrationData);
        double result = calculationService.CalculateSimpsonsIntegration(integrationData);
        System.out.println("Calculated integration: " + result);
        return ResponseEntity.ok(utilityService.Round(result, 3));
    }

    @PostMapping("/system_of_equations")
    public ResponseEntity<SystemOfEquationsResult> TreatSystemOfEquations(@RequestBody SystemOfEquationsData systemOfEquationsData) {
        System.out.println("Received SystemOfEquationsData: " + systemOfEquationsData);
        dataService.saveSystemOfEquationsData(systemOfEquationsData);
        SystemOfEquationsResult result = calculationService.CalculateSystemOfEquations(systemOfEquationsData);
        System.out.println("Calculated system of equations: " + result);
        return ResponseEntity.ok(result);
    }

}
