package com.example.projekt.controllers;

import com.example.projekt.calculations.InterpolationCalculator;
import com.example.projekt.model.Data;
import com.example.projekt.model.Point;
import com.example.projekt.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
public class CalculationController {

    @Autowired
    InterpolationCalculator calculator;

    @PostMapping("/")
    public String Test() {
        return ("Works!");
    }

    @PostMapping("/calculations/interpolation")
    public ResponseEntity<Double> Interpolate(@RequestBody Data data) {
        double result = calculator.Calculate(data);
        return ResponseEntity.ok(result);
    }
}
