package com.example.projekt.calculations;

import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.Point;
import com.example.projekt.service.UtilityService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InterpolationCalculator {

    public double Calculate(InterpolationData interpolationData) {
        if(interpolationData == null)
            return 0;
        double[] weights = new double[interpolationData.getPointsNumber()];
        double result = 0;

        for (int q = 0; q < interpolationData.getPointsNumber(); q++) {
            weights[q] = 1;
        }

        for (int q = 0; q < interpolationData.getPointsNumber(); q++)
            for (int w = 0; w < interpolationData.getPointsNumber(); w++)
                if (q != w)
                    weights[q] *= (interpolationData.getSearchedValue() - interpolationData.getPoints().get(w).getX()) /
                            (interpolationData.getPoints().get(q).getX() - interpolationData.getPoints().get(w).getX());

        for (int q = 0; q < interpolationData.getPointsNumber(); q++) {
            result += interpolationData.getPoints().get(q).getY() * weights[q];
        }

        return result;
    }

    public double[] GenerateCoefficients(List<Point> points) {
        int n = points.size();
        double[] coefficients = new double[n];

        for (int i = 0; i < n; i++) {
            double[] basis = calculateBase(points, i);

            for (int j = 0; j < basis.length; j++) {
                coefficients[j] += basis[j] * points.get(i).getY();
            }
        }

        for (int i = 0; i < n; i++) {
            coefficients[i] = UtilityService.Round(coefficients[i], 5);
        }

        return coefficients;
    }

    private double[] calculateBase(List<Point> points, int i) {
        int n = points.size();
        double[] base = new double[n];
        base[0] = 1.0;

        for (int j = 0; j < n; j++) {
            if (j != i) {
                double denominator = points.get(i).getX() - points.get(j).getX();
                for (int k = n - 1; k > 0; k--) {
                    base[k] = base[k - 1] / denominator - (base[k] * points.get(j).getX() / denominator);
                }
                base[0] = -base[0] * points.get(j).getX() / denominator;
            }
        }

        return base;
    }
}
