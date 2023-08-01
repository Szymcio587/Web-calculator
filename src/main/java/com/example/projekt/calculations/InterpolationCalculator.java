package com.example.projekt.calculations;

import com.example.projekt.model.InterpolationData;
import org.springframework.stereotype.Component;

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
}
