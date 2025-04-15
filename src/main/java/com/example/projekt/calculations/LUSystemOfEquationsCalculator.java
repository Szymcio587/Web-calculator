package com.example.projekt.calculations;

import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.SystemOfEquationsResult;
import com.example.projekt.service.UtilityService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LUSystemOfEquationsCalculator {

    public void Calculate(SystemOfEquationsData data, SystemOfEquationsResult result) {
        int n = data.getCoefficients().size();
        List<List<Double>> A = data.getCoefficients();
        List<Double> b = data.getConstants();

        if (b.size() != n) {
            result.setSolutions(new ArrayList<>(0));
            result.setExplanation("Liczba zmiennych nie odpowiada rozmiarowi macierzy współczynników.");
            return;
        }

        for (List<Double> row : A) {
            if (row.size() != n) {
                result.setSolutions(new ArrayList<>(0));
                result.setExplanation("W pewnym rzędzie znalazła się nieprawidłowa liczba współczynników.");
                return;
            }
        }

        if (!isSystemDetermined(data.getCoefficients())) {
            result.setSolutions(new ArrayList<>(0));
            result.setExplanation("Układ nie jest oznaczony – brak jednoznacznego rozwiązania.");
            return;
        }

        StringBuilder explanation = new StringBuilder("Wyjaśnienie krok po kroku:\n\n");
        explanation.append("Krok 1: Faktoryzacja macierzy A do postaci A = L * U, gdzie L jest macierzą dolnotrójkątną (wartośni nad przekątną będą równe zero), natomiast" +
                "U jest macierzą górnotrójkątną.\nPoniżej zostanie zaprezentowana wersja rozkładu Doolittle'a, co oznacza że dla macierzy L" +
                "wartości na przekątnej zostaną zamienione na 1, natomiast dla macierzy U będą to dowolne wartości.");

        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        double[][] matrixA = new double[n][n];
        for (int i = 0; i < n; i++) {
            List<Double> row = A.get(i);
            for (int j = 0; j < n; j++) {
                matrixA[i][j] = row.get(j);
            }
        }

        for (int i = 0; i < n; i++) {
            // U - górna trójkątna
            for (int k = i; k < n; k++) {
                double sum = 0;
                for (int j = 0; j < i; j++) {
                    sum += L[i][j] * U[j][k];
                }
                U[i][k] = matrixA[i][k] - sum;
            }

            for (int k = i; k < n; k++) {
                if (i == k) {
                    L[i][i] = 1;
                } else {
                    double sum = 0;
                    for (int j = 0; j < i; j++) {
                        sum += L[k][j] * U[j][i];
                    }
                    if (Math.abs(U[i][i]) < 1e-12) {
                        result.setSolutions(new ArrayList<>(0));
                        result.setExplanation("Podczas wykonywania obliczeń napotkano macierz osobliwą. Nie można wykonać rozkładu LU " +
                                "(dzielenie przez zero).");
                        return;
                    }
                    L[k][i] = (matrixA[k][i] - sum) / U[i][i];
                }
            }
        }

        explanation.append("Macierz L:\n");
        for (int i = 0; i < n; i++) {
            explanation.append(Arrays.toString(Arrays.stream(L[i]).map(val -> UtilityService.Round(val, 5)).toArray())).append("\n");
        }

        explanation.append("\nMacierz U:\n");
        for (int i = 0; i < n; i++) {
            explanation.append(Arrays.toString(Arrays.stream(U[i]).map(val -> UtilityService.Round(val, 5)).toArray())).append("\n");
        }

        explanation.append("\nKrok 2: Rozwiązanie równania pośredniego.\nRozwiązanie domyślne naszego układu równań ma postać: A * x = b, gdzie:\nA - macierz " +
                "współczynników\nx - wektor niewiadomych\nb - wektor wyrazów wolnych\n\n" +
                "Podstawiając pod powyższe równanie A = L * U oraz U * x = y, otrzymujemy równanie: L * y = b.\n Znając wartości L oraz b, możemy wyznaczyć kolejne wartości" +
                "wektora y rozwiązując za każdym razem równania z 1 niewiadomą (Ponieważ L to macierz dolnotrójkątna). Po podstawieniach otrzymujemy:\n");
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += L[i][j] * y[j];
            }
            y[i] = b.get(i) - sum;
            explanation.append("y[").append(i).append("] = ").append(UtilityService.Round(y[i], 5)).append("\n");
        }

        explanation.append("\nKrok 3: Powrót do równania U * x = y\n.Teraz, mając wartości y naszą jedyną niewiadomą jest x, dzięki czemu ponownie dostajemy szereg równań w " +
                "którym każde z nich zawiera wyłącznie 1 niewiadomą (Ponieważ U to macierz górnotrójkątna). Po rozwiązaniu tych równań otrzymujemy:\n");
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i][j] * x[j];
            }
            x[i] = (y[i] - sum) / U[i][i];
            explanation.append("x[").append(i).append("] = ").append(UtilityService.Round(x[i], 5)).append("\n");
        }

        List<Double> solutionList = Arrays.stream(x)
                .mapToObj(val -> UtilityService.Round(val, 5))
                .collect(Collectors.toList());

        result.setSolutions(solutionList);
        result.setExplanation(explanation.toString());
    }

    private boolean isSystemDetermined(List<List<Double>> coefficients) {
        int n = coefficients.size();

        for (List<Double> row : coefficients) {
            if (row.size() != n) {
                return false;
            }
        }

        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = coefficients.get(i).get(j);
            }
        }

        double det = calculateDeterminant(matrix);
        return Math.abs(det) > 1e-9;
    }

    private double calculateDeterminant(double[][] matrix) {
        int n = matrix.length;
        if (n == 1) return matrix[0][0];
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        double det = 0;
        for (int col = 0; col < n; col++) {
            double[][] minor = new double[n - 1][n - 1];
            for (int i = 1; i < n; i++) {
                int minorCol = 0;
                for (int j = 0; j < n; j++) {
                    if (j == col) continue;
                    minor[i - 1][minorCol++] = matrix[i][j];
                }
            }
            det += Math.pow(-1, col) * matrix[0][col] * calculateDeterminant(minor);
        }
        return det;
    }
}
