package com.example.projekt.calculations;

import com.example.projekt.model.InterpolationData;
import com.example.projekt.model.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

/*        for (int q = 0; q < data.getPointsNumber(); q++) {
            System.out.println(weights[q]);
        }*/

        for (int q = 0; q < interpolationData.getPointsNumber(); q++) {
            result += interpolationData.getPoints().get(q).getY() * weights[q];
        }

        return result;
    }

    private static double CalculateCoefficient(List<Point> points, int j, double x) {
        double coefficient = 1.0;
        for (int i = 0; i < points.size(); i++) {
            if (i != j) {
                coefficient *= (x - points.get(i).getX()) / (points.get(j).getX() - points.get(i).getX());
            }
        }
        return coefficient;
    }

    public static double[] InterpolatePolynomial(List<Point> points) {
        int n = points.size();
        double[] coefficients = new double[n];

        for (int j = 0; j < n; j++) {
            double y = points.get(j).getY();
            double coefficient = CalculateCoefficient(points, j, 0);
            coefficients[j] = y * coefficient;
        }

        return coefficients;
    }
}
