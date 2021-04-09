package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import model.complex.Complex;
import model.matrix.ComplexMatrix;

import java.util.function.BiFunction;

@AllArgsConstructor
@Getter
@Builder
public class ComprehensiveResultDataDto {

    private final BiFunction<Double, Double, Complex> analyticalSolution;
    private final ComplexMatrix implicitSchemeSolution;
    private final ComplexMatrix crankNicolsonSchemeSolution;
}
