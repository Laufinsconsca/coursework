package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import tabulatedFunctions.TabulatedFunction;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class CrossSectionResultDataDto {

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
//    private double[] fixedVariable;
//    private FixedVariableType fixedVariableType;
    private final List<TabulatedFunction> analyticalSolution;
    private final List<TabulatedFunction> implicitSchemeSolution;
    private final List<TabulatedFunction> crankNicolsonSchemeSolution;
}
