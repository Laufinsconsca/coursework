package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tabulatedFunctions.TabulatedFunction;

import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CrossSectionResultDataDto {

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
