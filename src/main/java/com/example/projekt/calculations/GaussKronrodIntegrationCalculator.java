package com.example.projekt.calculations;

import com.example.projekt.model.data.IntegrationData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GaussKronrodIntegrationCalculator {

    private final double[] weights = {
            0.022935322010529224963732008058970,
            0.063092092629978553290700663189204,
            0.104790010322250183839876322541518,
            0.140653259715525918745189590510238,
            0.169004726639267902826583426598550,
            0.190350578064785409913256402421014,
            0.204432940075298892414161999234649,
            0.209482141084727828012999174891714,
            0.204432940075298892414161999234649,
            0.190350578064785409913256402421014,
            0.169004726639267902826583426598550,
            0.140653259715525918745189590510238,
            0.104790010322250183839876322541518,
            0.063092092629978553290700663189204,
            0.022935322010529224963732008058970
    };
    private final double[] points = {
            -0.991455371120812639206854697526329,
            -0.949107912342758524526189684047851,
            -0.864864423359769072789712788640926,
            -0.741531185599394439863864773280788,
            -0.586087235467691130294144838258730,
            -0.405845151377397166906606412076961,
            -0.207784955007898467600689403773245,
            0.000000000000000000000000000000000,
            0.207784955007898467600689403773245,
            0.405845151377397166906606412076961,
            0.586087235467691130294144838258730,
            0.741531185599394439863864773280788,
            0.864864423359769072789712788640926,
            0.949107912342758524526189684047851,
            0.991455371120812639206854697526329
    };

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
        double a = integrationData.getXp();
        double b = integrationData.getXk();

        double midpoint = (a + b) / 2;
        double halfWidth = (b - a) / 2;

        double result = 0;
        for (int q = 0; q < points.length; q++) {
            double x = midpoint + halfWidth * points[q];
            result += weights[q] * ValueInPoint(integrationData.getFactors(), x, integrationData.getDegree());
        }

        result *= halfWidth;
        return result;
    }
}