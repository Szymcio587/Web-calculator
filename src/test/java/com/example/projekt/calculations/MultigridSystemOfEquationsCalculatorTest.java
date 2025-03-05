package com.example.projekt.calculations;

import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.SystemOfEquationsResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MultigridSystemOfEquationsCalculatorTest {

    @InjectMocks
    private MultigridSystemOfEquationsCalculator multigridSystemOfEquationsCalculator;

    @Test
    public void TestCalculateUniqueSolution() {
        List<List<Double>> coefficients = Arrays.asList(
                Arrays.asList(2.0, 3.0),
                Arrays.asList(1.0, -4.0)
        );
        List<Double> constants = Arrays.asList(8.0, -2.0);

        SystemOfEquationsData data = new SystemOfEquationsData(coefficients, constants);
        SystemOfEquationsResult result = new SystemOfEquationsResult();
        multigridSystemOfEquationsCalculator.Calculate(data, result);

        System.out.println(result.getSolutions().get(0));
        System.out.println(result.getSolutions().get(1));

        assertEquals(2.36363, result.getSolutions().get(0), 0.001);
        assertEquals(1.09090, result.getSolutions().get(1), 0.001);
    }

    @Test
    public void TestCalculateNegativeCoefficients() {
        List<List<Double>> coefficients = Arrays.asList(
                Arrays.asList(-3.0, -1.0),
                Arrays.asList(5.0, -2.0)
        );
        List<Double> constants = Arrays.asList(-7.0, 4.0);

        SystemOfEquationsData data = new SystemOfEquationsData(coefficients, constants);
        SystemOfEquationsResult result = new SystemOfEquationsResult();
        multigridSystemOfEquationsCalculator.Calculate(data, result);

        System.out.println(result.getSolutions().get(0));
        System.out.println(result.getSolutions().get(1));

        assertEquals(1.63636, result.getSolutions().get(0), 0.001);
        assertEquals(2.09090, result.getSolutions().get(1), 0.001);
    }
}