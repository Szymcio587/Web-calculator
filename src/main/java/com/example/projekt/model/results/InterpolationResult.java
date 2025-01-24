package com.example.projekt.model.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterpolationResult {

    private double result;

    private double[] coefficients;
    private String prompt;

    private String explanation;
}
