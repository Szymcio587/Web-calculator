
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
public class CramerSystemOfEquationsCalculator {

    @Autowired
    private final UtilityService utilityService;

    public void Calculate(SystemOfEquationsData data, SystemOfEquationsResult result) {
        int size = data.getCoefficients().size();
        List<List<Double>> coefficients = data.getCoefficients();
        List<Double> constants = data.getConstants();

        StringBuilder explanation = new StringBuilder();
        explanation.append("Wyjaśnienie krok po kroku:\n\n");
        explanation.append("Krok 1: Obliczenie wyznacznika macierzy głównej. W przypadku gdy będzie różny od zera, układ będzie oznaczony, czyli będzie miał tylko jedno rozwiązanie\n");

        if (coefficients.size() != size || constants.size() != size) {
            throw new IllegalArgumentException("Invalid system of equations.");
        }

        double determinant = CalculateDeterminant(coefficients);
        explanation.append("W naszym przypadku detA = ").append(UtilityService.Round(determinant, 5)).append("\n\n");
        explanation.append("Krok 2: Utworzenie macierzy dla wybranej zmiennej.\n");

        if (Math.abs(determinant) < 1e-9) {
            throw new ArithmeticException("The system has no unique solution (determinant is zero).");
        }

        List<Double> solutions = new java.util.ArrayList<>(size);
        for (int q = 0; q < size; q++) {
            List<List<Double>> modifiedMatrix = ReplaceColumn(coefficients, constants, q);
            double detChanged = CalculateDeterminant(modifiedMatrix);
            solutions.add(utilityService.Round( detChanged / determinant, 3));
            if(q == 0) {
                List<Double> row = new ArrayList<>();
                for(int w = 0; w < size; w++) {
                    row.add(modifiedMatrix.get(w).get(0));
                }
                explanation.append("W zależności od tego którą zmienną analizujemy, naszym celem jest zamiana pewnej konkretnej kolumny w macierzy głównej na kolumnę zawierającą wyrazy wolne." +
                        " Wszystkie pozostałe kolumny pozostają wtedy bez zmian.\n");
                explanation.append("Przykładowo, dla pierwszej zmiennej będziemy zamieniali kolumnę zawierającą wyrazy: ").append(UtilityService.Round(coefficients.get(q), 5))
                        .append(" na wektor wyrazów wolnych: ").append(row).append("\n\n");
                explanation.append("Krok 3: Obliczenie wyznacznika macierzy zmodyfikowanej.\n");
                explanation.append("W naszym przypadku detA_0 = ").append(UtilityService.Round(detChanged, 5)).append("\n\n");
                explanation.append("Krok 4: Zastosowanie wzoru Kramera\n");
                explanation.append("Dla każdego obliczonego wyznacznika zmodyfikowanej macierzy, jesteśmy w stanie obliczyć wartość zmiennej jej odpowiadającej\n");
                explanation.append("Wartość pierwszej zmiennej możemy w ten sposób wyrazić jako x_0 = detA_0/detA = ").append(UtilityService.Round(detChanged, 5)).
                        append("/").append(UtilityService.Round(determinant, 5)).append(" = ").append(UtilityService.Round(solutions.get(0), 5)).append("\n\n");
            }
        }

        explanation.append("\nProces ten powtarzamy dla każdej zmiennej.\n");
        explanation.append("Krok 5: Powtórzenie powyższych kroków 2-4 dla każdej kolejnej zmiennej w celu uzyskania pełnego wektora wyników\n");

        result.setExplanation(explanation.toString());
        result.setSolutions(UtilityService.Round(solutions, 5));
    }

    private List<List<Double>> ReplaceColumn(List<List<Double>> matrix, List<Double> column, int colIndex) {
        int size = matrix.size();
        List<List<Double>> modifiedMatrix = new java.util.ArrayList<>();
        for (int q = 0; q < size; q++) {
            List<Double> row = new java.util.ArrayList<>(matrix.get(q));
            row.set(colIndex, column.get(q));
            modifiedMatrix.add(row);
        }
        return modifiedMatrix;
    }

    private double CalculateDeterminant(List<List<Double>> matrix) {
        int n = matrix.size();
        if (n == 1) return matrix.get(0).get(0);
        if (n == 2) return matrix.get(0).get(0) * matrix.get(1).get(1) - matrix.get(0).get(1) * matrix.get(1).get(0);

        double determinant = 0;
        for (int col = 0; col < n; col++) {
            determinant += Math.pow(-1, col) * matrix.get(0).get(col) * CalculateDeterminant(Minor(matrix, 0, col));
        }
        return determinant;
    }

    private List<List<Double>> Minor(List<List<Double>> matrix, int row, int col) {
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
