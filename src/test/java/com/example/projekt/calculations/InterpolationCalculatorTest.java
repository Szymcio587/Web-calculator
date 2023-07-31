package com.example.projekt.calculations;

import com.example.projekt.model.Data;
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
    private Data data;

    private List<Point> points;

    @Test
    public void TestCalculateWithEmptyPoints() {
        data = null;
        double result = interpolationCalculator.Calculate(data);

        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void TestCalculateWithOnePoint() {
        points = new ArrayList<>();
        points.add(new Point(2, 5));
        data = new Data(1, 2, points);

        double result = interpolationCalculator.Calculate(data);

        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void TestCalculateWithMultiplePoints() {
        points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 4));
        points.add(new Point(5, 25));
        data = new Data(3, 3, points);

        double result = interpolationCalculator.Calculate(data);

        assertEquals(9.0, result, 0.0001);
    }
}
