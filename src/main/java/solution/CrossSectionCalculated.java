package solution;


import dto.InputDataDto;
import tabulatedFunctions.TabulatedFunction;

import java.util.List;

public interface CrossSectionCalculated {
    List<TabulatedFunction> crossSectionCalculate(InputDataDto inputDataDto);
}
