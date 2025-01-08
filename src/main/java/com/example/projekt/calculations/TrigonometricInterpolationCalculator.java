package com.example.projekt.calculations;

import com.example.projekt.model.Point;
import com.example.projekt.model.data.InterpolationData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TrigonometricInterpolationCalculator {

    public double Calculate(InterpolationData interpolationData) {
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

        for (int k = 0; k < n; k++) {
            for (Point point : transformedPoints) {
                double x = point.getX();
                double y = point.getY();
                a[k] += y * Math.cos(k * x);
                b[k] += y * Math.sin(k * x);
            }
            a[k] /= n;
            if (k > 0) {
                b[k] /= n;
            }
        }

        double result = a[0] / 2;
        for (int k = 1; k < n; k++) {
            result += a[k] * Math.cos(k * normalizedSearchedX) + b[k] * Math.sin(k * normalizedSearchedX);
        }

        return result;
    }
}

