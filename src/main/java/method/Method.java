package method;


import dto.InputDataDto;
import tabulatedFunctions.TabulatedFunction;

public interface Method {
    TabulatedFunction doCalculation(InputDataDto inputDataDto);
}
