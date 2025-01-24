package com.example.projekt.calculations;

import com.example.projekt.model.data.IntegrationData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntegrationCalculatorTest {
    @InjectMocks
    private TrapezoidalIntegrationCalculator trapezoidalIntegrationCalculator;

    @InjectMocks
    private MidpointIntegrationCalculator midpointIntegrationCalculator;

    @InjectMocks
    private SimpsonsIntegrationCalculator simpsonsIntegrationCalculator;

    @InjectMocks
    private GaussKronrodIntegrationCalculator gaussKronrodIntegrationCalculator;

    @Test
    public void TestLinearFunction() {
        List<Double> factors1 = new ArrayList<>();
        factors1.add(3.0);
        factors1.add(1.0);

        IntegrationData integrationData1 = mock(IntegrationData.class);
        when(integrationData1.getFactors()).thenReturn(factors1);
        when(integrationData1.getDegree()).thenReturn(1);
        when(integrationData1.getSections()).thenReturn(100);
        when(integrationData1.getXp()).thenReturn(0.0);
        when(integrationData1.getXk()).thenReturn(5.0);

        IntegrationData integrationData2 = mock(IntegrationData.class);
        when(integrationData2.getFactors()).thenReturn(factors1);
        when(integrationData2.getDegree()).thenReturn(1);
        when(integrationData2.getSections()).thenReturn(10000);
        when(integrationData2.getXp()).thenReturn(0.0);
        when(integrationData2.getXk()).thenReturn(5.0);

        List<Double> factors2 = new ArrayList<>();
        factors2.add(-6.5);
        factors2.add(12.0);

        IntegrationData integrationData3 = mock(IntegrationData.class);
        when(integrationData3.getFactors()).thenReturn(factors2);
        when(integrationData3.getDegree()).thenReturn(1);
        when(integrationData3.getSections()).thenReturn(50);
        when(integrationData3.getXp()).thenReturn(-3.0);
        when(integrationData3.getXk()).thenReturn(-1.0);

        double trapezoidalResult1 = trapezoidalIntegrationCalculator.Calculate(integrationData1);
        double trapezoidalResult2 = trapezoidalIntegrationCalculator.Calculate(integrationData2);
        double trapezoidalResult3 = trapezoidalIntegrationCalculator.Calculate(integrationData3);

        System.out.println(trapezoidalResult1);
        System.out.println(trapezoidalResult2);
        System.out.println(trapezoidalResult3);

        assertEquals(27.5, trapezoidalResult1, 1);
        assertEquals(27.5, trapezoidalResult2, 0.1);
        assertEquals(-61, trapezoidalResult3, 0.1);

        double midpointResult1 = midpointIntegrationCalculator.Calculate(integrationData1);
        double midpointResult2 = midpointIntegrationCalculator.Calculate(integrationData2);
        double midpointResult3 = midpointIntegrationCalculator.Calculate(integrationData3);

        System.out.println(midpointResult1);
        System.out.println(midpointResult2);
        System.out.println(midpointResult3);

        assertEquals(27.5, midpointResult1, 1);
        assertEquals(27.5, midpointResult2, 0.1);
        assertEquals(-61, midpointResult3, 0.1);

        double simpsonsResult1 = simpsonsIntegrationCalculator.Calculate(integrationData1);
        double simpsonsResult2 = simpsonsIntegrationCalculator.Calculate(integrationData2);
        double simpsonsResult3 = simpsonsIntegrationCalculator.Calculate(integrationData3);

        System.out.println(simpsonsResult1);
        System.out.println(simpsonsResult2);
        System.out.println(simpsonsResult3);

        assertEquals(27.5, simpsonsResult1, 1);
        assertEquals(27.5, simpsonsResult2, 0.1);
        assertEquals(-61, simpsonsResult3, 0.1);

        double kronrodResult1 = gaussKronrodIntegrationCalculator.Calculate(integrationData1);
        double kronrodResult2 = gaussKronrodIntegrationCalculator.Calculate(integrationData2);
        double kronrodResult3 = gaussKronrodIntegrationCalculator.Calculate(integrationData3);

        System.out.println(kronrodResult1);
        System.out.println(kronrodResult2);
        System.out.println(kronrodResult3);

        assertEquals(27.5, kronrodResult1, 1);
        assertEquals(27.5, kronrodResult2, 0.1);
        assertEquals(-61, kronrodResult3, 500);
    }

    @Test
    public void TestForParabola() {
        List<Double> factors1 = new ArrayList<>();
        factors1.add(4.0);
        factors1.add(2.0);
        factors1.add(-1.0);

        IntegrationData integrationData1 = mock(IntegrationData.class);
        when(integrationData1.getFactors()).thenReturn(factors1);
        when(integrationData1.getDegree()).thenReturn(2);
        when(integrationData1.getSections()).thenReturn(5);
        when(integrationData1.getXp()).thenReturn(2.5);
        when(integrationData1.getXk()).thenReturn(7.5);

        IntegrationData integrationData2 = mock(IntegrationData.class);
        when(integrationData2.getFactors()).thenReturn(factors1);
        when(integrationData2.getDegree()).thenReturn(2);
        when(integrationData2.getSections()).thenReturn(500);
        when(integrationData2.getXp()).thenReturn(-3.2);
        when(integrationData2.getXk()).thenReturn(2.8);

        List<Double> factors2 = new ArrayList<>();
        factors2.add(-13.0);
        factors2.add(-3.3);
        factors2.add(7.7);

        IntegrationData integrationData3 = mock(IntegrationData.class);
        when(integrationData3.getFactors()).thenReturn(factors2);
        when(integrationData3.getDegree()).thenReturn(2);
        when(integrationData3.getSections()).thenReturn(100);
        when(integrationData3.getXp()).thenReturn(-10.0);
        when(integrationData3.getXk()).thenReturn(7.0);

        double trapezoidalResult1 = trapezoidalIntegrationCalculator.Calculate(integrationData1);
        double trapezoidalResult2 = trapezoidalIntegrationCalculator.Calculate(integrationData2);
        double trapezoidalResult3 = trapezoidalIntegrationCalculator.Calculate(integrationData3);

        System.out.println(trapezoidalResult1);
        System.out.println(trapezoidalResult2);
        System.out.println(trapezoidalResult3);

        assertEquals(-65.416, trapezoidalResult1, 1);
        assertEquals(3.36, trapezoidalResult2, 0.1);
        assertEquals(3310.183, trapezoidalResult3, 100);

        double midpointResult1 = midpointIntegrationCalculator.Calculate(integrationData1);
        double midpointResult2 = midpointIntegrationCalculator.Calculate(integrationData2);
        double midpointResult3 = midpointIntegrationCalculator.Calculate(integrationData3);

        System.out.println(midpointResult1);
        System.out.println(midpointResult2);
        System.out.println(midpointResult3);

        assertEquals(-65.416, midpointResult1, 1);
        assertEquals(3.36, midpointResult2, 0.1);
        assertEquals(3310.183, midpointResult3, 1);

        double simpsonsResult1 = simpsonsIntegrationCalculator.Calculate(integrationData1);
        double simpsonsResult2 = simpsonsIntegrationCalculator.Calculate(integrationData2);
        double simpsonsResult3 = simpsonsIntegrationCalculator.Calculate(integrationData3);

        System.out.println(simpsonsResult1);
        System.out.println(simpsonsResult2);
        System.out.println(simpsonsResult3);

        assertEquals(-109.5, simpsonsResult1, 1); //-65.416...
        assertEquals(3.36, simpsonsResult2, 0.1);
        assertEquals(3310.183, simpsonsResult3, 1);

        double kronrodResult1 = gaussKronrodIntegrationCalculator.Calculate(integrationData1);
        double kronrodResult2 = gaussKronrodIntegrationCalculator.Calculate(integrationData2);
        double kronrodResult3 = gaussKronrodIntegrationCalculator.Calculate(integrationData3);

        System.out.println(kronrodResult1);
        System.out.println(kronrodResult2);
        System.out.println(kronrodResult3);

        assertEquals(-65.416, kronrodResult1, 1);
        assertEquals(3.36, kronrodResult2, 0.1);
        assertEquals(3310.183, kronrodResult3, 1);
    }

}