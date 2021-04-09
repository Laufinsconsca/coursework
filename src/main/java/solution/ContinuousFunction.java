package solution;

import model.complex.Complex;
import dto.InputDataDto;

import java.util.function.BiFunction;

public interface ContinuousFunction {
    BiFunction<Double, Double, Complex> calculate(InputDataDto inputDataDto);
}
