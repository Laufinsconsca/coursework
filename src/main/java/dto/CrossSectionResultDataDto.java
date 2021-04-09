package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import model.tabulatedFunctions.TabulatedFunction;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class CrossSectionResultDataDto {

    private final List<TabulatedFunction> analyticalSolution;
    private final List<TabulatedFunction> implicitSchemeSolution;
    private final List<TabulatedFunction> crankNicolsonSchemeSolution;
}
