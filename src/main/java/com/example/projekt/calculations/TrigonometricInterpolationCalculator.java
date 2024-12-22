package com.example.projekt.calculations;

import com.example.projekt.model.Point;
import com.example.projekt.model.data.InterpolationData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrigonometricInterpolationCalculator {

    public double Calculate(InterpolationData interpolationData) {
        if (interpolationData == null)
            return 0;

        List<Point> points = interpolationData.getPoints();
        int n = points.size();
        double searchedX = interpolationData.getSearchedValue();
        double[] a = new double[n];
        double[] b = new double[n];

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                double x = points.get(i).getX();
                double y = points.get(i).getY();
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
            result += a[k] * Math.cos(k * searchedX) + b[k] * Math.sin(k * searchedX);
        }

        return result;
    }
}

