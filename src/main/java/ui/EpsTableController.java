package ui;

import complex.Complex;
import dto.ComprehensiveResultDataDto;
import dto.InputDataDto;
import epsTable.CrankNicolsonSchemeEpsTableRow;
import epsTable.ImplicitSchemeEpsTableRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;
import matrices.matrix.Matrix;
import method.Calculator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

@AutoInitializableController(name = "Таблица погрешностей", type = Item.CONTROLLER, pathFXML = "epsTable.fxml")
public class EpsTableController implements Initializable, aWindow, InputDataDtoHolder {
    private ObservableList<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableObservableRows;
    private ObservableList<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableObservableRows;
    public TableView<ImplicitSchemeEpsTableRow> implicitSchemeTableView;
    public TableView<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeTableView;
    public TableColumn<ImplicitSchemeEpsTableRow, Integer> implicitSchemeTableColumnJ;
    public TableColumn<ImplicitSchemeEpsTableRow, Integer> implicitSchemeTableColumnK;
    public TableColumn<ImplicitSchemeEpsTableRow, Double> implicitSchemeTableColumnΕ_2h_r_4h_z;
    public TableColumn<ImplicitSchemeEpsTableRow, Double> implicitSchemeTableColumnΕ_h_r_h_z;
    public TableColumn<ImplicitSchemeEpsTableRow, Double> implicitSchemeTableColumnΔ_h_r_h_z;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Integer> crankNicolsonSchemeTableColumnJ;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Integer> crankNicolsonSchemeTableColumnK;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Double> crankNicolsonSchemeTableColumnΕ_2h_r_2h_z;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Double> crankNicolsonSchemeTableColumnΕ_h_r_h_z;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Double> crankNicolsonSchemeTableColumnΔ_h_r_h_z;
    private Stage stage;
    private InputDataDto inputDataDto;
    private double R, L;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        implicitSchemeTableColumnJ.setCellValueFactory(new PropertyValueFactory<>("J"));
        implicitSchemeTableColumnK.setCellValueFactory(new PropertyValueFactory<>("K"));
        implicitSchemeTableColumnΕ_2h_r_4h_z.setCellValueFactory(new PropertyValueFactory<>("ε_2h_r_4h_z"));
        implicitSchemeTableColumnΕ_h_r_h_z.setCellValueFactory(new PropertyValueFactory<>("ε_h_r_h_z"));
        implicitSchemeTableColumnΔ_h_r_h_z.setCellValueFactory(new PropertyValueFactory<>("δ_h_r_h_z"));
        crankNicolsonSchemeTableColumnJ.setCellValueFactory(new PropertyValueFactory<>("J"));
        crankNicolsonSchemeTableColumnK.setCellValueFactory(new PropertyValueFactory<>("K"));
        crankNicolsonSchemeTableColumnΕ_2h_r_2h_z.setCellValueFactory(new PropertyValueFactory<>("ε_2h_r_2h_z"));
        crankNicolsonSchemeTableColumnΕ_h_r_h_z.setCellValueFactory(new PropertyValueFactory<>("ε_h_r_h_z"));
        crankNicolsonSchemeTableColumnΔ_h_r_h_z.setCellValueFactory(new PropertyValueFactory<>("δ_h_r_h_z"));
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        stage.setResizable(false);
        stage.setOnShown(windowEvent -> {
            R = inputDataDto.getR();
            L = inputDataDto.getL();
            List<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableRows = new ArrayList<>();
            List<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableRows = new ArrayList<>();
            for (int i = 1; i < Math.pow(2,12); i*=2) {
                Pair<ImplicitSchemeEpsTableRow, CrankNicolsonSchemeEpsTableRow> rows = populateRow(inputDataDto.toBuilder().J(2*i).K(2*i).build());
                implicitSchemeEpsTableRows.add(rows.getKey());
                crankNicolsonSchemeEpsTableRows.add(rows.getValue());
            }
            implicitSchemeEpsTableObservableRows = FXCollections.observableList(implicitSchemeEpsTableRows);
            crankNicolsonSchemeEpsTableObservableRows = FXCollections.observableList(crankNicolsonSchemeEpsTableRows);
            implicitSchemeTableView.setItems(implicitSchemeEpsTableObservableRows);
            crankNicolsonSchemeTableView.setItems(crankNicolsonSchemeEpsTableObservableRows);
        });
        this.stage = stage;
    }

    @Override
    public InputDataDto getInputDataDto() {
        return inputDataDto;
    }

    @Override
    public void setInputDataDto(InputDataDto inputDataDto) {
        this.inputDataDto = inputDataDto;
    }

    private Pair<ImplicitSchemeEpsTableRow, CrankNicolsonSchemeEpsTableRow> populateRow(InputDataDto inputDataDto) {
        ImplicitSchemeEpsTableRow implicitSchemeEpsTableRow = new ImplicitSchemeEpsTableRow();
        CrankNicolsonSchemeEpsTableRow crankNicolsonSchemeEpsTableRow = new CrankNicolsonSchemeEpsTableRow();
        implicitSchemeEpsTableRow.setJ(inputDataDto.getJ());
        implicitSchemeEpsTableRow.setK(inputDataDto.getK());
        crankNicolsonSchemeEpsTableRow.setJ(inputDataDto.getJ());
        crankNicolsonSchemeEpsTableRow.setK(inputDataDto.getK());

        ComprehensiveResultDataDto comprehensiveResultDataDto = Calculator.calculate(inputDataDto);
        BiFunction<Double, Double, Complex> analyticalSolution = comprehensiveResultDataDto.getAnalyticalSolution();
        implicitSchemeEpsTableRow.setΕ_h_r_h_z(maxEps(comprehensiveResultDataDto.getImplicitSchemeSolution(), comprehensiveResultDataDto.getAnalyticalSolution()));
        crankNicolsonSchemeEpsTableRow.setΕ_h_r_h_z(maxEps(comprehensiveResultDataDto.getCrankNicolsonSchemeSolution(), comprehensiveResultDataDto.getAnalyticalSolution()));

        comprehensiveResultDataDto = Calculator.calculate(inputDataDto.toBuilder().K((inputDataDto.getK()-1)/4+1).J((inputDataDto.getJ()-1)/2+1).build());
        implicitSchemeEpsTableRow.setΕ_2h_r_4h_z(maxEps(comprehensiveResultDataDto.getImplicitSchemeSolution(), analyticalSolution));

        comprehensiveResultDataDto = Calculator.calculate(inputDataDto.toBuilder().K((inputDataDto.getK()-1)/2+1).J((inputDataDto.getJ()-1)/2+1).build());
        crankNicolsonSchemeEpsTableRow.setΕ_2h_r_2h_z(maxEps(comprehensiveResultDataDto.getCrankNicolsonSchemeSolution(), analyticalSolution));

        implicitSchemeEpsTableRow.setΔ_h_r_h_z(implicitSchemeEpsTableRow.getΕ_2h_r_4h_z()/implicitSchemeEpsTableRow.getΕ_h_r_h_z());
        crankNicolsonSchemeEpsTableRow.setΔ_h_r_h_z(crankNicolsonSchemeEpsTableRow.getΕ_2h_r_2h_z()/crankNicolsonSchemeEpsTableRow.getΕ_h_r_h_z());
        return new Pair<>(implicitSchemeEpsTableRow, crankNicolsonSchemeEpsTableRow);
    }

    private double maxEps(Matrix<Complex> schemeSolution, BiFunction<Double, Double, Complex> analyticalSolution) {
        double max = 0;
        double h_r = R/(schemeSolution.getCountRows()-1);
        double h_z = L/(schemeSolution.getCountColumns()-1);
        for (int j = 0; j < schemeSolution.getCountRows(); j++) {
            for (int k = 0; k < schemeSolution.getCountColumns(); k++) {
                double temp = schemeSolution.get(j + 1, k + 1).get().subtract(analyticalSolution.apply(j*h_r,k*h_z)).abs();
                if (temp > max) {
                    max = temp;
                }
            }
        }
        return max;
    }
}
