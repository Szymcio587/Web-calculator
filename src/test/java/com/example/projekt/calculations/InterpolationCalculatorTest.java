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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InterpolationCalculatorTest {

    @InjectMocks
    private InterpolationCalculator interpolationCalculator;

    @Mock
    private Data data;

    @Test
    public void TestCalculateWithEmptyPoints() {
        data = null;
        double result = interpolationCalculator.Calculate(data);

        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void TestCalculateWithOnePoint() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(2, 5));
        data = new Data(1, 2, points);

        double result = interpolationCalculator.Calculate(data);

        assertEquals(5.0, result, 0.0001);
    }

/*    @Test
    public void TestCalculateWithMultiplePoints() {

        double result = interpolationCalculator.Calculate(data);

        assertEquals(5.0, result, 0.0001);
    }*/
}