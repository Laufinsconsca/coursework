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
public class ImplicitSchemeEpsTableRow {
    private Integer J;
    private Integer K;
    private double ε_1_2h_r_1_4h_z;
    private double ε_h_r_h_z;
    private double δ_h_r_h_z;

    public static ImplicitSchemeEpsTableRow populateImplicitSchemeEpsTableRow(InputDataDto inputDataDto) {
        Norm norm = new Norm(inputDataDto.getR(), inputDataDto.getL());
        ImplicitSchemeEpsTableRow implicitSchemeEpsTableRow = new ImplicitSchemeEpsTableRow();
        implicitSchemeEpsTableRow.setJ(inputDataDto.getJ());
        implicitSchemeEpsTableRow.setK(inputDataDto.getK());
        ComprehensiveResultDataDto comprehensiveResultDataDto = Calculator.calculate(inputDataDto);
        BiFunction<Double, Double, Complex> analyticalSolution = comprehensiveResultDataDto.getAnalyticalSolution();
        implicitSchemeEpsTableRow.setΕ_h_r_h_z(norm.maxEps(comprehensiveResultDataDto.getImplicitSchemeSolution(), comprehensiveResultDataDto.getAnalyticalSolution()));
        comprehensiveResultDataDto = Calculator.calculate(inputDataDto.toBuilder().K(inputDataDto.getK() * 4).J(inputDataDto.getJ() * 2).build());
        implicitSchemeEpsTableRow.setΕ_1_2h_r_1_4h_z(norm.maxEps(comprehensiveResultDataDto.getImplicitSchemeSolution(), analyticalSolution));
        implicitSchemeEpsTableRow.setΔ_h_r_h_z(implicitSchemeEpsTableRow.getΕ_h_r_h_z() / implicitSchemeEpsTableRow.getΕ_1_2h_r_1_4h_z());
        return implicitSchemeEpsTableRow;
    }

    public static TabulatedFunction getImplicitSchemeFunction(List<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableRows) {
        List<Point> implicitSchemePoints = implicitSchemeEpsTableRows.stream()
                .map(implicitSchemeEpsTableRow ->
                        new Point(implicitSchemeEpsTableRow.getJ(),
                                implicitSchemeEpsTableRow.getK(),
                                new Complex(implicitSchemeEpsTableRow.getΔ_h_r_h_z(), 0)))
                .collect(Collectors.toList());
        return new ArrayTabulatedFunction(FXCollections.observableList(implicitSchemePoints), 0);
    }
}