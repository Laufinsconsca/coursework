package method.impl;

import complex.Complex;
import dto.InputDataDto;
import matrices.matrix.Matrix;
import method.Method;
import tabulatedFunctions.TabulatedFunction;

public class CrankNicolsonScheme extends BaseMethod implements Method {
    @Override
    public TabulatedFunction doCalculation(InputDataDto inputDataDto) {
        init(inputDataDto);
        double h_r = R / J;
        double h_z = L / K;
        Complex η = new Complex(0, h_z * λ / (4 * Math.PI * n * h_r * h_r));
        Matrix<Complex> A = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> B = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> C = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> α = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> β = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> F = new Matrix<>(1, J, Complex.class);
        Matrix<Complex> U = new Matrix<>(J + 1, K + 1, Complex.class);
        A.set(0, 1, 1);
        B.set(η.scale(-2).plus(1), 1, 1);
        C.set(η.scale(2), 1, 1);
        for (int j = 1; j < J; j++) {
            A.set(η.scale((1 - 1. / (2 * j)) / 2), 1, j + 1);
            B.set(η.negate().plus(1), 1, j + 1);
            C.set(η.scale((1 + 1. / (2 * j)) / 2), 1, j + 1);
        }
        C.set(0, 1, J);
        for (int j = 0; j <= J; j++) {
            U.set(ψ(j * h_r, R), j + 1, 1);
        }
        for (int k = 1; k <= K; k++) {
            F.set(U.get(2, k).multiply(A.get(1, 1)).negate()
                    .add(U.get(1, k).multiply(B.get(1, 1).negate().add(2)))
                    .add(U.get(2, k).multiply(C.get(1, 1)).negate()), 1, 1);
            for (int j = 1; j < J; j++) {
                F.set((U.get(j, k).multiply(A.get(1, j + 1)).negate())
                        .add(U.get(j + 1, k).multiply(B.get(1, j + 1).negate().add(2)))
                        .add(U.get(j + 2, k).multiply(C.get(1, j + 1)).negate()), 1, j + 1);
            }
            α.set(C.get(1, 1).divide(B.get(1, 1)).negate(), 1, 1);
            β.set(F.get(1, 1).divide(B.get(1, 1)), 1, 1);
            for (int j = 1; j < J; j++) {
                α.set(C.get(1, j + 1)
                        .divide(B.get(1, j + 1).add(A.get(1, j + 1)
                                .multiply(α.get(1, j)))).negate(), 1, j + 1);
                β.set((F.get(1, j + 1)
                        .subtract(A.get(1, j + 1).multiply(β.get(1, j))))
                        .divide(B.get(1, j + 1).add(A.get(1, j + 1)
                                .multiply(α.get(1, j)))), 1, j + 1);
            }
            U.set(β.get(1, J), J, k + 1);
            for (int j = J - 2; j >= 0; j--) {
                U.set(α.get(1, j + 1).multiply(U.get(j + 2, k + 1)).add(β.get(1, j + 1)), j + 1, k + 1);
            }
        }
        TabulatedFunction crankNicolsonSchemeSolution = getTabulatedFunction(U, Math.round((float) (z * K / L)));
        crankNicolsonSchemeSolution.setName("схема\nКранка-Николсона");
        return crankNicolsonSchemeSolution;
    }
}
