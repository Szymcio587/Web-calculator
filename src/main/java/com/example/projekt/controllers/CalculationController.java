package com.example.projekt.controllers;

import com.example.projekt.calculations.InterpolationCalculator;
import com.example.projekt.model.Data;
import com.example.projekt.model.Point;
import com.example.projekt.model.Result;
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
    public ResponseEntity<String> ReceiveInterpolationData(@RequestBody Data data) {
        System.out.println(data.getSearchedValue());
        calculationService.CacheData(data);
        return ResponseEntity.ok("Data received succesfully");
    }

    @GetMapping("/result")
    public ResponseEntity<Map<String, Double>> SendInterpolationResult() {
        Data cachedData = calculationService.GetCachedData();
        if (cachedData == null) {
            return ResponseEntity.badRequest().body(null);
        }
        System.out.println(cachedData.getSearchedValue());
        double result = calculationService.CalculateInterpolation(cachedData);

        Map<String, Double> resultMap = new HashMap<>();
        resultMap.put("result", 5.0);

        return ResponseEntity.ok(resultMap);
    }
}
