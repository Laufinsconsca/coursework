package method;


import dto.InputDataDto;
import tabulatedFunctions.TabulatedFunction;

public interface CrossSectionCalculated {
    TabulatedFunction crossSectionCalculate(InputDataDto inputDataDto);
}
