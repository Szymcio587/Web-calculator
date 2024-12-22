package com.example.projekt.calculations;

import com.example.projekt.model.Point;
import com.example.projekt.model.data.InterpolationData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrigonometricInterpolationCalculatorTest {

    @InjectMocks
    private TrigonometricInterpolationCalculator trigonometricInterpolationCalculator;

    @Mock
    private InterpolationData interpolationData;

    private List<Point> points;

    @Test
    public void TestCalculateWithEmptyPoints() {
        interpolationData = null;
        double result = trigonometricInterpolationCalculator.Calculate(interpolationData);

        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void TestCalculateWithOnePoint() {
        points = new ArrayList<>();
        points.add(new Point(2, 5));
        interpolationData = new InterpolationData(1, 2, points);

        double result = trigonometricInterpolationCalculator.Calculate(interpolationData);

        assertEquals(2.5, result, 0.0001);
    }

    @Test
    public void TestCalculateWithMultiplePoints() {
        points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 4));
        points.add(new Point(5, 25));
        interpolationData = new InterpolationData(3, 3, points);

        double result = trigonometricInterpolationCalculator.Calculate(interpolationData);

        assertEquals(-4.10598, result, 0.0001);
    }
}