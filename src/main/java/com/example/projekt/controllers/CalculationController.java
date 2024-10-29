package com.example.projekt.controllers;

import com.example.projekt.model.IntegrationData;
import com.example.projekt.model.InterpolationData;
import com.example.projekt.model.InterpolationResult;
import com.example.projekt.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/calculations")
public class CalculationController {

    @Autowired
    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/")
    public String Test() {
        return ("Works!");
    }

    @PostMapping("/interpolation")
    public ResponseEntity<InterpolationResult> TreatInterpolationData(@RequestBody InterpolationData interpolationData) {
        System.out.println("Received InterpolationData: " + interpolationData);
        double result = calculationService.CalculateInterpolation(interpolationData);
        System.out.println("CalculatedInterpolation: " + result);
        return ResponseEntity.ok(new InterpolationResult(result , "Interpolation"));
    }

    @PostMapping("/integration")
    public ResponseEntity<Double> TreatIntegrationData(@RequestBody IntegrationData integrationData) {
        System.out.println("Received IntegrationData: " + integrationData);
        double result = calculationService.CalculateIntegration(integrationData);
        System.out.println("CalculatedIntegration: " + result);
        return ResponseEntity.ok(result);
    }
}
