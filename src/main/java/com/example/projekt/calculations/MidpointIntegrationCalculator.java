
package com.example.projekt.calculations;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.results.IntegrationResult;
import com.example.projekt.service.UtilityService;
import org.mariuszgromada.math.mxparser.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MidpointIntegrationCalculator {

    private static int counter;

    private double ValueInPoint(List<Double> factors, double q, int degree) {
        double result = 0, partial = 1;
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
        StringBuilder explanation = new StringBuilder();
        explanation.append("Wyjaśnienie krok po kroku:\n\n");

        double width = (integrationData.getXk() - integrationData.getXp()) / integrationData.getSections();
        double result = 0;
        boolean isCustom = integrationData.getCustomFunction() != null && !integrationData.getCustomFunction().isEmpty();
        double m = 0;
        double m1 = 0;
        double m2 = 0;

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
            m1 = UtilityService.Round(ValueInPoint(integrationData.getFactors(), integrationData.getXp() + (width / 2), integrationData.getDegree()), 5);
            m1 = UtilityService.Round(ValueInPoint(integrationData.getFactors(), integrationData.getXp() + (width * 1.5), integrationData.getDegree()), 5);
        }

        explanation.append("Krok 1: Wyznaczenie szerokości podprzedziałów\n");
        explanation.append("Można go wyznaczyć za pomocą wzoru: h=(b-a)/n, Gdzie h - szerokość jednego podprzedziału, [a,b] - granica całego przedziału, n - podana liczba podprzedziałów\n");
        explanation.append("W naszym przypadku: h = (").append(UtilityService.Round(integrationData.getXk(), 5)).append(" - ").append(UtilityService.Round(integrationData.getXp(), 5))
                .append(")/").append(UtilityService.Round(integrationData.getSections(), 5)).append(" = ").append(UtilityService.Round(width, 5)).append("\n\n");
        explanation.append("Krok 2: Znalezienie punktów środkowych każdego podprzedziału\n");
        explanation.append("Punkt ten dany jest wzorem: m_{i} = (x_{i} + x_{i+1})/2, gdzie i będzie przyjmowało wartości z zakresu [0;n-1]\n");
        explanation.append("Przykładowo dla pierwszej iteracji wzór ten będzie wyglądał następująco: m_{0} = (").append(m1).append(" + ")
                .append(m2).append(")/2 = ").append(UtilityService.Round((m1 + m2)/2, 5)).append("\n\n");
        explanation.append("Krok 3: Zsumowanie wszystkich wartości funkcji dla m_{i}\n");
        explanation.append("Wartość przybliżoną całki możemy wyznaczyć ze wzoru: F(x) = h(f(m_{0}) + f(m_{1}) + ... + f(m_{n-1})), gdzie F(x) stanowi szukaną wartość, a f(m) wartość funkcji dla danego punktu\n");
        explanation.append("W naszym przypadku F(x) = ").append(UtilityService.Round(width, 5)).append("(");

        if (isCustom) {
            String customFunction = integrationData.getCustomFunction();
            Function function = new Function("f(x) = " + customFunction);
            if (!function.checkSyntax()) {
                throw new IllegalArgumentException("Invalid function syntax: " + function.getErrorMessage());
            }

            for (double q = integrationData.getXp() + width / 2; q < integrationData.getXk(); q += width) {
                m = function.calculate(q);
                result += m;
                Append(explanation, m, integrationData);
            }
        } else {
            for (double q = integrationData.getXp() + width / 2; q < integrationData.getXk(); q += width) {
                m = ValueInPoint(integrationData.getFactors(), q, integrationData.getDegree());
                result += m;
                Append(explanation, m, integrationData);
            }
        }

        result *= width;
        explanation.append(UtilityService.Round(result, 5));
        integrationResult.setExplanation(explanation.toString());
        integrationResult.setResult(UtilityService.Round(result, 5));
    }

    private void Append(StringBuilder explanation, double point, IntegrationData integrationData) {
        counter++;
        if(counter < 10)
            explanation.append(UtilityService.Round(point, 5));
        if(counter < 10 || counter == (integrationData.getSections() - 1)) {
            explanation.append(" + ");
        }
        else if(counter == 10)
            explanation.append("...");
        if(counter == (integrationData.getSections() - 1)) {
            explanation.append(UtilityService.Round(point, 5)).append(") = ");
        }
    }
}
