package dto;

import lombok.*;
import tabulatedFunctions.TabulatedFunction;

import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResultDataDto {

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
    private TabulatedFunction analyticalSolution;
    private TabulatedFunction implicitSchemeSolution;
    private TabulatedFunction crankNicolsonSchemeSolution;
}
