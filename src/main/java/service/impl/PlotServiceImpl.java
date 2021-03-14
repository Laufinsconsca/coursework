package service.impl;


import dto.InputDataDto;
import method.AnalyticalMethod;
import method.CrankNicholsonScheme;
import method.ImplicitDifferenceScheme;
import method.Method;
import model.CalculationResult;
import service.PlotService;

public class PlotServiceImpl implements PlotService {

    @Override
    public String plot(InputDataDto inputDataDto) {
        try {

            Method analyticalMethod = new AnalyticalMethod();
            Method crankNicholsonScheme = new CrankNicholsonScheme();
            Method implicitDifferenceScheme = new ImplicitDifferenceScheme();

            CalculationResult calculationResult = CalculationResult.builder()
                    .name(inputDataDto.getName())
                    .date(null)//Todo add current Time
                    .lambda(inputDataDto.getLambda())
                    .length(inputDataDto.getLength())
                    .radius(inputDataDto.getRadius())
                    .nRefraction(inputDataDto.getNRefraction())
                    .numberPoint(inputDataDto.getNumberPoint())
                    .numberOfMembers(inputDataDto.getNumberOfMembers())
                    .analyticalSolution(analyticalMethod.makeCalculation(inputDataDto))
                    .implicitSchemaSolution(implicitDifferenceScheme.makeCalculation(inputDataDto))
                    .solutionByTheCrankNicholsonScheme(crankNicholsonScheme.makeCalculation(inputDataDto))
                    .build();


            return "result";
        } catch (Exception e) {
            return "error";
        }
    }
}
