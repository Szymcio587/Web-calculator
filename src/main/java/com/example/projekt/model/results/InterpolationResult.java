package com.example.projekt.model.results;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterpolationResult {

    private double result;

    private double[] coefficients;
    private String prompt;
}
