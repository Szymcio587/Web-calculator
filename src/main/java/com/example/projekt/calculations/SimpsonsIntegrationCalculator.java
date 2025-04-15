
package com.example.projekt.calculations;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.results.IntegrationResult;
import com.example.projekt.service.UtilityService;
import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpsonsIntegrationCalculator {

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
        double width = integrationData.getXp() >= integrationData.getXk() || integrationData.getSections() <= 0 ? 0
                : (integrationData.getXk() - integrationData.getXp()) / integrationData.getSections();
        double result = 0;
        boolean isCustom = integrationData.getCustomFunction() != null && !integrationData.getCustomFunction().isEmpty();
        double m1 = 0;
        double m2 = 0;
        double m3 = 0;
        double value_tmp = 0;
        StringBuilder explanation = new StringBuilder();

        if(isCustom) {
            String customFunction = integrationData.getCustomFunction();
            Function function = new Function("f(x) = " + customFunction);
            if (!function.checkSyntax()) {
                throw new IllegalArgumentException("Invalid function syntax: " + function.getErrorMessage());
            }
            m1 = function.calculate(integrationData.getXp());
            m2 = function.calculate(integrationData.getXp() + width);
            m3 = function.calculate(integrationData.getXp() + width * 2);
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

            m1 = ValueInPoint(integrationData.getFactors(), integrationData.getXp(), integrationData.getDegree());
            m2 = ValueInPoint(integrationData.getFactors(), integrationData.getXp() + width, integrationData.getDegree());
            m3 = ValueInPoint(integrationData.getFactors(), integrationData.getXp() + width * 2, integrationData.getDegree());
        }

        explanation.append("Wyjaśnienie krok po kroku:\n\n");
        explanation.append("Krok 1: Wyznaczenie szerokości podprzedziałów\n");
        explanation.append("Można go wyznaczyć za pomocą wzoru: h=(b-a)/n, Gdzie h - szerokość jednego podprzedziału, [a;b] - granica całego przedziału, n - podana liczba podprzedziałów\n");
        explanation.append("W naszym przypadku: h = (").append(integrationData.getXk()).append(" - ").append(integrationData.getXp()).append(")/").append(integrationData.getSections())
                .append(" = ").append(width).append("\n\n");
        explanation.append("Krok 2: Obliczenie pola pojedyńczego podprzedziału\n");
        explanation.append("Korzystając ze wzoru: P = h/3 * (f(x_{i}) + 4 * f(x_{i+1}) + f(x_{i+2})) jesteśmy w stanie obliczyć przybliżoną wartość całki na przedziale[x_{i};x_{i+2}]\n");
        explanation.append("Wartość i jest wartością iteratora dla kolejnych podzbiorów naszego zbioru głównego [a;b], gdzie i będzie przyjmowało wartości od 0 do n - 1\n");
        explanation.append("Przykładowo, dla pierwszej iteracji wzór ten będzie wyglądał następująco: P = h/3 * (")
                .append(UtilityService.Round(m1, 5)).append(" + 4 * ")
                .append(UtilityService.Round(m2, 5))
                .append(" * ").append(UtilityService.Round(m3, 5)).append(")").append("\n\n");
        explanation.append("Krok 3: Zsumowanie wszystkich wartości podprzedziałów dla całego przedziału\n");
        explanation.append("Wartość przybliżoną całki możemy wyznaczyć ze wzoru: F(x) = P_{0} + P_{1} + ... + P_{n-1}, gdzie F(x) stanowi szukaną wartość całki\n");
        explanation.append("W naszym przypadku F(x) = ");

        if (integrationData.getCustomFunction() != null && !integrationData.getCustomFunction().isEmpty()) {
            String customFunction = integrationData.getCustomFunction();
            Function function = new Function("f(x) = " + customFunction);
            if (!function.checkSyntax()) {
                throw new IllegalArgumentException("Invalid function syntax: " + function.getErrorMessage());
            }

            for (int q = 0; q < integrationData.getSections(); q += 2) {
                double x0 = integrationData.getXp() + q * width;
                double x1 = x0 + width;
                double x2 = x1 + width;

                value_tmp = function.calculate(x0) +
                        4 * function.calculate(x1) +
                        function.calculate(x2);
                result += value_tmp;
                Append(explanation, value_tmp, integrationData);
            }
        }
        else {
            for (int q = 0; q < integrationData.getSections(); q += 2) {
                double x0 = integrationData.getXp() + q * width;
                double x1 = x0 + width;
                double x2 = x1 + width;

                value_tmp = ValueInPoint(integrationData.getFactors(), x0, integrationData.getDegree()) +
                        4 * ValueInPoint(integrationData.getFactors(), x1, integrationData.getDegree()) +
                        ValueInPoint(integrationData.getFactors(), x2, integrationData.getDegree());
                result += value_tmp;
                Append(explanation, value_tmp, integrationData);
            }
        }

        result *= width / 3;

        explanation.append(UtilityService.Round(result, 5));

        integrationResult.setExplanation(explanation.toString());
        integrationResult.setResult(UtilityService.Round(result, 5));
    }

    private void Append(StringBuilder explanation, double result, IntegrationData integrationData) {
        counter++;
        if(counter < 10)
            explanation.append(UtilityService.Round(result, 5));
        if(counter < 10 || counter >= (integrationData.getSections()/2)) {
            explanation.append(" + ");
        }
        else if(counter == 10)
            explanation.append("...");
        if(counter >= (integrationData.getSections()/2)) {
            explanation.append(UtilityService.Round(result, 5)).append(" = ");
        }
    }
}
