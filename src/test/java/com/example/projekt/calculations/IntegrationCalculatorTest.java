package com.example.projekt.calculations;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.results.IntegrationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mariuszgromada.math.mxparser.License;
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

        License.iConfirmNonCommercialUse("szymon.talar2882@gmail.com");

        IntegrationData integrationData4 = mock(IntegrationData.class);
        when(integrationData4.getCustomFunction()).thenReturn("5 * sin(x)");
        when(integrationData4.getSections()).thenReturn(10);
        when(integrationData4.getXp()).thenReturn(0.0);
        when(integrationData4.getXk()).thenReturn(Math.PI);

        IntegrationResult integrationResult1 = new IntegrationResult();
        IntegrationResult integrationResult2 = new IntegrationResult();
        IntegrationResult integrationResult3 = new IntegrationResult();
        IntegrationResult integrationResult4 = new IntegrationResult();

        trapezoidalIntegrationCalculator.Calculate(integrationData1, integrationResult1);
        trapezoidalIntegrationCalculator.Calculate(integrationData2, integrationResult2);
        trapezoidalIntegrationCalculator.Calculate(integrationData3, integrationResult3);
        trapezoidalIntegrationCalculator.Calculate(integrationData4, integrationResult4);

        System.out.println(integrationResult1.getResult());
        System.out.println(integrationResult2.getResult());
        System.out.println(integrationResult3.getResult());
        System.out.println(integrationResult4.getResult());

        assertEquals(27.5, integrationResult1.getResult(), 1);
        assertEquals(27.5, integrationResult2.getResult(), 0.1);
        assertEquals(-61, integrationResult3.getResult(), 0.1);
        assertEquals(10, integrationResult4.getResult(), 1);

        midpointIntegrationCalculator.Calculate(integrationData1, integrationResult1);
        midpointIntegrationCalculator.Calculate(integrationData2, integrationResult2);
        midpointIntegrationCalculator.Calculate(integrationData3, integrationResult3);
        midpointIntegrationCalculator.Calculate(integrationData4, integrationResult4);

        System.out.println(integrationResult1.getResult());
        System.out.println(integrationResult2.getResult());
        System.out.println(integrationResult3.getResult());
        System.out.println(integrationResult4.getResult());

        assertEquals(27.5, integrationResult1.getResult(), 1);
        assertEquals(27.5, integrationResult2.getResult(), 0.1);
        assertEquals(-61, integrationResult3.getResult(), 0.1);
        assertEquals(10, integrationResult4.getResult(), 1);

        simpsonsIntegrationCalculator.Calculate(integrationData1, integrationResult1);
        simpsonsIntegrationCalculator.Calculate(integrationData2, integrationResult2);
        simpsonsIntegrationCalculator.Calculate(integrationData3, integrationResult3);
        simpsonsIntegrationCalculator.Calculate(integrationData4, integrationResult4);

        System.out.println(integrationResult1.getResult());
        System.out.println(integrationResult2.getResult());
        System.out.println(integrationResult3.getResult());
        System.out.println(integrationResult4.getResult());

        assertEquals(27.5, integrationResult1.getResult(), 1);
        assertEquals(27.5, integrationResult2.getResult(), 0.1);
        assertEquals(-61, integrationResult3.getResult(), 0.1);
        assertEquals(10, integrationResult4.getResult(), 1);

        gaussKronrodIntegrationCalculator.Calculate(integrationData1, integrationResult1);
        gaussKronrodIntegrationCalculator.Calculate(integrationData2, integrationResult2);
        gaussKronrodIntegrationCalculator.Calculate(integrationData3, integrationResult3);
        gaussKronrodIntegrationCalculator.Calculate(integrationData4, integrationResult4);

        System.out.println(integrationResult1.getResult());
        System.out.println(integrationResult2.getResult());
        System.out.println(integrationResult3.getResult());
        System.out.println(integrationResult4.getResult());

        assertEquals(27.5, integrationResult1.getResult(), 1);
        assertEquals(27.5, integrationResult2.getResult(), 0.1);
        assertEquals(-61, integrationResult3.getResult(), 0.1);
        assertEquals(10, integrationResult4.getResult(), 1);
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

        IntegrationResult integrationResult1 = new IntegrationResult();
        IntegrationResult integrationResult2 = new IntegrationResult();
        IntegrationResult integrationResult3 = new IntegrationResult();

        trapezoidalIntegrationCalculator.Calculate(integrationData1, integrationResult1);
        trapezoidalIntegrationCalculator.Calculate(integrationData2, integrationResult2);
        trapezoidalIntegrationCalculator.Calculate(integrationData3, integrationResult3);

        System.out.println(integrationResult1.getResult());
        System.out.println(integrationResult2.getResult());
        System.out.println(integrationResult3.getResult());

        assertEquals(-65.416, integrationResult1.getResult(), 1);
        assertEquals(3.36, integrationResult2.getResult(), 0.1);
        assertEquals(3310.183, integrationResult3.getResult(), 100);

        midpointIntegrationCalculator.Calculate(integrationData1, integrationResult1);
        midpointIntegrationCalculator.Calculate(integrationData2, integrationResult2);
        midpointIntegrationCalculator.Calculate(integrationData3, integrationResult3);

        System.out.println(integrationResult1.getResult());
        System.out.println(integrationResult2.getResult());
        System.out.println(integrationResult3.getResult());

        assertEquals(-65.416, integrationResult1.getResult(), 1); //-65.416
        assertEquals(3.36, integrationResult2.getResult(), 0.1);
        assertEquals(3310.183, integrationResult3.getResult(), 100);

        simpsonsIntegrationCalculator.Calculate(integrationData1, integrationResult1);
        simpsonsIntegrationCalculator.Calculate(integrationData2, integrationResult2);
        simpsonsIntegrationCalculator.Calculate(integrationData3, integrationResult3);

        System.out.println(integrationResult1.getResult());
        System.out.println(integrationResult2.getResult());
        System.out.println(integrationResult3.getResult());

        assertEquals(-109.5, integrationResult1.getResult(), 1);
        assertEquals(3.36, integrationResult2.getResult(), 0.1);
        assertEquals(3310.183, integrationResult3.getResult(), 100);

        gaussKronrodIntegrationCalculator.Calculate(integrationData1, integrationResult1);
        gaussKronrodIntegrationCalculator.Calculate(integrationData2, integrationResult2);
        gaussKronrodIntegrationCalculator.Calculate(integrationData3, integrationResult3);

        System.out.println(integrationResult1.getResult());
        System.out.println(integrationResult2.getResult());
        System.out.println(integrationResult3.getResult());

        assertEquals(-65.416, integrationResult1.getResult(), 1);
        assertEquals(3.36, integrationResult2.getResult(), 0.1);
        assertEquals(3310.183, integrationResult3.getResult(), 100);
    }

}