package method;

import dto.ResultDataDto;
import dto.InputDataDto;
import method.impl.AnalyticalMethod;
import method.impl.CrankNicolsonScheme;
import method.impl.ImplicitDifferenceScheme;

import java.sql.Time;
import java.time.LocalTime;

public class Calculator {
    private static final int i = 0;

    public static ResultDataDto doCalculation(InputDataDto inputDataDto) {

        Method analyticalMethod = new AnalyticalMethod();
        Method crankNicolsonScheme = new CrankNicolsonScheme();
        Method implicitDifferenceScheme = new ImplicitDifferenceScheme();
        return ResultDataDto.builder()
                .idCalculationResult(i)
                .name("Calc№ " + i)
                .date(Time.valueOf(LocalTime.now()))
                .length(inputDataDto.getL())
                .K(inputDataDto.getK())
                .J(inputDataDto.getJ())
                .λ(inputDataDto.getΛ())
                .nRefraction(inputDataDto.getNRefraction())
                .radius(inputDataDto.getR())
                .z(inputDataDto.getZ())
                .analyticalSolution(analyticalMethod.doCalculation(inputDataDto))
                .crankNicolsonSchemeSolution(crankNicolsonScheme.doCalculation(inputDataDto))
                .implicitSchemeSolution(implicitDifferenceScheme.doCalculation(inputDataDto))
                .build();
    }
}
