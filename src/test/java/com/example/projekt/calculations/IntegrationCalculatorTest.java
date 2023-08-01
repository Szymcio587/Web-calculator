package com.example.projekt.calculations;

import com.example.projekt.model.IntegrationData;
import com.example.projekt.model.InterpolationData;
import com.example.projekt.model.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntegrationCalculatorTest {
    @InjectMocks
    private IntegrationCalculator integrationCalculator;

    @Test
    public void TestCalculateForNullData() {
        IntegrationData integrationData = null;
        double result = integrationCalculator.Calculate(integrationData);

        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void TestCalculateForLinearFunction() {
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

        double result1 = integrationCalculator.Calculate(integrationData1);
        double result2 = integrationCalculator.Calculate(integrationData2);
        double result3 = integrationCalculator.Calculate(integrationData3);

        assertEquals(27.5, result1, 1);
        assertEquals(27.5, result2, 0.1);
        assertEquals(-61, result3, 0.1);
    }

    @Test
    public void TestCalculateForParabola() {
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

        double result1 = integrationCalculator.Calculate(integrationData1);
        double result2 = integrationCalculator.Calculate(integrationData2);
        double result3 = integrationCalculator.Calculate(integrationData3);

        assertEquals(-66.25, result1, 1); //-65.41666...
        assertEquals(3.35, result2, 0.1); //3.36
        assertEquals(3141.88, result3, 500); //3141.88333...
    }


}