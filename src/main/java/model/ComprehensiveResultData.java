package model;

import lombok.*;
import tabulatedFunctions.TabulatedFunction;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Data
public class ComprehensiveResultData {

    private Integer idCalculationResult;
    private String name;
    private Date date;
    private Integer J;
    private Integer K;
    private double λ;
    private double nRefraction;
    private double L;
    private double R;
    private TabulatedFunction analyticalSolution;
    private TabulatedFunction implicitSchemaSolution;
    private TabulatedFunction solutionByTheCrankNicholsonScheme;
}
