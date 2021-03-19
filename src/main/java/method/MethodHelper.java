package method;

import dto.CalculationResultDto;
import dto.InputDataDto;
import method.impl.AnalyticalMethod;
import method.impl.CrankNicolsonScheme;
import method.impl.ImplicitDifferenceScheme;

import java.sql.Time;
import java.time.LocalTime;

public class MethodHelper {
    private static final int i = 0;

    public static CalculationResultDto doCalculation(InputDataDto inputDataDto) {

        Method analyticalMethod = new AnalyticalMethod();
        Method crankNicolsonScheme = new CrankNicolsonScheme();
        Method implicitDifferenceScheme = new ImplicitDifferenceScheme();
        return CalculationResultDto.builder()
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
                .solutionByTheCrankNicholsonScheme(crankNicolsonScheme.doCalculation(inputDataDto))
                .implicitSchemaSolution(implicitDifferenceScheme.doCalculation(inputDataDto))
                .build();
    }
}
