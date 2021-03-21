package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tabulatedFunctions.TabulatedFunction;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CrossSectionResultData {

    private Integer idCalculationResult;
    private String name;
    private Date date;
    private Integer J;
    private Integer K;
    private double λ;
    private double nRefraction;
    private double L;
    private double R;
    private double z;
    private TabulatedFunction analyticalSolution;
    private TabulatedFunction implicitSchemaSolution;
    private TabulatedFunction crankNicolsonSchemeSolution;
}
