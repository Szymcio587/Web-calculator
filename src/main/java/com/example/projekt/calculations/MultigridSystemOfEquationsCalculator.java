package com.example.projekt.calculations;

import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.SystemOfEquationsResult;
import com.example.projekt.service.UtilityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MultigridSystemOfEquationsCalculator {

    @Autowired
    private final UtilityService utilityService;

    public SystemOfEquationsResult Calculate(SystemOfEquationsData data) {
        int size = data.getCoefficients().size();
        List<List<Double>> coefficients = data.getCoefficients();
        List<Double> constants = data.getConstants();

        if (coefficients.size() != size || constants.size() != size) {
            throw new IllegalArgumentException("Invalid system of equations.");
        }

        List<Double> solution = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            solution.add(0.0);
        }

        solution = multigridSolve(coefficients, constants, solution, 1e-6, 100);

        return new SystemOfEquationsResult(solution, "MultigridSolution");
    }

    private List<Double> multigridSolve(List<List<Double>> A, List<Double> b, List<Double> x, double tolerance, int maxIterations) {
        int n = A.size();

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            x = smooth(A, b, x, 3);

            List<Double> residual = calculateResidual(A, b, x);

            double residualNorm = calculateNorm(residual);
            if (residualNorm < tolerance) {
                return x;
            }

            List<Double> coarseResidual = restrictResidual(residual);

            List<Double> coarseError = solveCoarseGrid(coarseResidual);

            List<Double> fineError = prolongateError(coarseError, n);

            for (int i = 0; i < n; i++) {
                x.set(i, x.get(i) + fineError.get(i));
            }
        }

        throw new ArithmeticException("Multigrid method did not converge within the maximum number of iterations.");
    }

    private List<Double> smooth(List<List<Double>> A, List<Double> b, List<Double> x, int iterations) {
        int n = A.size();
        for (int iter = 0; iter < iterations; iter++) {
            for (int i = 0; i < n; i++) {
                double sigma = 0.0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        sigma += A.get(i).get(j) * x.get(j);
                    }
                }
                x.set(i, (b.get(i) - sigma) / A.get(i).get(i));
            }
        }
        return x;
    }

    private List<Double> calculateResidual(List<List<Double>> A, List<Double> b, List<Double> x) {
        int n = A.size();
        List<Double> residual = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += A.get(i).get(j) * x.get(j);
            }
            residual.add(b.get(i) - sum);
        }
        return residual;
    }

    private List<Double> restrictResidual(List<Double> residual) {
        // Placeholder: Implement a restriction operator to map residual to a coarser grid
        return residual; // Simplified for demonstration
    }

    private List<Double> solveCoarseGrid(List<Double> coarseResidual) {
        // Placeholder: Implement coarse grid solver (e.g., direct solver or iterative method)
        return coarseResidual; // Simplified for demonstration
    }

    private List<Double> prolongateError(List<Double> coarseError, int fineGridSize) {
        List<Double> fineError = new ArrayList<>(fineGridSize);
        for (int i = 0; i < fineGridSize; i++) {
            fineError.add(0.0);
        }
        return fineError;
    }

    public double calculateNorm(List<Double> vector) {
        double sum = 0.0;
        for (Double value : vector) {
            sum += value * value;
        }
        return Math.sqrt(sum);
    }
}

