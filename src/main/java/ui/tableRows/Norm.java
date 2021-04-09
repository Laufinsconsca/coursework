package ui.tableRows;

import model.complex.Complex;
import model.matrix.ComplexMatrix;

import java.util.function.BiFunction;

public class Norm {
    private final double R;
    private final double L;

    public Norm(double r, double l) {
        R = r;
        L = l;
    }

    public double maxEps(ComplexMatrix schemeSolution, BiFunction<Double, Double, Complex> analyticalSolution) {
        double max = 0;
        double h_r = R / (schemeSolution.getRowDimension() - 1);
        double h_z = L / (schemeSolution.getColumnDimension() - 1);
        for (int j = 0; j < schemeSolution.getRowDimension(); j++) {
            for (int k = 0; k < schemeSolution.getColumnDimension(); k++) {
                double temp = schemeSolution.get(j + 1, k + 1).subtract(analyticalSolution.apply(j * h_r, k * h_z)).abs();
                if (temp > max) {
                    max = temp;
                }
            }
        }
        return max;
    }
}
