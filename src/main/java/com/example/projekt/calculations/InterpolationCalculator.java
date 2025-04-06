package com.example.projekt.calculations;

import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.Point;
import com.example.projekt.model.results.InterpolationResult;
import com.example.projekt.service.UtilityService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterpolationCalculator {

    public void Calculate(InterpolationData interpolationData, InterpolationResult interpolationResult) {
        if (interpolationData == null) {
            return;
        }

        StringBuilder explanation = new StringBuilder();
        explanation.append("Wyjaśnienie krok po kroku:\n\n");

        int pointsNumber = interpolationData.getPointsNumber();
        double[] weights = new double[pointsNumber];
        double result = 0;

        explanation.append("Krok 1: Wyznaczenie wzoru ogólnego szukanej funkcji.\nf(x) = ");
        for (int q = 0; q < pointsNumber; q++) {
            weights[q] = 1;
            if(q != 0)
                explanation.append(" + ");
            explanation.append("L").append(q + 1).append("(x)y").append(q + 1);
        }
        explanation.append("\n\n");

        explanation.append("Krok 2: Obliczanie wag dla każdego punktu za pomocą wzoru Lagrange'a.\n");

        String[] numerators = new String[pointsNumber];
        String[] denominators = new String[pointsNumber];
        String[] numeratorsNumbers = new String[pointsNumber];
        String[] denominatorsNumbers = new String[pointsNumber];
        String[] L = new String[pointsNumber];

        for (int i = 0; i < numerators.length; i++) {
            numerators[i] = "";
            denominators[i] = "";
            numeratorsNumbers[i] = "";
            denominatorsNumbers[i] = "";
            L[i] = "";
        }

        for (int q = 0; q < pointsNumber; q++) {
            for (int w = 0; w < pointsNumber; w++) {
                if (q != w) {
                    numerators[w] += "(x - x" + (q + 1) + ")";
                    denominators[w] += "(x" + (w + 1) + " - x" + (q + 1) + ")";
                    numeratorsNumbers[w] += "(x - " + interpolationData.getPoints().get(q).getX() + ")";
                    denominatorsNumbers[w] += "(" + interpolationData.getPoints().get(w).getX() + " - " + interpolationData.getPoints().get(q).getX() + ")";

                    double numerator = interpolationData.getSearchedValue() - interpolationData.getPoints().get(w).getX();
                    double denominator = interpolationData.getPoints().get(q).getX() - interpolationData.getPoints().get(w).getX();
                    weights[q] *= numerator / denominator;
                }
            }
        }

        for (int q = 0; q < pointsNumber; q++) {
            L[q] = "L" + (q + 1) + "(x) = " + numerators[q] + "/" + denominators[q] + " = " + numeratorsNumbers[q] + "/" + denominatorsNumbers[q];
            explanation.append(L[q]).append("\n");
        }

        StringBuilder formula = new StringBuilder("f(x) = ");
        StringBuilder answer = new StringBuilder("f(" + interpolationData.getSearchedValue() + ") = ");

        explanation.append("\nKrok 3: Obliczanie interpolowanego wyniku poprzez sumowanie ważonych wartości y dla wszystkich punktów. Wartość ta jest równoznaczna z wartością" +
                "wyznaczonego wielomianu interpolacyjnego dla szukanego punktu.\n");
        for (int q = 0; q < pointsNumber; q++) {
            double yValue = interpolationData.getPoints().get(q).getY();
            double term = yValue * weights[q];
            result += term;

            formula.append("L").append(q + 1).append("(x) * ").append(yValue);
            answer.append(UtilityService.Round(weights[q], 5)).append(" * ").append(yValue);
            if(q != pointsNumber - 1) {
                formula.append(" + ");
                answer.append(" + ");
            }
        }

        answer.append(" = ").append(UtilityService.Round(result, 5));

        explanation.append(formula).append("\n").append(answer);

        explanation.append("\n\nOstateczny przybliżony wynik interpolacji: ").append(UtilityService.Round(result, 5)).append("\n");

        interpolationResult.setResult(UtilityService.Round(result, 3));
        interpolationResult.setExplanation(explanation.toString());
    }

    public double[] GenerateCoefficients(List<Point> points) {
        int n = points.size();
        double[] coefficients = new double[n];

        for (int q = 0; q < n; q++) {
            double[] basis = CalculateBase(points, q);

            for (int w = 0; w < basis.length; w++) {
                coefficients[w] += basis[w] * points.get(q).getY();
            }
        }

        for (int q = 0; q < n; q++) {
            coefficients[q] = UtilityService.Round(coefficients[q], 5);
        }

        return coefficients;
    }

    private double[] CalculateBase(List<Point> points, int q) {
        int n = points.size();
        double[] base = new double[n];
        base[0] = 1.0;

        for (int w = 0; w < n; w++) {
            if (w!= q) {
                double denominator = points.get(q).getX() - points.get(w).getX();
                for (int e = n - 1; e > 0; e--) {
                    base[e] = base[e - 1] / denominator - (base[e] * points.get(w).getX() / denominator);
                }
                base[0] = -base[0] * points.get(w).getX() / denominator;
            }
        }

        return base;
    }
}
