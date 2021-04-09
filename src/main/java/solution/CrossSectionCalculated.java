package solution;


import dto.InputDataDto;
import model.tabulatedFunctions.TabulatedFunction;

import java.util.List;

public interface CrossSectionCalculated {
    List<TabulatedFunction> crossSectionCalculate(InputDataDto inputDataDto);
}
