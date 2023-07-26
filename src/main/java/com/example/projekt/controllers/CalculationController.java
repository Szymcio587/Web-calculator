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
        System.out.println("Receive: " + data.getSearchedValue());
        //calculationService.CacheData(data);
        double result = calculationService.CalculateInterpolation(data);
        return ResponseEntity.ok(result);
    }

/*    @GetMapping("/result")
    public ResponseEntity<Double> SendInterpolationResult() {
        Data cachedData = calculationService.GetCachedData();
        if (cachedData == null) {
            return ResponseEntity.badRequest().body(null);
        }
        System.out.println("Send: " + cachedData.getSearchedValue());
        double result = calculationService.CalculateInterpolation(cachedData);
        return ResponseEntity.ok(result);
    }*/
}
