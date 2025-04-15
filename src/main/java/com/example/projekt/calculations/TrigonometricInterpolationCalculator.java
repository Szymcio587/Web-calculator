package com.example.projekt.calculations;

import com.example.projekt.model.Point;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.results.InterpolationResult;
import com.example.projekt.service.UtilityService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TrigonometricInterpolationCalculator {

    public void Calculate(InterpolationData interpolationData, InterpolationResult interpolationResult) {
        if (interpolationData == null || interpolationData.getPoints() == null || interpolationData.getPoints().isEmpty()) {
            interpolationResult.setResult(0);
            interpolationResult.setExplanation("Nie podano żadnego punktu do interpolacji.");
            return;
        }

        StringBuilder explanation = new StringBuilder();

        interpolationData.setPoints(removeDuplicateXPoints(interpolationData.getPoints()));
        if(interpolationData.getPoints().size() != interpolationData.getPointsNumber()) {
            explanation.append("UWAGA: z powodu duplikujących się punktów, usunięto ")
                    .append(interpolationData.getPointsNumber() - interpolationData.getPoints().size())
                    .append(" z nich.\n\n");
            interpolationData.setPointsNumber(interpolationData.getPoints().size());
        }

        if (interpolationData.getPoints().size() == 1) {
            interpolationResult.setResult(UtilityService.Round(interpolationData.getPoints().get(0).getY(), 3));
            interpolationResult.setExplanation("Został podany wyłącznie 1 punkt, więc wartość interpolacji jest równa wartości dla tego" +
                    "punktu.");
            return;
        }

        for(Point point : interpolationData.getPoints()) {
            if(point.getX() == interpolationData.getSearchedValue()) {
                interpolationResult.setResult(UtilityService.Round(point.getY(), 3));
                interpolationResult.setExplanation("Punkt dla którego poszukiwana jest wartość pokrywa się z jednym z podanych punktów, więc" +
                        " jego wynik został ustawiony na wartość podanego punktu.");
                return;
            }
        }

        int n = interpolationData.getPointsNumber();

        explanation.append("Wyjaśnienie krok po kroku:\n\n");
        explanation.append("Krok 1: Normalizacja wartości X podanych punktów do zakresu [0;2PI].\nJeżeli został podany tylko jeden punkt, algorytm zwróci po prostu wartość Y dla tego punktu," +
                " ponieważ nie ma wystarczającej liczby danych do wykonania interpolacji. Dla tego przypadku podano " + n + " punktów, więc ");

        if(n == 1) {
            explanation.append("w tym miejscu można już zakończyć działanie algorytmu i wyświetlić wynik");

            explanation.append("\n\nOstateczny przybliżony wynik interpolacji: ").append(UtilityService.Round(interpolationData.getPoints().get(0).getY(), 5)).append("\n");
            interpolationResult.setResult(UtilityService.Round(interpolationData.getPoints().get(0).getY(), 5));
            interpolationResult.setExplanation(explanation.toString());
            return;
        }

        explanation.append("będziemy kontynuować normalizację wszystkich punktów. Ten krok ma na celu umożliwienie potraktowania podanych punktów jako punktów funkcji okresowej, " +
                "co z kolei pozwoli na zastosowanie interpolacji trygonometrycznej.");
        explanation.append("\nBędzie się ona odbywała wedle wzoru: x_i = (x - minX)/rangeX * 2 * PI, gdzie:\nx_i - znormalizowana wartość x\nx - pierwotna wartość x\nminX - " +
                "najmniejsza podana wartość x w danych wejściowych\nrangeX - różnica pomiędzy wartościami zakresu pierwotnego");
        explanation.append("\nWartości kolejnych znormalizowanych punktów dla tego przypadku: ( ");

        double searchedX = interpolationData.getSearchedValue();

        List<Point> inputPoints = interpolationData.getPoints()
                .stream()
                .sorted(Comparator.comparingDouble(Point::getX))
                .collect(Collectors.toList());

        double minX = inputPoints.get(0).getX();
        double maxX = inputPoints.get(n - 1).getX();
        double rangeX = maxX - minX;

        List<Point> transformedPoints = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            double yi = inputPoints.get(i).getY();
            transformedPoints.add(new Point(angle, yi));
        }

        double normalizedSearchedX = (searchedX - minX) / rangeX * (2 * Math.PI);

        int m = n / 2;
        double[] a = new double[m + 1];
        double[] b = new double[m];

        explanation.append(")\n\nKrok 2: Obliczenie kolejnych wyznaczników dla szeregu Fouriera.\n");
        explanation.append("Wyznaczniki te można obliczyć ze wzorów: a_k = (2/n) * Σ y_j * cos(k * x_j), b_k = (2/n) * Σ y_j * sin(k * x_j)\n");

        for (int k = 0; k <= m; k++) {
            for (int j = 0; j < n; j++) {
                double xj = transformedPoints.get(j).getX();
                double yj = transformedPoints.get(j).getY();

                a[k] += yj * Math.cos(k * xj);
                if (k > 0 && k < m) {
                    b[k] += yj * Math.sin(k * xj);
                }
            }
            a[k] *= 2.0 / n;
            if (k > 0 && k < m) {
                b[k] *= 2.0 / n;
            }
        }

        double result = a[0] / 2;
        for (int k = 1; k < m; k++) {
            result += a[k] * Math.cos(k * normalizedSearchedX) + b[k] * Math.sin(k * normalizedSearchedX);
        }
        result += a[m] * Math.cos(m * normalizedSearchedX);  // b_m is not used for Nyquist frequency

        explanation.append("\n\nKrok 3: Zsumowanie wszystkich elementów występujących w szeregu.\n");
        explanation.append("Pojedyńczy element szeregu jest równy: a_i * cos(x * i) + b_i * sin(x * i). Po zsumowaniu elementów dla każdego podanego punktu, uzyskujemy finalny " +
                "wynik: " + UtilityService.Round(result, 5));

        interpolationResult.setResult(UtilityService.Round(result, 5));
        interpolationResult.setExplanation(explanation.toString());
    }

    public static List<Point> removeDuplicateXPoints(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Double> seenX = new HashSet<>();
        List<Point> filteredPoints = new ArrayList<>();

        for (Point point : points) {
            if (point == null) continue;
            if (!seenX.contains(point.getX())) {
                seenX.add(point.getX());
                filteredPoints.add(point);
            }
        }

        return filteredPoints;
    }
}

