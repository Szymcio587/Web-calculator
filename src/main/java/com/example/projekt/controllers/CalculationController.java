package com.example.projekt.controllers;

import com.example.projekt.calculations.InterpolationCalculator;
import com.example.projekt.model.Data;
import com.example.projekt.model.Point;
import com.example.projekt.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
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

    @PostMapping("/calculations/interpolation")
    public ResponseEntity<Double> ReceiveInterpolationData(@RequestBody Data data) {
        double result = calculationService.CalculateInterpolation(data);
        System.out.println("Calculated: " + result);
        return ResponseEntity.ok(result);
    }
}
