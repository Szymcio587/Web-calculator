package com.example.projekt.model.data;

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
    private int sections;
    private double Xp;
    private double Xk;

    @Override
    public String toString() {
        return "IntegrationData{" +
                "degree=" + degree +
                ", factors=" + factors +
                ", sections=" + sections +
                ", Xp=" + Xp +
                ", Xk=" + Xk +
                '}';
    }
}
