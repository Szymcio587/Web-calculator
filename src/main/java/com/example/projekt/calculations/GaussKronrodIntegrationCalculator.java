package com.example.projekt.calculations;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.results.IntegrationResult;
import com.example.projekt.service.UtilityService;
import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GaussKronrodIntegrationCalculator {

    private static int counter;

    private final double[] WEIGHTS = {
            0.022935322010529224963732008058970,
            0.063092092629978553290700663189204,
            0.104790010322250183839876322541518,
            0.140653259715525918745189590510238,
            0.169004726639267902826583426598550,
            0.190350578064785409913256402421014,
            0.204432940075298892414161999234649,
            0.209482141084727828012999174891714,
            0.204432940075298892414161999234649,
            0.190350578064785409913256402421014,
            0.169004726639267902826583426598550,
            0.140653259715525918745189590510238,
            0.104790010322250183839876322541518,
            0.063092092629978553290700663189204,
            0.022935322010529224963732008058970
    };
    private final double[] POINTS = {
            -0.991455371120812639206854697526329,
            -0.949107912342758524526189684047851,
            -0.864864423359769072789712788640926,
            -0.741531185599394439863864773280788,
            -0.586087235467691130294144838258730,
            -0.405845151377397166906606412076961,
            -0.207784955007898467600689403773245,
            0.000000000000000000000000000000000,
            0.207784955007898467600689403773245,
            0.405845151377397166906606412076961,
            0.586087235467691130294144838258730,
            0.741531185599394439863864773280788,
            0.864864423359769072789712788640926,
            0.949107912342758524526189684047851,
            0.991455371120812639206854697526329
    };

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

        double a = integrationData.getXp();
        double b = integrationData.getXk();
        double midpoint = (a + b) / 2;
        double halfWidth = (b - a) / 2;
        double result = 0;

        if(a >= b) {
            integrationResult.setResult(0);
            explanation.append("Punkt początkowy znajduje się dalej niż punkt końcowy, w związku z czym nie udało się wykonać dalszych obliczeń.");
            return;
        }

        if((integrationData.getDegree() <= 0 || integrationData.getFactors().isEmpty() || integrationData.getDegree() !=
                integrationData.getFactors().size() - 1)) {
            integrationResult.setResult(0);
            explanation.append("Popełniono błąd podczas podawania stopnia wielomianu bądź też kolejnych współczynników.");
            return;
        }

        explanation.append("Wyjaśnienie krok po kroku:\n\n");
        explanation.append("Krok 1: Zdefiniowanie stałych\n");
        explanation.append("Aby skutecznie wykorzystać metodę Gaussa-Kronroda, należy najpierw zdefiniować zasadę 15 punktów Kronroda, dla których będą wyliczanie wartości po " +
                "znormalizowaniu zakresu do przedziału [-1;1]. Są to wartości predefiniowane, czyli narzucone odgórnie w ramach tej właśnie metody.\n");
        explanation.append("Poniżej wypisane zostaną kolejno wartości punktów oraz ich poszczególne wagi:\n");
        explanation.append("Punkty: \n");
        for(double point : POINTS) {
            explanation.append(UtilityService.Round(point, 5)).append(" ");
        }
        explanation.append("\nWagi: \n");
        for(double weight : WEIGHTS) {
            explanation.append(UtilityService.Round(weight, 5)).append(" ");
        }
        explanation.append("\n\nKrok 2: wyznaczenie połowy długości całego przedziału oraz punktu środkowego\n");
        explanation.append("Można te wartości wyznaczyć za pomocą wzorów: h=(b-a)/2 oraz x_s=(a+b)/2, Gdzie h - połowa długości, x_s - punktu środkowy, [a,b] - granica całego przedziału\n");
        explanation.append("W naszym przypadku: \nh = (").append(UtilityService.Round(integrationData.getXk(), 5)).append(" - ").append(UtilityService.Round(integrationData.getXp(), 5))
                .append(")/").append(2).append(" = ").append(UtilityService.Round(halfWidth, 5)).append("\n");
        explanation.append("x_s = (").append(UtilityService.Round(integrationData.getXk(), 5)).append(" + ").append(UtilityService.Round(integrationData.getXp(), 5))
                .append(")/").append(2).append(" = ").append(UtilityService.Round(midpoint, 5)).append("\n\n");

        explanation.append("Krok 3: Zsumowanie wszystkich wartości funkcji dla kolejnych punktów zbioru\n");
        explanation.append("W tym celu, najpierw dostowujemy punkty ze stałego zakresu powyżej do punktów odpowiadających im dla naszego przedziału, korzystając ze wzoru " +
                "x = x_s + h * p_n,\ngdzie p_n to wybrany punkty z listy.\n");
        explanation.append("Następnie, obliczamy wartość funkcji w tak wybranym punkcie, i sumujemy ze sobą wszystkie 15 wartości wymnożone przez odpowiadające im wagi\n\n");

        if (integrationData.getCustomFunction() != null && !integrationData.getCustomFunction().isEmpty()) {
            String customFunction = integrationData.getCustomFunction();
            Function function = new Function("f(x) = " + customFunction);
            if (!function.checkSyntax()) {
                throw new IllegalArgumentException("Invalid function syntax: " + function.getErrorMessage());
            }

            for (int q = 0; q < POINTS.length; q++) {
                double x = midpoint + halfWidth * POINTS[q];
                result += WEIGHTS[q] * function.calculate(x);
            }
        }
        else {
            for (int q = 0; q < POINTS.length; q++) {
                double x = midpoint + halfWidth * POINTS[q];
                result += WEIGHTS[q] * ValueInPoint(integrationData.getFactors(), x, integrationData.getDegree());
            }
        }
        explanation.append("Po wykonaniu powyższych operacji uzyskujemy wynik: " + UtilityService.Round(result, 5));

        result *= halfWidth;
        integrationResult.setExplanation(explanation.toString());
        integrationResult.setResult(UtilityService.Round(result, 5));
    }
}