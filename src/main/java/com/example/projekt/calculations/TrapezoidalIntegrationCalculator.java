package com.example.projekt.calculations;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.results.IntegrationResult;
import com.example.projekt.service.UtilityService;
import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrapezoidalIntegrationCalculator {

    private static int counter;

    private double ValueInPoint(List<Double> factors, double q, int degree) {
        double result = 0, partial;
        for (int i = degree; i >= 0; i--) {
            partial = factors.get(i);
            for (int j = i; j > 0; j--) {
                partial *= q;
            }
            result += partial;
        }
        return result;
    }

    public void Calculate(IntegrationData integrationData, IntegrationResult integrationResult) {
        counter = 0;
        double width = integrationData.getXp() >= integrationData.getXk() || integrationData.getSections() <= 0 ? 0
                : (integrationData.getXk() - integrationData.getXp()) / integrationData.getSections();
        double result = 0;
        boolean isCustom = integrationData.getCustomFunction() != null && !integrationData.getCustomFunction().isEmpty();
        double m1 = 0;
        double m2 = 0;
        StringBuilder explanation = new StringBuilder();

        if(isCustom) {
            String customFunction = integrationData.getCustomFunction();
            Function function = new Function("f(x) = " + customFunction);
            if (!function.checkSyntax()) {
                throw new IllegalArgumentException("Invalid function syntax: " + function.getErrorMessage());
            }
            m1 = UtilityService.Round(function.calculate(integrationData.getXp() + (width / 2)), 5);
            m2 = UtilityService.Round(function.calculate(integrationData.getXp() + (width * 1.5)), 5);
        }
        else {
            if(width == 0) {
                integrationResult.setResult(0);
                explanation.append("Szerokość podprzedziału została ustalona jako 0, w związku z czym nie udało się wykonać dalszych obliczeń.");
                return;
            }
            if((integrationData.getDegree() <= 0 || integrationData.getFactors().isEmpty() || integrationData.getDegree() !=
                    integrationData.getFactors().size() - 1)) {
                integrationResult.setResult(0);
                explanation.append("Popełniono błąd podczas podawania stopnia wielomianu bądź też kolejnych współczynników.");
                return;
            }

            m1 = UtilityService.Round(ValueInPoint(integrationData.getFactors(), integrationData.getXp() + (width / 2), integrationData.getDegree()), 5);
            m2 = UtilityService.Round(ValueInPoint(integrationData.getFactors(), integrationData.getXp() + (width * 1.5), integrationData.getDegree()), 5);
        }

        explanation.append("Wyjaśnienie krok po kroku:\n\n");
        explanation.append("Krok 1: Wyznaczenie szerokości podprzedziałów\n");
        explanation.append("Można go wyznaczyć za pomocą wzoru: h=(b-a)/n, Gdzie h - szerokość jednego podprzedziału, [a;b] - granica całego przedziału, n - podana liczba podprzedziałów\n");
        explanation.append("W naszym przypadku: h = (").append(integrationData.getXk()).append(" - ").append(integrationData.getXp()).append(")/").append(integrationData.getSections())
                .append(" = ").append(UtilityService.Round(width, 5)).append("\n\n");
        explanation.append("Krok 2: Obliczenie pola pojedyńczego trapezu\n");
        explanation.append("Korzystając ze wzoru na pole trapezu: P = 1/2 * h * (f(x_{i}) + f(x_{i+1})) jesteśmy w stanie obliczyć przybliżoną wartość całki na przedziale[x_{i};x_{i+1}]\n");
        explanation.append("Wartość i jest wartością iteratora dla kolejnych podzbiorów naszego zbioru głównego [a;b], gdzie i będzie przyjmowało wartości od 0 do n - 1\n");
        explanation.append("Przykładowo, dla pierwszej iteracji wzór ten będzie wyglądał następująco: P = 1/2 * ").append(UtilityService.Round(width, 5)).append(" * (")
                .append(m1).append(" + ").append(m2).append(") = ").append(UtilityService.Round(width * (m1 + m2)/2, 5)).append("\n\n");
        explanation.append("Krok 3: Zsumowanie wszystkich pól takich trapezów\n");
        explanation.append("Wartość przybliżoną całki możemy wyznaczyć ze wzoru: F(x) = P_{0} + P_{1} + ... + P_{n-1}, gdzie F(x) stanowi szukaną wartość całki\n");
        explanation.append("W naszym przypadku F(x) = ");

        if (integrationData.getCustomFunction() != null && !integrationData.getCustomFunction().isEmpty()) {
            String customFunction = integrationData.getCustomFunction();
            Function function = new Function("f(x) = " + customFunction);
            if (!function.checkSyntax()) {
                throw new IllegalArgumentException("Invalid function syntax: " + function.getErrorMessage());
            }

            for (double q = integrationData.getXp(); q < integrationData.getXk(); q += width) {
                double area = ((function.calculate(q)) + function.calculate(q + width)) / 2;
                result += area;
                Append(explanation, area, integrationData);
            }
        }
        else {
            for (double q = integrationData.getXp(); q < integrationData.getXk(); q += width) {
                double area =  (ValueInPoint(integrationData.getFactors(), q, integrationData.getDegree()) +
                        ValueInPoint(integrationData.getFactors(), q + width, integrationData.getDegree())) / 2;
                Append(explanation, area, integrationData);
                result += area;
            }
        }

        result *= width;

        explanation.append(UtilityService.Round(result, 5));

        integrationResult.setExplanation(explanation.toString());
        integrationResult.setResult(UtilityService.Round(result, 5));
    }

    private void Append(StringBuilder explanation, double area, IntegrationData integrationData) {
        counter++;
        if(counter < 10)
            explanation.append(UtilityService.Round(area, 5));
        if(counter < 10 || counter == (integrationData.getSections() - 1)) {
            explanation.append(" + ");
        }
        else if(counter == 10)
            explanation.append("...");
        if(counter == (integrationData.getSections() - 1)) {
            explanation.append(UtilityService.Round(area, 5)).append(" = ");
        }
    }
}

