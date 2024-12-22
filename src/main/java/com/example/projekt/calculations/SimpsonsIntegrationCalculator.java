
package com.example.projekt.calculations;

import com.example.projekt.model.data.IntegrationData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpsonsIntegrationCalculator {

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

        for (int i = 0; i < integrationData.getSections(); i += 2) {
            double x0 = integrationData.getXp() + i * width;
            double x1 = x0 + width;
            double x2 = x1 + width;

            result += ValueInPoint(integrationData.getFactors(), x0, integrationData.getDegree()) +
                    4 * ValueInPoint(integrationData.getFactors(), x1, integrationData.getDegree()) +
                    ValueInPoint(integrationData.getFactors(), x2, integrationData.getDegree());
        }

        result *= width / 3;
        return result;
    }
}
