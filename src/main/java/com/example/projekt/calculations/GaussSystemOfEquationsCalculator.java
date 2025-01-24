
package com.example.projekt.calculations;

import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.SystemOfEquationsResult;
import com.example.projekt.service.UtilityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GaussSystemOfEquationsCalculator {

    @Autowired
    private final UtilityService utilityService;

    public SystemOfEquationsResult Calculate(SystemOfEquationsData data) {
        int size = data.getCoefficients().size();
        List<List<Double>> coefficients = data.getCoefficients();
        List<Double> constants = data.getConstants();

        if (coefficients.size() != size || constants.size() != size) {
            throw new IllegalArgumentException("Invalid system of equations.");
        }

        double determinant = calculateDeterminant(coefficients);
        if (Math.abs(determinant) < 1e-9) {
            throw new ArithmeticException("The system has no unique solution (determinant is zero).");
        }

        List<Double> solutions = new java.util.ArrayList<>(size);
        for (int q = 0; q < size; q++) {
            List<List<Double>> modifiedMatrix = replaceColumn(coefficients, constants, q);
            solutions.add(utilityService.Round(calculateDeterminant(modifiedMatrix) / determinant, 3));
        }

        return new SystemOfEquationsResult(solutions, "SystemOfEquations");
    }

    private List<List<Double>> replaceColumn(List<List<Double>> matrix, List<Double> column, int colIndex) {
        int size = matrix.size();
        List<List<Double>> modifiedMatrix = new java.util.ArrayList<>();
        for (int q = 0; q < size; q++) {
            List<Double> row = new java.util.ArrayList<>(matrix.get(q));
            row.set(colIndex, column.get(q));
            modifiedMatrix.add(row);
        }
        return modifiedMatrix;
    }

    private double calculateDeterminant(List<List<Double>> matrix) {
        int n = matrix.size();
        if (n == 1) return matrix.get(0).get(0);
        if (n == 2) return matrix.get(0).get(0) * matrix.get(1).get(1) - matrix.get(0).get(1) * matrix.get(1).get(0);

        double determinant = 0;
        for (int col = 0; col < n; col++) {
            determinant += Math.pow(-1, col) * matrix.get(0).get(col) * calculateDeterminant(minor(matrix, 0, col));
        }
        return determinant;
    }

    private List<List<Double>> minor(List<List<Double>> matrix, int row, int col) {
        int n = matrix.size();
        List<List<Double>> minor = new java.util.ArrayList<>();
        for (int q = 0; q < n; q++) {
            if (q == row) continue;
            List<Double> minorRow = new java.util.ArrayList<>();
            for (int w = 0; w < n; w++) {
                if (w == col) continue;
                minorRow.add(matrix.get(q).get(w));
            }
            minor.add(minorRow);
        }
        return minor;
    }
}
