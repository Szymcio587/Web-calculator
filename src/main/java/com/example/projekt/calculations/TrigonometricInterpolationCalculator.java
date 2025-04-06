package com.example.projekt.calculations;

import com.example.projekt.model.Point;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.results.InterpolationResult;
import com.example.projekt.service.UtilityService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrigonometricInterpolationCalculator {

    public void Calculate(InterpolationData interpolationData, InterpolationResult interpolationResult) {
        if (interpolationData == null)
            return;

        int n = interpolationData.getPointsNumber();

        StringBuilder explanation = new StringBuilder();
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

        double minX = interpolationData.getPoints().stream().mapToDouble(Point::getX).min().orElse(0);
        double maxX = interpolationData.getPoints().stream().mapToDouble(Point::getX).max().orElse(0);
        double rangeX = maxX - minX;

        List<Point> transformedPoints = new ArrayList<>();
        for (Point point : interpolationData.getPoints()) {
            double normalizedX = (point.getX() - minX) / rangeX * (2 * Math.PI);
            explanation.append(UtilityService.Round(normalizedX, 3) + " ");
            transformedPoints.add(new Point(normalizedX, point.getY()));
        }

        double normalizedSearchedX = (searchedX - minX) / rangeX * (2 * Math.PI);

        double[] a = new double[n];
        double[] b = new double[n];

        explanation.append(")\n\nKrok 2: Obliczenie kolejnych wyznaczników dla szeregu Fouriera.\n");
        explanation.append("Wyznaczniki te można obliczyć ze wzorów: a_i = 2(y_i * cos(x * (i+1))) / n, b_i = 2(y_i * sin(x * (i+1))) / n");

        for (int q = 0; q < n; q++) {
            for (Point point : transformedPoints) {
                double x = point.getX();
                double y = point.getY();
                a[q] += y * Math.cos(q * x);
                b[q] += y * Math.sin(q * x);
            }
            a[q] /= n;
            if (q > 0) {
                b[q] /= n;
            }
        }

        double result = a[0] / 2;
        for (int q = 1; q < n; q++) {
            result += a[q] * Math.cos(q * normalizedSearchedX) + b[q] * Math.sin(q * normalizedSearchedX);
        }

        explanation.append("\n\nKrok 3: Zsumowanie wszystkich elementów występujących w szeregu.\n");
        explanation.append("Pojedyńczy element szeregu jest równy: a_i * cos(x * i) + b_i * sin(x * i). Po zsumowaniu elementów dla każdego podanego punktu, uzyskujemy finalny " +
                "wynik: " + UtilityService.Round(result, 5));

        interpolationResult.setResult(UtilityService.Round(result, 5));
        interpolationResult.setExplanation(explanation.toString());
    }
}

