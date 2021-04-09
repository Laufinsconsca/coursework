package solution;

import dto.InputDataDto;
import model.complex.Complex;

import java.util.function.BiFunction;

public interface ContinuousFunction {
    BiFunction<Double, Double, Complex> calculate(InputDataDto inputDataDto);
}
