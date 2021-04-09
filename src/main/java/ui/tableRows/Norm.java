package ui.tableRows;

import model.complex.Complex;
import model.matrix.ComplexMatrix;

import java.util.function.BiFunction;

public class Norm {
    private double R;
    private double L;

    public Norm(double r, double l) {
        R = r;
        L = l;
    }

    public double maxEps(ComplexMatrix schemeSolution, BiFunction<Double, Double, Complex> analyticalSolution) {
        double max = 0;
        double h_r = R / (schemeSolution.getCountRows() - 1);
        double h_z = L / (schemeSolution.getCountColumns() - 1);
        for (int j = 0; j < schemeSolution.getCountRows(); j++) {
            for (int k = 0; k < schemeSolution.getCountColumns(); k++) {
                double temp = schemeSolution.get(j + 1, k + 1).subtract(analyticalSolution.apply(j * h_r, k * h_z)).abs();
                if (temp > max) {
                    max = temp;
                }
            }
        }
        return max;
    }
}
