package method;

import dto.ComprehensiveResultDataDto;
import dto.CrossSectionResultDataDto;
import dto.InputDataDto;
import method.impl.AnalyticalMethod;
import method.impl.CrankNicolsonScheme;
import method.impl.ImplicitDifferenceScheme;

import java.sql.Time;
import java.time.LocalTime;

public class Calculator {
    private static final int i = 0;

    public static CrossSectionResultDataDto crossSectionCalculate(InputDataDto inputDataDto) {

        CrossSectionCalculated analyticalMethod = new AnalyticalMethod();
        CrossSectionCalculated crankNicolsonScheme = new CrankNicolsonScheme();
        CrossSectionCalculated implicitDifferenceScheme = new ImplicitDifferenceScheme();
        return CrossSectionResultDataDto.builder()
                .idCalculationResult(i)
                .name("Calc№ " + i)
                .date(Time.valueOf(LocalTime.now()))
                .L(inputDataDto.getL())
                .K(inputDataDto.getK())
                .J(inputDataDto.getJ())
                .λ(inputDataDto.getΛ())
                .nRefraction(inputDataDto.getNRefraction())
                .R(inputDataDto.getR())
                .z(inputDataDto.getZ())
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
                .idCalculationResult(i)
                .name("Calc№ " + i)
                .date(Time.valueOf(LocalTime.now()))
                .L(inputDataDto.getL())
                .K(inputDataDto.getK())
                .J(inputDataDto.getJ())
                .λ(inputDataDto.getΛ())
                .nRefraction(inputDataDto.getNRefraction())
                .R(inputDataDto.getR())
                .analyticalSolution(analyticalMethod.calculate(inputDataDto))
                .crankNicolsonSchemeSolution(crankNicolsonScheme.comprehensiveCalculate(inputDataDto))
                .implicitSchemeSolution(implicitDifferenceScheme.comprehensiveCalculate(inputDataDto))
                .build();
    }
}
