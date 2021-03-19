package method.impl;

import dto.InputDataDto;
import method.Method;
import tabulatedFunctions.ArrayTabulatedFunction;
import tabulatedFunctions.TabulatedFunction;

public class ImplicitDifferenceScheme extends BaseMethod implements Method {
    @Override
    public TabulatedFunction doCalculation(InputDataDto inputDataDto) {
        init(inputDataDto);
        //calculation
        return ArrayTabulatedFunction.getIdentity();
    }
}
