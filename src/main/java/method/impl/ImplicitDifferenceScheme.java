package method.impl;

import complex.Complex;
import dto.InputDataDto;
import matrices.matrix.Matrix;
import method.ComprehensiveCalculated;
import tabulatedFunctions.TabulatedFunction;

public class ImplicitDifferenceScheme extends BaseMethod implements ComprehensiveCalculated {

    @Override
    public Matrix<Complex> comprehensiveCalculate(InputDataDto inputDataDto) {
        init(inputDataDto);
        double h_r = R / J;
        double h_z = L / K;
        Complex η = new Complex(0, h_z * λ / (4 * Math.PI * n * h_r * h_r));
        Matrix<Complex> A = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> B = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> C = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> α = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> β = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> U = new Matrix<>(J + 1, K + 1, Complex.class);
        A.set(0, 1, 1);
        B.set(η.scaleOn(-4).add(1), 1, 1);
        C.set(η.scaleOn(4), 1, 1);
        for (int j = 1; j < J; j++) {
            A.set(η.scaleOn(1 - 1. / (2 * j)), 1, j + 1);
            B.set(η.scaleOn(-2).add(1), 1, j + 1);
            C.set(η.scaleOn(1 + 1. / (2 * j)), 1, j + 1);
        }
        C.set(0, 1, J);
        for (int j = 0; j <= J; j++) {
            U.set(ψ(j * h_r, R), j + 1, 1);
        }
        for (int k = 1; k <= K; k++) {
            α.set(C.get(1, 1).divide(B.get(1, 1)).negate(), 1, 1);
            β.set(U.get(1, k).divide(B.get(1, 1)), 1, 1);
            for (int j = 1; j < J; j++) {
                α.set(C.get(1, j + 1)
                        .divide(B.get(1, j + 1).add(A.get(1, j + 1)
                                .multiply(α.get(1, j)))).negate(), 1, j + 1);
                β.set((U.get(j + 1, k)
                        .subtract(A.get(1, j + 1).multiply(β.get(1, j))))
                        .divide(B.get(1, j + 1).add(A.get(1, j + 1)
                                .multiply(α.get(1, j)))), 1, j + 1);
            }
            U.set(β.get(1, J), J, k + 1);
            for (int j = J - 2; j >= 0; j--) {
                U.set(α.get(1, j + 1).multiply(U.get(j + 2, k + 1)).add(β.get(1, j + 1)), j + 1, k + 1);
            }
        }
        return U;
    }

    @Override
    public TabulatedFunction crossSectionCalculate(InputDataDto inputDataDto) {
        TabulatedFunction implicitSchemeSolution = getTabulatedFunction(comprehensiveCalculate(inputDataDto), Math.round((float) (z * K / L)));
        implicitSchemeSolution.setName("неявная\nсхема");
        return implicitSchemeSolution;
    }
}
