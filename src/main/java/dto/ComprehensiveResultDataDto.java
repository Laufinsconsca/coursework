package dto;

import complex.Complex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import matrix.ComplexMatrix;

import java.util.function.BiFunction;

@AllArgsConstructor
@Getter
@Builder
public class ComprehensiveResultDataDto {

    //    private Integer idCalculationResult;
//    private String name;
//    private Time date;
//    private Integer J;
//    private Integer K;
//    private Integer nEigenfunction;
//    private double λ;
//    private double nRefraction;
//    private double L;
//    private double R;
    private final BiFunction<Double, Double, Complex> analyticalSolution;
    private final ComplexMatrix implicitSchemeSolution;
    private final ComplexMatrix crankNicolsonSchemeSolution;
}
