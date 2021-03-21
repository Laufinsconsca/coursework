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
import matrices.matrix.Matrix;
import method.Calculator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.BiFunction;

@AutoInitializableController(name = "Таблица погрешностей", type = Item.CONTROLLER, pathFXML = "epsTable.fxml")
public class EpsTableController implements Initializable, aWindow, InputDataDtoHolder {
    private ObservableList<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableObservableRows;
    private ObservableList<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableObservableRows;
    public TableView<ImplicitSchemeEpsTableRow> implicitSchemeTableView;
    public TableView<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeTableView;
    public TableColumn<ImplicitSchemeEpsTableRow, Integer> implicitSchemeTableColumnJ, implicitSchemeTableColumnK;
    public TableColumn<ImplicitSchemeEpsTableRow, Double> implicitSchemeTableColumnΕ_2h_r_4h_z, implicitSchemeTableColumnΕ_h_r_h_z, implicitSchemeTableColumnΔ_h_r_h_z;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Integer> crankNicolsonSchemeTableColumnJ, crankNicolsonSchemeTableColumnK;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Double> crankNicolsonSchemeTableColumnΕ_2h_r_2h_z, crankNicolsonSchemeTableColumnΕ_h_r_h_z, crankNicolsonSchemeTableColumnΔ_h_r_h_z;
    private Stage stage;
    private InputDataDto inputDataDto;
    private double R, L;
    private Map<Integer, Integer> implicitSchemeKJ;
    private Map<Integer, Integer> crankNicolsonSchemeKJ;

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
            implicitSchemeKJ = getJK("src/main/resources/implicit_scheme_k_j.xlsx");
            crankNicolsonSchemeKJ = getJK("src/main/resources/crank_nicolson_scheme_k_j.xlsx");
            implicitSchemeKJ.forEach((key, value) -> implicitSchemeEpsTableRows.add(populateImplicitSchemeEpsTableRow(inputDataDto.toBuilder().J(key).K(value).build())));
            crankNicolsonSchemeKJ.forEach((key, value) -> crankNicolsonSchemeEpsTableRows.add(populateCrankNicolsonSchemeEpsTableRow(inputDataDto.toBuilder().J(key).K(value).build())));
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

    private ImplicitSchemeEpsTableRow populateImplicitSchemeEpsTableRow(InputDataDto inputDataDto) {
        ImplicitSchemeEpsTableRow implicitSchemeEpsTableRow = new ImplicitSchemeEpsTableRow();
        implicitSchemeEpsTableRow.setJ(inputDataDto.getJ());
        implicitSchemeEpsTableRow.setK(inputDataDto.getK());

        ComprehensiveResultDataDto comprehensiveResultDataDto = Calculator.calculate(inputDataDto);
        BiFunction<Double, Double, Complex> analyticalSolution = comprehensiveResultDataDto.getAnalyticalSolution();
        implicitSchemeEpsTableRow.setΕ_h_r_h_z(maxEps(comprehensiveResultDataDto.getImplicitSchemeSolution(), comprehensiveResultDataDto.getAnalyticalSolution()));

        comprehensiveResultDataDto = Calculator.calculate(inputDataDto.toBuilder().K(inputDataDto.getK() / 4).J(inputDataDto.getJ() / 2).build());
        implicitSchemeEpsTableRow.setΕ_2h_r_4h_z(maxEps(comprehensiveResultDataDto.getImplicitSchemeSolution(), analyticalSolution));
        implicitSchemeEpsTableRow.setΔ_h_r_h_z(implicitSchemeEpsTableRow.getΕ_2h_r_4h_z() / implicitSchemeEpsTableRow.getΕ_h_r_h_z());
        return implicitSchemeEpsTableRow;
    }

    private CrankNicolsonSchemeEpsTableRow populateCrankNicolsonSchemeEpsTableRow(InputDataDto inputDataDto) {
        CrankNicolsonSchemeEpsTableRow crankNicolsonSchemeEpsTableRow = new CrankNicolsonSchemeEpsTableRow();
        crankNicolsonSchemeEpsTableRow.setJ(inputDataDto.getJ());
        crankNicolsonSchemeEpsTableRow.setK(inputDataDto.getK());

        ComprehensiveResultDataDto comprehensiveResultDataDto = Calculator.calculate(inputDataDto);
        BiFunction<Double, Double, Complex> analyticalSolution = comprehensiveResultDataDto.getAnalyticalSolution();
        crankNicolsonSchemeEpsTableRow.setΕ_h_r_h_z(maxEps(comprehensiveResultDataDto.getCrankNicolsonSchemeSolution(), comprehensiveResultDataDto.getAnalyticalSolution()));

        comprehensiveResultDataDto = Calculator.calculate(inputDataDto.toBuilder().K(inputDataDto.getK() / 2).J(inputDataDto.getJ() / 2).build());
        crankNicolsonSchemeEpsTableRow.setΕ_2h_r_2h_z(maxEps(comprehensiveResultDataDto.getCrankNicolsonSchemeSolution(), analyticalSolution));
        crankNicolsonSchemeEpsTableRow.setΔ_h_r_h_z(crankNicolsonSchemeEpsTableRow.getΕ_2h_r_2h_z() / crankNicolsonSchemeEpsTableRow.getΕ_h_r_h_z());
        return crankNicolsonSchemeEpsTableRow;
    }

    private double maxEps(Matrix<Complex> schemeSolution, BiFunction<Double, Double, Complex> analyticalSolution) {
        double max = 0;
        double h_r = R / (schemeSolution.getCountRows() - 1);
        double h_z = L / (schemeSolution.getCountColumns() - 1);
        for (int j = 0; j < schemeSolution.getCountRows(); j++) {
            for (int k = 0; k < schemeSolution.getCountColumns(); k++) {
                double temp = schemeSolution.get(j + 1, k + 1).get().subtract(analyticalSolution.apply(j * h_r, k * h_z)).abs();
                if (temp > max) {
                    max = temp;
                }
            }
        }
        return max;
    }

    private static Map<Integer, Integer> getJK(String source) {
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(source));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XSSFWorkbook workbook = null;
        try {
            assert file != null;
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert workbook != null;
        XSSFSheet sheet = workbook.getSheetAt(0);
        Map<Integer, Integer> kj = new TreeMap<>();
        Iterator<Row> iterator = sheet.iterator();
        iterator.next();
        iterator.forEachRemaining(row -> kj.put((int) row.getCell(0).getNumericCellValue(), (int) row.getCell(1).getNumericCellValue()));
        return kj;
    }
}
