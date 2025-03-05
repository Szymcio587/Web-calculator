package com.example.projekt.calculations;

import com.example.projekt.model.Point;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.results.InterpolationResult;
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

    @Mock
    private InterpolationResult interpolationResult = new InterpolationResult();

    private List<Point> points;

    @Test
    public void TestCalculateWithEmptyPoints() {
        interpolationData = null;
        trigonometricInterpolationCalculator.Calculate(interpolationData, interpolationResult);

        assertEquals(0.0, interpolationResult.getResult(), 0.0001);
    }

    @Test
    public void TestCalculateWithOnePoint() {
        points = new ArrayList<>();
        points.add(new Point(2, 5));
        interpolationData = new InterpolationData(1, 2, points);

        trigonometricInterpolationCalculator.Calculate(interpolationData, interpolationResult);

        assertEquals(5, interpolationResult.getResult(), 0.0001);
    }

    @Test
    public void TestCalculateWithMultiplePoints() {
        points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 4));
        points.add(new Point(3, 1));
        points.add(new Point(4, -2));
        points.add(new Point(5, 1));
        interpolationData = new InterpolationData(3, 5, points);

        trigonometricInterpolationCalculator.Calculate(interpolationData, interpolationResult);

        assertEquals(3.6666666, interpolationResult.getResult(), 0.0001);
    }
}