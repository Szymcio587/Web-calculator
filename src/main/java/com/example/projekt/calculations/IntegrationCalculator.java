package com.example.projekt.calculations;

import com.example.projekt.model.IntegrationData;
import org.springframework.stereotype.Component;

import com.example.projekt.model.InterpolationData;

import java.util.List;

@Component
public class IntegrationCalculator {

    private double ValueInPoint(List<Double> factors, double q, int degree) {
        double result = 0, partial = 1;
        for (int i = degree; i >= 0; i--) {
            partial = factors.get(i);
            for (int j = i; j > 0; j--) {
                partial *= q;
            }
            result += partial;
            partial = 1;
        }
        return result;
    }

    public double Calculate(IntegrationData integrationData) {
        double width = (integrationData.getXk() - integrationData.getXp()) / integrationData.getSections();
        double result = 0;

        for (double q = integrationData.getXp(); q < integrationData.getXk(); q += width) {
            result += (ValueInPoint(integrationData.getFactors(), q, integrationData.getDegree()) +
                    ValueInPoint(integrationData.getFactors(), q + width, integrationData.getDegree())) / 2;
        }

        result *= width;
        return result;
    }
}
