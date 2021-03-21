package model;

import complex.Complex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import matrices.matrix.Matrix;

import java.util.Date;
import java.util.function.BiFunction;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ComprehensiveResultData {

    private Integer idCalculationResult;
    private String name;
    private Date date;
    private Integer J;
    private Integer K;
    private Integer nEigenfunction;
    private double λ;
    private double nRefraction;
    private double L;
    private double R;
    private BiFunction<Double, Double, Complex> analyticalSolution;
    private Matrix<Complex> implicitSchemeSolution;
    private Matrix<Complex> crankNicolsonSchemeSolution;
}

