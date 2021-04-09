package ui.tableRows;

import dto.ComprehensiveResultDataDto;
import dto.InputDataDto;
import javafx.collections.FXCollections;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Point;
import model.complex.Complex;
import model.tabulatedFunctions.ArrayTabulatedFunction;
import model.tabulatedFunctions.TabulatedFunction;
import solution.Calculator;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CrankNicolsonSchemeEpsTableRow {
    private Integer J;
    private Integer K;
    private double ε_1_2h_r_1_2h_z;
    private double ε_h_r_h_z;
    private double δ_h_r_h_z;

    public static CrankNicolsonSchemeEpsTableRow populateCrankNicolsonSchemeEpsTableRow(InputDataDto inputDataDto) {
        Norm norm = new Norm(inputDataDto.getR(), inputDataDto.getL());
        CrankNicolsonSchemeEpsTableRow crankNicolsonSchemeEpsTableRow = new CrankNicolsonSchemeEpsTableRow();
        crankNicolsonSchemeEpsTableRow.setJ(inputDataDto.getJ());
        crankNicolsonSchemeEpsTableRow.setK(inputDataDto.getK());
        ComprehensiveResultDataDto comprehensiveResultDataDto = Calculator.calculate(inputDataDto);
        BiFunction<Double, Double, Complex> analyticalSolution = comprehensiveResultDataDto.getAnalyticalSolution();
        crankNicolsonSchemeEpsTableRow.setΕ_h_r_h_z(norm.maxEps(comprehensiveResultDataDto.getCrankNicolsonSchemeSolution(), comprehensiveResultDataDto.getAnalyticalSolution()));
        comprehensiveResultDataDto = Calculator.calculate(inputDataDto.toBuilder().K(inputDataDto.getK() * 2).J(inputDataDto.getJ() * 2).build());
        crankNicolsonSchemeEpsTableRow.setΕ_1_2h_r_1_2h_z(norm.maxEps(comprehensiveResultDataDto.getCrankNicolsonSchemeSolution(), analyticalSolution));
        crankNicolsonSchemeEpsTableRow.setΔ_h_r_h_z(crankNicolsonSchemeEpsTableRow.getΕ_h_r_h_z() / crankNicolsonSchemeEpsTableRow.getΕ_1_2h_r_1_2h_z());
        return crankNicolsonSchemeEpsTableRow;
    }

    public static TabulatedFunction getCrankNicolsonSchemeFunction(List<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableRows) {
        List<Point> crankNicolsonSchemePoints = crankNicolsonSchemeEpsTableRows.stream()
                .map(crankNicolsonSchemeEpsTableRow ->
                        new Point(crankNicolsonSchemeEpsTableRow.getJ(),
                                crankNicolsonSchemeEpsTableRow.getK(),
                                new Complex(crankNicolsonSchemeEpsTableRow.getΔ_h_r_h_z(), 0)))
                .collect(Collectors.toList());
        return new ArrayTabulatedFunction(FXCollections.observableList(crankNicolsonSchemePoints), 0);
    }
}