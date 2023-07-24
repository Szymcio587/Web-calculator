package com.example.projekt.calculations;

import com.example.projekt.model.Data;
import org.springframework.stereotype.Component;

@Component
public class InterpolationCalculator {

    public double Calculate(Data data) {
        int[] weights = new int[data.getPointsNumber()];
        double result = 0;

        for (int q = 0; q < data.getPointsNumber(); q++) {
            weights[q] = 1;
        }

        for (int q = 0; q < data.getPointsNumber(); q++)
            for (int w = 0; w < data.getPointsNumber(); w++)
                if (q != w)
                    weights[q] *= (data.getSearchedValue() - data.getPoints().get(w).getX()) /
                            (data.getPoints().get(q).getX() - data.getPoints().get(w).getX());

        for (int q = 0; q < data.getPointsNumber(); q++) {
            result += data.getPoints().get(q).getY() * weights[q];
        }

        return result;
    }
}
