package method;

import dto.CalculationResultDto;
import dto.InputDataDto;
import method.impl.AnalyticalMethod;
import method.impl.CrankNicholsonScheme;
import method.impl.ImplicitDifferenceScheme;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;

public class MethodHelper {
    private static int i = 0;

    public static CalculationResultDto doCalculation(InputDataDto inputDataDto) {

        Method analyticalMethod = new AnalyticalMethod();
        Method crankNicholsonScheme = new CrankNicholsonScheme();
        Method implicitDifferenceScheme = new ImplicitDifferenceScheme();
        return CalculationResultDto.builder()
                .idCalculationResult(i)
                .name("Calc№ " + i)
                .date(Time.valueOf(LocalTime.now()))
                .length(inputDataDto.getLength())
                .lambda(inputDataDto.getLambda())
                .nRefraction(inputDataDto.getNRefraction())
                .radius(inputDataDto.getRadius())
                .numberPoint(inputDataDto.getNumberPoint())
                .numberOfMembers(inputDataDto.getNumberOfMembers())
                .analyticalSolution(analyticalMethod.makeCalculation(inputDataDto))
                .solutionByTheCrankNicholsonScheme(crankNicholsonScheme.makeCalculation(inputDataDto))
                .implicitSchemaSolution(implicitDifferenceScheme.makeCalculation(inputDataDto))
                .build();
    }
}
