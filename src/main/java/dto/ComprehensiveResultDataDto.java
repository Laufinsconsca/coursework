package dto;

import complex.Complex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import matrices.matrix.Matrix;

import java.sql.Time;
import java.util.function.BiFunction;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ComprehensiveResultDataDto {
    private Integer idCalculationResult;
    private String name;
    private Time date;
    private Integer J;
    private Integer K;
    private double λ;
    private double nRefraction;
    private double length;
    private double radius;
    private double z;
    private BiFunction<Double, Double, Complex> analyticalSolution;
    private Matrix<Complex> implicitSchemeSolution;
    private Matrix<Complex> crankNicolsonSchemeSolution;
}
