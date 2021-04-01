package solution.methods;

import complex.Complex;
import dto.InputDataDto;
import enums.FixedVariableType;
import matrices.matrix.Matrix;
import solution.ComprehensiveCalculated;
import tabulatedFunctions.TabulatedFunction;

import java.util.List;

public class ImplicitDifferenceScheme extends BaseMethod implements ComprehensiveCalculated {

    @Override
    public Matrix<Complex> comprehensiveCalculate(InputDataDto inputDataDto) {
        init(inputDataDto);
        A.set(0, 1, 1);
        B.set(α.scaleOn(-4).add(1), 1, 1);
        C.set(α.scaleOn(4), 1, 1);
        for (int j = 1; j < J; j++) {
            A.set(α.scaleOn(1 - 1. / (2 * j)), 1, j + 1);
            B.set(α.scaleOn(-2).add(1), 1, j + 1);
            C.set(α.scaleOn(1 + 1. / (2 * j)), 1, j + 1);
        }
        C.set(0, 1, J);
        for (int j = 0; j <= J; j++) {
            U.set(ψ(j * R / J, R), j + 1, 1);
        }
        for (int k = 1; k <= K; k++) {
            p.set(C.get(1, 1).divide(B.get(1, 1)).negate(), 1, 1);
            q.set(U.get(1, k).divide(B.get(1, 1)), 1, 1);
            for (int j = 1; j < J; j++) {
                p.set(C.get(1, j + 1)
                        .divide(B.get(1, j + 1).add(A.get(1, j + 1)
                                .multiply(p.get(1, j)))).negate(), 1, j + 1);
                q.set((U.get(j + 1, k)
                        .subtract(A.get(1, j + 1).multiply(q.get(1, j))))
                        .divide(B.get(1, j + 1).add(A.get(1, j + 1)
                                .multiply(p.get(1, j)))), 1, j + 1);
            }
            U.set(q.get(1, J), J, k + 1);
            for (int j = J - 2; j >= 0; j--) {
                U.set(p.get(1, j + 1).multiply(U.get(j + 2, k + 1)).add(q.get(1, j + 1)), j + 1, k + 1);
            }
        }
        return U;
    }

    @Override
    public List<TabulatedFunction> crossSectionCalculate(InputDataDto inputDataDto) {
        List<TabulatedFunction> implicitSchemeSolutionArray = getTabulatedFunction(comprehensiveCalculate(inputDataDto));
        int i = 0;
        for (TabulatedFunction function : implicitSchemeSolutionArray) {
            if (fixedVariableType.equals(FixedVariableType.r)) {
                function.setName("неявная\nсхема,\nr = " + fixedVariable[i]);
            } else {
                function.setName("неявная\nсхема,\nz = " + fixedVariable[i]);
            }
            i++;
        }
        return implicitSchemeSolutionArray;
    }
}
