package dto;

import lombok.*;
import tabulatedFunctions.TabulatedFunction;

import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Data
public class CalculationResultDto {

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
    private TabulatedFunction implicitSchemaSolution;
    private TabulatedFunction solutionByTheCrankNicholsonScheme;
}
