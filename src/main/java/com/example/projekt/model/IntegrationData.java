package com.example.projekt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class IntegrationData {
    private int degree;
    private List<Double> factors;
    private double sections;
    private double Xp;
    private double Xk;
}
