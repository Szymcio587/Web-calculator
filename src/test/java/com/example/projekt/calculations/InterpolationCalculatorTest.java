package com.example.projekt.calculations;

import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.Point;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InterpolationCalculatorTest {

    @InjectMocks
    private InterpolationCalculator interpolationCalculator;

    @Mock
    private InterpolationData interpolationData;

    private List<Point> points;

    @Test
    public void TestCalculateWithEmptyPoints() {
        interpolationData = null;
        double result = interpolationCalculator.Calculate(interpolationData);

        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void TestCalculateWithOnePoint() {
        points = new ArrayList<>();
        points.add(new Point(2, 5));
        interpolationData = new InterpolationData(1, 2, points);

        double result = interpolationCalculator.Calculate(interpolationData);

        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void TestCalculateWithMultiplePoints() {
        points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 4));
        points.add(new Point(5, 25));
        interpolationData = new InterpolationData(3, 3, points);

        double result = interpolationCalculator.Calculate(interpolationData);

        assertEquals(9.0, result, 0.0001);
    }

    @Test
    public void TestLagrange() {
        points = new ArrayList<>();
        points.add(new Point(0, 3));
        points.add(new Point(3, -5));
        points.add(new Point(4, 6));
        interpolationData = new InterpolationData(3, 3, points);

        double[] coefficients = interpolationCalculator.GenerateCoefficients(points);

        System.out.print("Resulting polynomial coefficients: ");
        for (int i = 0; i < coefficients.length; i++) {
            System.out.print(String.format("%.1f ", coefficients[i]));
        }

        //assertEquals(9.0, result, 0.0001);
    }
}
