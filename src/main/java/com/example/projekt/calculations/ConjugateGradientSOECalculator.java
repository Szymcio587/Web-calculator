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
public class ConjugateGradientSOECalculator {

    private final int MAX_ITERATIONS = 100;
    private final double TOLERANCE = 1;

    @Autowired
    private final UtilityService utilityService;


    public SystemOfEquationsResult Calculate(SystemOfEquationsData data) {
        int size = data.getCoefficients().size();
        List<List<Double>> coefficients = data.getCoefficients();
        List<Double> constants = data.getConstants();

        if (coefficients.size() != size || constants.size() != size) {
            throw new IllegalArgumentException("Invalid system of equations.");
        }

        // Ensure the coefficients matrix is symmetric and positive definite
        if (!isSymmetric(coefficients) || !isPositiveDefinite(coefficients)) {
            throw new IllegalArgumentException("The coefficients matrix must be symmetric and positive definite.");
        }

        // Initialize vectors
        List<Double> x = new ArrayList<>(size);
        List<Double> r = new ArrayList<>(size);
        List<Double> p = new ArrayList<>(size);
        List<Double> Ap = new ArrayList<>(size);

        // Initialize x, r, and p
        for (int i = 0; i < size; i++) {
            x.add(0.0);
            r.add(constants.get(i));
            p.add(constants.get(i));
            Ap.add(0.0);
        }

        double rsOld = dotProduct(r, r);

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            // Calculate Ap = A * p
            for (int i = 0; i < size; i++) {
                double sum = 0.0;
                for (int j = 0; j < size; j++) {
                    sum += coefficients.get(i).get(j) * p.get(j);
                }
                Ap.set(i, sum);
            }

            // Calculate alpha = rsOld / (p^T * Ap)
            double denominator = dotProduct(p, Ap);
            if (Math.abs(denominator) < 1e-10) {
                throw new ArithmeticException("Matrix is singular or nearly singular.");
            }
            double alpha = rsOld / denominator;

            // Update x and r
            for (int i = 0; i < size; i++) {
                x.set(i, x.get(i) + alpha * p.get(i));
                r.set(i, r.get(i) - alpha * Ap.get(i));
            }

            // Check for convergence
            double rsNew = dotProduct(r, r);
            if (Math.sqrt(rsNew) < TOLERANCE) {
                return new SystemOfEquationsResult(x, "ConjugateGradient");
            }

            // Update p = r + (rsNew / rsOld) * p
            double beta = rsNew / rsOld;
            for (int i = 0; i < size; i++) {
                p.set(i, r.get(i) + beta * p.get(i));
            }

            rsOld = rsNew;
        }

        throw new ArithmeticException("Conjugate Gradient method did not converge within the maximum number of iterations.");
    }

    // Helper method to calculate dot product
    private double dotProduct(List<Double> v1, List<Double> v2) {
        double sum = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            sum += v1.get(i) * v2.get(i);
        }
        return sum;
    }

    private boolean isSymmetric(List<List<Double>> matrix) {
        int size = matrix.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!matrix.get(i).get(j).equals(matrix.get(j).get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isPositiveDefinite(List<List<Double>> matrix) {
        int size = matrix.size();
        for (int i = 0; i < size; i++) {
            double sum = 0.0;
            for (int j = 0; j <= i; j++) {
                sum += matrix.get(i).get(j) * matrix.get(i).get(j);
            }
            if (sum <= 0) {
                return false;
            }
        }
        return true;
    }
}
