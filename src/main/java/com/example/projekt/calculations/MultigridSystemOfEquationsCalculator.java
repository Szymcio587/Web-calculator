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
@AllArgsConstructor
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

        List<Double> solution = new ArrayList<>(data.getCoefficients().size());
        for (int i = 0; i < data.getCoefficients().size(); i++) {
            solution.add(0.0);
        }

        SystemOfEquationsData cp = data;
        data.setCoefficients(MakeDiagonallyDominant(data.getCoefficients(), data.getConstants()));

        solution = MultigridSolve(data.getCoefficients(), data.getConstants(), solution);

        result.setSolutions(UtilityService.Round(solution, 5));
        data.setCoefficients(cp.getCoefficients());
        data.setConstants(cp.getConstants());
    }

    private List<Double> MultigridSolve(List<List<Double>> A, List<Double> b, List<Double> x) {
        for (int iteration = 0; iteration < LOOP_ITERATIONS; iteration++) {
            x = Smooth(A, b, x);

            List<Double> residual = CalculateResidual(A, b, x);

            double residualNorm = CalculateNorm(residual);
            if (residualNorm < TOLERANCE) {
                return x;
            }

            List<Double> coarseResidual = RestrictResidual(residual);

            List<Double> coarseError = SolveCoarseGrid(coarseResidual);

            List<Double> fineError = ProlongateError(coarseError, A.size());

            for (int i = 0; i < A.size(); i++) {
                x.set(i, x.get(i) + fineError.get(i));
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

    private List<List<Double>> MakeDiagonallyDominant(List<List<Double>> matrix, List<Double> constants) {
        for (int q = 0; q < matrix.size(); q++) {
            int maxIndex = q;

            for (int w = 0; w < matrix.size(); w++) {
                if (Math.abs(matrix.get(w).get(q)) > Math.abs(matrix.get(maxIndex).get(q))) {
                    maxIndex = w;
                }
            }

            if (maxIndex != q) {
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
            }
        }

        return matrix;
    }

}