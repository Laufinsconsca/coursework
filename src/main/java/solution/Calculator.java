package solution;

import dto.ComprehensiveResultDataDto;
import dto.CrossSectionResultDataDto;
import dto.InputDataDto;
import solution.methods.AnalyticalMethod;
import solution.methods.CrankNicolsonScheme;
import solution.methods.ImplicitDifferenceScheme;

public class Calculator {
    public static CrossSectionResultDataDto crossSectionCalculate(InputDataDto inputDataDto) {

        CrossSectionCalculated analyticalMethod = new AnalyticalMethod();
        CrossSectionCalculated crankNicolsonScheme = new CrankNicolsonScheme();
        CrossSectionCalculated implicitDifferenceScheme = new ImplicitDifferenceScheme();
        return CrossSectionResultDataDto.builder()
                .analyticalSolution(analyticalMethod.crossSectionCalculate(inputDataDto))
                .crankNicolsonSchemeSolution(crankNicolsonScheme.crossSectionCalculate(inputDataDto))
                .implicitSchemeSolution(implicitDifferenceScheme.crossSectionCalculate(inputDataDto))
                .build();
    }

    public static ComprehensiveResultDataDto calculate(InputDataDto inputDataDto) {

        ContinuousFunction analyticalMethod = new AnalyticalMethod();
        ComprehensiveCalculated crankNicolsonScheme = new CrankNicolsonScheme();
        ComprehensiveCalculated implicitDifferenceScheme = new ImplicitDifferenceScheme();
        return ComprehensiveResultDataDto.builder()
                .analyticalSolution(analyticalMethod.calculate(inputDataDto))
                .crankNicolsonSchemeSolution(crankNicolsonScheme.comprehensiveCalculate(inputDataDto))
                .implicitSchemeSolution(implicitDifferenceScheme.comprehensiveCalculate(inputDataDto))
                .build();
    }
}
