
package com.example.projekt.calculations;

import com.example.projekt.model.data.IntegrationData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MidpointIntegrationCalculator {

    private double ValueInPoint(List<Double> factors, double q, int degree) {
        double result = 0, partial = 1;
        for (int i = degree; i >= 0; i--) {
            partial = factors.get(i);
            for (int j = i; j > 0; j--) {
                partial *= q;
            }
            result += partial;
        }
        return result;
    }

    public double Calculate(IntegrationData integrationData) {
        double width = (integrationData.getXk() - integrationData.getXp()) / integrationData.getSections();
        double result = 0;

        for (double q = integrationData.getXp() + width / 2; q < integrationData.getXk(); q += width) {
            result += ValueInPoint(integrationData.getFactors(), q, integrationData.getDegree());
        }

        result *= width;
        return result;
    }
}
