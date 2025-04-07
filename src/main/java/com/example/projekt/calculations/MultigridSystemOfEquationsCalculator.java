package com.example.projekt.calculations;

import com.example.projekt.model.data.SystemOfEquationsData;
import com.example.projekt.model.results.SystemOfEquationsResult;
import com.example.projekt.service.UtilityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MultigridSystemOfEquationsCalculator {

    private final int LOOP_ITERATIONS = 200;
    private final int SMOOTH_ITERATIONS = 5;
    private final int COARSE_GRID_ITERATIONS = 5;

    private final int RESIDUAL_RESTRICTION = 2;
    private final double TOLERANCE = 0.00001;

    public void Calculate(SystemOfEquationsData data, SystemOfEquationsResult result) {
        if (data.getConstants().size() != data.getCoefficients().size()) {
            throw new IllegalArgumentException("Invalid system of equations.");
        }

        StringBuilder explanation = new StringBuilder();
        explanation.append("Wyjaśnienie krok po kroku:\n\n");
        explanation.append("Krok 1: Sprowadzenie macierzy układu równań do postaci diagonalnie-dominującej, tj. dla każdego wiersza wartość na przekątnej jest większa od sumy pozostałych" +
                "wartości w wierszu.\n");
        explanation.append("Odbywa się to poprzez sprawdzenie kolejnych wierszy pod kątem spełnienia tej zależności. W przypadku gdy pewien wiersz nie spełnia tej zależności, " +
                "następuje zamiana tego konkretnego wiersza z wierszem posiadającym największą wartość w danym miejscu na przekątnej, tak aby otrzymać postać macierzy " +
                "diagonalnie dominującej\n");

        List<Double> solution = new ArrayList<>(data.getCoefficients().size());
        for (int i = 0; i < data.getCoefficients().size(); i++) {
            solution.add(0.0);
        }

        SystemOfEquationsData cp = data;
        data.setCoefficients(MakeDiagonallyDominant(data.getCoefficients(), data.getConstants(), explanation));

        explanation.append("\nKrok 2: Przejście do obliczeń na poprawionej macierzy.\n");
        explanation.append("Metoda wielosiatkowa do obliczeń stosuje tzw. algorytm V-cycle, który polega na iteracyjnym przeprowadzeniu następujących etapów:\n");

        solution = MultigridSolve(data.getCoefficients(), data.getConstants(), solution, explanation);
        boolean isGood = true;
        for(int i = 0; i < solution.size(); i++) {
            if(solution.get(i).isNaN()) {
                solution.set(i, Double.valueOf(0));
                isGood = false;
            }
        }
        if(isGood)
            explanation.append("\nPo wykonaniu wszystkich obliczeń, ostateczne wartości zmiennych wynoszą: ");
        else
            explanation.append("\nW trakcie obliczeń wystąpiły błędy. Źle obliczone wartości zostały oznaczone jako 0: ");
        explanation.append(UtilityService.Round(solution, 5));

        result.setSolutions(UtilityService.Round(solution, 5));
        result.setExplanation(explanation.toString());

        //Zresetowanie danych pierwotnych dla zapisu do bazy danych
        data.setCoefficients(cp.getCoefficients());
        data.setConstants(cp.getConstants());
    }

    private List<Double> MultigridSolve(List<List<Double>> A, List<Double> b, List<Double> x, StringBuilder explanation) {
        for (int iteration = 0; iteration < LOOP_ITERATIONS; iteration++) {
            x = Smooth(A, b, x);

            if(iteration == 0) {
                explanation.append("- Wygładzenie, czyli obliczenie wstępnego rozwiązania inną prostszą metodą iteracyjną na macierzy. W ramach tego przykładu została zastosowana" +
                        " metoda Gaussa-Seidla\n");
                explanation.append("W naszym przypadku, po pierwszej iteracji metody wielosiatkowej wstępne rozwiązania wynoszą: ");
                for(int i = 0; i < x.size(); i++) {
                    explanation.append(UtilityService.Round(x.get(i), 5));
                    if(i != x.size() - 1)
                        explanation.append(", ");
                }
            }

            List<Double> residual = CalculateResidual(A, b, x);

            double residualNorm = CalculateNorm(residual);

            if(iteration == 0) {
                explanation.append("\n- Obliczenie łącznej wartości błędu. W przypadku gdy uzyskany błąd obliczeniowy na tym etapie będzie mniejszy od wymaganej dokładności," +
                        " która w tym przypadku wynosi " + TOLERANCE + " możemy zakończyć obliczenia. W przeciwnym wypadku, przechodzimy do dalszych kroków.\n");
                explanation.append("Wartość błędu po pierwszej iteracji algorytmu: " + residualNorm);
            }

            if (residualNorm < TOLERANCE) {
                return x;
            }

            List<Double> coarseResidual = RestrictResidual(residual);

            if(iteration == 0) {
                explanation.append("\n- Restrykcja, czyli projekcja pierwotnej siatki rozwiązań. Kolejne wartości mniejszej siatki są obliczanie poprzez uśrednienie sąsiadujących wartości" +
                        "elementów na siatce pierwotnej.\n");
                explanation.append("Po wykonaniu tej operacji, wartości błędu na skurczonej siatce wynoszą: ");
                for(int i = 0; i < coarseResidual.size(); i++) {
                    explanation.append(UtilityService.Round(coarseResidual.get(i), 5));
                    if(i != coarseResidual.size() - 1)
                        explanation.append(", ");
                }
            }

            List<Double> coarseError = SolveCoarseGrid(coarseResidual);

            if(iteration == 0) {
                explanation.append("\n- Rozwiązanie problemu na mniejszej siatce. W tym celu możemy ponownie skorzystać z jakiejś prostszej metody numerycznej(np. Gaussa-Seidla).\n");
                explanation.append("Po wykonaniu tej operacji, wartości na skurczonej siatce wynoszą: ");
                for(int i = 0; i < coarseError.size(); i++) {
                    explanation.append(UtilityService.Round(coarseError.get(i), 5));
                    if(i != coarseError.size() - 1)
                        explanation.append(", ");
                }
            }

            List<Double> fineError = ProlongateError(coarseError, A.size());

            for (int i = 0; i < A.size(); i++) {
                x.set(i, x.get(i) + fineError.get(i));
            }

            if(iteration == 0) {
                explanation.append("\n- Interpolacja rozwiązania na pierwotny problem. Po rozpatrzeniu mniejszej siatki błędów, poszerzamy ją do wymiarów problemu pierwotnego, " +
                        "poprzez duplikację jej elementów.\nNastępnie, wykonujemy aktualizację wartości wstępnych roziązań dodając do nich wartość błędu ze skurczonej siatki.");
                explanation.append("Zaktualizowane wartości rozwiązań po pierwszej iteracji algorytmu: ");
                for(int i = 0; i < x.size(); i++) {
                    explanation.append(UtilityService.Round(x.get(i), 5));
                    if(i != x.size() - 1)
                        explanation.append(", ");
                }
                explanation.append("\n\nZastosowanie iteracyjnie powyższych kroków pozwala na rozwiązanie układu równań.\n");
            }
        }

        return x;
    }

    private List<Double> Smooth(List<List<Double>> A, List<Double> b, List<Double> x) {
        int n = A.size();
        for (int iter = 0; iter < SMOOTH_ITERATIONS; iter++) {
            for (int i = 0; i < n; i++) {
                double sigma = 0.0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        sigma += A.get(i).get(j) * x.get(j);
                    }
                }
                x.set(i, (b.get(i) - sigma) / A.get(i).get(i));
            }
        }
        return x;
    }

    private List<Double> CalculateResidual(List<List<Double>> A, List<Double> b, List<Double> x) {
        int n = A.size();
        List<Double> residual = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += A.get(i).get(j) * x.get(j);
            }
            residual.add(b.get(i) - sum);
        }
        return residual;
    }

    private List<Double> RestrictResidual(List<Double> residual) {
        List<Double> coarseResidual = new ArrayList<>();
        for (int i = 0; i < residual.size() - 1; i += RESIDUAL_RESTRICTION) {
            double restrictedValue = (residual.get(i) + residual.get(i + 1)) / RESIDUAL_RESTRICTION;
            coarseResidual.add(restrictedValue);
        }
        return coarseResidual;
    }

    private List<Double> SolveCoarseGrid(List<Double> coarseResidual) {
        int n = coarseResidual.size();
        List<List<Double>> A_c = createCoarseGridMatrix(n);
        List<Double> coarseError = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            coarseError.add(0.0);
        }

        for (int iter = 0; iter < COARSE_GRID_ITERATIONS; iter++) {
            for (int i = 0; i < n; i++) {
                double sigma = 0.0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        sigma += A_c.get(i).get(j) * coarseError.get(j);
                    }
                }
                if (A_c.get(i).get(i) != 0) {
                    coarseError.set(i, (coarseResidual.get(i) - sigma) / A_c.get(i).get(i));
                }
            }
        }

        return coarseError;
    }
    private List<List<Double>> createCoarseGridMatrix(int size) {
        List<List<Double>> matrix = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    row.add(4.0);
                } else if (Math.abs(i - j) == 1) {
                    row.add(1.0);
                } else {
                    row.add(0.0);
                }
            }
            matrix.add(row);
        }
        return matrix;
    }

    private List<Double> ProlongateError(List<Double> coarseError, int fineGridSize) {
        List<Double> fineError = new ArrayList<>();
        for (int i = 0; i < coarseError.size(); i++) {
            fineError.add(coarseError.get(i));
            fineError.add(coarseError.get(i));
        }

        if (fineError.size() < fineGridSize) {
            fineError.add(0.0);
        }

        return fineError;
    }

    private double CalculateNorm(List<Double> vector) {
        double sum = 0.0;
        for (Double value : vector) {
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    private List<List<Double>> MakeDiagonallyDominant(List<List<Double>> matrix, List<Double> constants, StringBuilder explanation) {
        for (int q = 0; q < matrix.size(); q++) {
            int maxIndex = q;

            for (int w = 0; w < matrix.size(); w++) {
                if (Math.abs(matrix.get(w).get(q)) > Math.abs(matrix.get(maxIndex).get(q))) {
                    maxIndex = w;
                }
            }
            if(q == 0) {
                explanation.append("Dla tego przypadku, w pierwszym wierszu macierzy obecna wartość na przekątnej wynosi: " + matrix.get(q).get(q) + ", a najwyższa występująca " +
                        "wartość wynosi: " + matrix.get(maxIndex).get(q) + ", tak więc obie te wartości są ");
                if(maxIndex != q)
                    explanation.append("różne.\n");
                else
                    explanation.append("równe.\n");
            }
            if (maxIndex != q) {
                if(q == 0)
                    explanation.append("W związku z tym, dochodzi w tym momencie do zamiany wiersza " + (q + 1) + " z wierszem " + (maxIndex + 1) + "\n");
                Collections.swap(matrix, q, maxIndex);
                Collections.swap(constants, q, maxIndex);
            }

            double rowSum = 0.0;
            for (int w = 0; w < matrix.size(); w++) {
                if (q != w) {
                    rowSum += Math.abs(matrix.get(q).get(w));
                }
            }

            if (Math.abs(matrix.get(q).get(q)) < rowSum) {
                double d = matrix.get(q).get(q);
                matrix.get(q).set(q, rowSum + 1.0);
                for(int w = 0; w < matrix.size(); w++) {
                    if(q != w) {
                        matrix.get(q).set(w, matrix.get(q).get(w) * (rowSum + 1.0) / d);
                    }
                }
                constants.set(q, constants.get(q) * (rowSum + 1.0) / d);
                if(q == 0) {
                    explanation.append("\nPo ewentualnej zamianie wierszy, należy jeszcze przeskalować wartości znajdujące się w wierszu tak, aby spełniona była wyżej założona " +
                            "nierówność, tj. aby wartość na przekątnej była większa od sumy wartości poza nią. W tym celu, do wartości na przekątnej dodajemy sumę pozostałych " +
                            "wierszy + 1, natomiast resztę mnożymy przez tę właśnie wartość i dzielimy przez pierwotną wartość na przekątnej.\n");
                }
            }
            if(q == 0) {
                explanation.append("Po tej operacji, wartości w pierwszym rzędzie macierzy wyglądają następująco: ");
                for(int i = 0; i < matrix.size(); i++) {
                    explanation.append(matrix.get(0).get(i));
                    explanation.append(", ");
                }
                explanation.append(constants.get(0));
                explanation.append("\nW analogiczny sposób modyfikowane są wszystkie kolejne wiersze macierzy.\n");
            }
        }

        return matrix;
    }

}