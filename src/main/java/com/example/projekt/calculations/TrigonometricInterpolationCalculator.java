package com.example.projekt.calculations;

import com.example.projekt.model.Point;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.results.InterpolationResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TrigonometricInterpolationCalculator {

    public double Calculate(InterpolationData interpolationData, InterpolationResult interpolationResult) {
        if (interpolationData == null)
            return 0;

        int n = interpolationData.getPointsNumber();;

        if(n == 1)
            return interpolationData.getPoints().get(0).getY();

        double searchedX = interpolationData.getSearchedValue();

        double minX = interpolationData.getPoints().stream().mapToDouble(Point::getX).min().orElse(0);
        double maxX = interpolationData.getPoints().stream().mapToDouble(Point::getX).max().orElse(0);
        double rangeX = maxX - minX;

        List<Point> transformedPoints = new ArrayList<>();
        for (Point point : interpolationData.getPoints()) {
            double normalizedX = (point.getX() - minX) / rangeX * (2 * Math.PI);
            transformedPoints.add(new Point(normalizedX, point.getY()));
        }

        double normalizedSearchedX = (searchedX - minX) / rangeX * (2 * Math.PI);

        double[] a = new double[n];
        double[] b = new double[n];

        for (int q = 0; q < n; q++) {
            for (Point point : transformedPoints) {
                double x = point.getX();
                double y = point.getY();
                a[q] += y * Math.cos(q * x);
                b[q] += y * Math.sin(q * x);
            }
            a[q] /= n;
            if (q > 0) {
                b[q] /= n;
            }
        }

        double result = a[0] / 2;
        for (int q = 1; q < n; q++) {
            result += a[q] * Math.cos(q * normalizedSearchedX) + b[q] * Math.sin(q * normalizedSearchedX);
        }

        return result;
    }
}

