package solution;

import dto.InputDataDto;
import model.tabulatedFunction.TabulatedFunction;

import java.util.List;

public interface CrossSectionCalculated {
    List<TabulatedFunction> crossSectionCalculate(InputDataDto inputDataDto);
}
