package service.impl;


import dto.InputDataDto;
import method.Method;
import method.impl.AnalyticalMethod;
import method.impl.CrankNicolsonScheme;
import method.impl.ImplicitDifferenceScheme;
import model.CalculationResult;
import service.PlotService;

public class PlotServiceImpl implements PlotService {

    @Override
    public String plot(InputDataDto inputDataDto) {
        try {

            Method analyticalMethod = new AnalyticalMethod();
            Method crankNicolsonScheme = new CrankNicolsonScheme();
            Method implicitDifferenceScheme = new ImplicitDifferenceScheme();

            CalculationResult calculationResult = CalculationResult.builder()
                    .name(inputDataDto.getName())
                    .date(null)//Todo add current Time
                    .λ(inputDataDto.getK())
                    .L(inputDataDto.getL())
                    .R(inputDataDto.getR())
                    .nRefraction(inputDataDto.getNRefraction())
                    .analyticalSolution(analyticalMethod.doCalculation(inputDataDto))
                    .implicitSchemaSolution(implicitDifferenceScheme.doCalculation(inputDataDto))
                    .solutionByTheCrankNicholsonScheme(crankNicolsonScheme.doCalculation(inputDataDto))
                    .build();


            return "result";
        } catch (Exception e) {
            return "error";
        }
    }
}
