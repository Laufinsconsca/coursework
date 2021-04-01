package ui;

import complex.Complex;
import dto.ComprehensiveResultDataDto;
import dto.InputDataDto;
import enums.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import matrices.matrix.Matrix;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import solution.Calculator;
import ui.epsTableRows.CrankNicolsonSchemeEpsTableRow;
import ui.epsTableRows.ImplicitSchemeEpsTableRow;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.BiFunction;

@AutoInitializableController(name = "Таблица погрешностей аппроксимации", type = Item.CONTROLLER, pathFXML = "epsTable.fxml")
public class EpsTableController implements Initializable, aWindow, InputDataDtoHolder {
    private static final FileChooser.ExtensionFilter xlsxExtensionFilter =
            new FileChooser.ExtensionFilter("XSLX files (*.xlsx)", "*.xlsx");
    public TableView<ImplicitSchemeEpsTableRow> implicitSchemeTableView;
    public TableView<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeTableView;
    public TableColumn<ImplicitSchemeEpsTableRow, Integer> implicitSchemeTableColumnJ, implicitSchemeTableColumnK;
    public TableColumn<ImplicitSchemeEpsTableRow, Double> implicitSchemeTableColumnΕ_1_2h_r_1_4h_z, implicitSchemeTableColumnΕ_h_r_h_z, implicitSchemeTableColumnΔ_h_r_h_z;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Integer> crankNicolsonSchemeTableColumnJ, crankNicolsonSchemeTableColumnK;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Double> crankNicolsonSchemeTableColumnΕ_1_2h_r_1_2h_z, crankNicolsonSchemeTableColumnΕ_h_r_h_z, crankNicolsonSchemeTableColumnΔ_h_r_h_z;
    private ObservableList<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableObservableRows;
    private ObservableList<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableObservableRows;
    private Stage stage;
    private InputDataDto inputDataDto;
    private double R, L;
    private List<Map<Integer, Integer>> kj;
    private List<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableRows;
    private List<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableRows;

    private static List<Map<Integer, Integer>> getJK(String source) {
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
        List<Map<Integer, Integer>> kjList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            Map<Integer, Integer> kj = new TreeMap<>();
            Iterator<Row> iterator = sheet.iterator();
            iterator.next();
            iterator.forEachRemaining(row -> kj.put((int) row.getCell(0).getNumericCellValue(), (int) row.getCell(1).getNumericCellValue()));
            kjList.add(kj);
        }
        return kjList;
    }

    private static void fillImplicitSchemeSheet(XSSFSheet sheet, int rowNum, ImplicitSchemeEpsTableRow dataModel) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(dataModel.getJ());
        row.createCell(1).setCellValue(dataModel.getK());
        row.createCell(2).setCellValue(dataModel.getΕ_h_r_h_z());
        row.createCell(3).setCellValue(dataModel.getΕ_1_2h_r_1_4h_z());
        row.createCell(4).setCellValue(dataModel.getΔ_h_r_h_z());
    }

    private static void fillCrankNicolsonSchemeSheet(XSSFSheet sheet, int rowNum, CrankNicolsonSchemeEpsTableRow dataModel) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(dataModel.getJ());
        row.createCell(1).setCellValue(dataModel.getK());
        row.createCell(2).setCellValue(dataModel.getΕ_h_r_h_z());
        row.createCell(3).setCellValue(dataModel.getΕ_1_2h_r_1_2h_z());
        row.createCell(4).setCellValue(dataModel.getΔ_h_r_h_z());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        implicitSchemeTableColumnJ.setCellValueFactory(new PropertyValueFactory<>("J"));
        implicitSchemeTableColumnK.setCellValueFactory(new PropertyValueFactory<>("K"));
        implicitSchemeTableColumnΕ_1_2h_r_1_4h_z.setCellValueFactory(new PropertyValueFactory<>("ε_1_2h_r_1_4h_z"));
        implicitSchemeTableColumnΕ_h_r_h_z.setCellValueFactory(new PropertyValueFactory<>("ε_h_r_h_z"));
        implicitSchemeTableColumnΔ_h_r_h_z.setCellValueFactory(new PropertyValueFactory<>("δ_h_r_h_z"));
        crankNicolsonSchemeTableColumnJ.setCellValueFactory(new PropertyValueFactory<>("J"));
        crankNicolsonSchemeTableColumnK.setCellValueFactory(new PropertyValueFactory<>("K"));
        crankNicolsonSchemeTableColumnΕ_1_2h_r_1_2h_z.setCellValueFactory(new PropertyValueFactory<>("ε_1_2h_r_1_2h_z"));
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
            implicitSchemeEpsTableRows = new ArrayList<>();
            crankNicolsonSchemeEpsTableRows = new ArrayList<>();
            kj = getJK("src/main/resources/k_j.xlsx");
            kj.get(0).forEach((key, value) -> implicitSchemeEpsTableRows.add(populateImplicitSchemeEpsTableRow(inputDataDto.toBuilder().J(key).K(value).build())));
            kj.get(1).forEach((key, value) -> crankNicolsonSchemeEpsTableRows.add(populateCrankNicolsonSchemeEpsTableRow(inputDataDto.toBuilder().J(key).K(value).build())));
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

        comprehensiveResultDataDto = Calculator.calculate(inputDataDto.toBuilder().K(inputDataDto.getK() * 4).J(inputDataDto.getJ() * 2).build());
        implicitSchemeEpsTableRow.setΕ_1_2h_r_1_4h_z(maxEps(comprehensiveResultDataDto.getImplicitSchemeSolution(), analyticalSolution));
        implicitSchemeEpsTableRow.setΔ_h_r_h_z(implicitSchemeEpsTableRow.getΕ_h_r_h_z() / implicitSchemeEpsTableRow.getΕ_1_2h_r_1_4h_z());
        return implicitSchemeEpsTableRow;
    }

    private CrankNicolsonSchemeEpsTableRow populateCrankNicolsonSchemeEpsTableRow(InputDataDto inputDataDto) {
        CrankNicolsonSchemeEpsTableRow crankNicolsonSchemeEpsTableRow = new CrankNicolsonSchemeEpsTableRow();
        crankNicolsonSchemeEpsTableRow.setJ(inputDataDto.getJ());
        crankNicolsonSchemeEpsTableRow.setK(inputDataDto.getK());

        ComprehensiveResultDataDto comprehensiveResultDataDto = Calculator.calculate(inputDataDto);
        BiFunction<Double, Double, Complex> analyticalSolution = comprehensiveResultDataDto.getAnalyticalSolution();
        crankNicolsonSchemeEpsTableRow.setΕ_h_r_h_z(maxEps(comprehensiveResultDataDto.getCrankNicolsonSchemeSolution(), comprehensiveResultDataDto.getAnalyticalSolution()));

        comprehensiveResultDataDto = Calculator.calculate(inputDataDto.toBuilder().K(inputDataDto.getK() * 2).J(inputDataDto.getJ() * 2).build());
        crankNicolsonSchemeEpsTableRow.setΕ_1_2h_r_1_2h_z(maxEps(comprehensiveResultDataDto.getCrankNicolsonSchemeSolution(), analyticalSolution));
        crankNicolsonSchemeEpsTableRow.setΔ_h_r_h_z(crankNicolsonSchemeEpsTableRow.getΕ_h_r_h_z() / crankNicolsonSchemeEpsTableRow.getΕ_1_2h_r_1_2h_z());
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

    @FXML
    public void export() {
        saveEpsTableToExcelFile(implicitSchemeEpsTableRows, crankNicolsonSchemeEpsTableRows);
    }

    private void saveEpsTableToExcelFile(List<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableRows,
                                         List<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableRows) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet implicitSchemeSheet = workbook.createSheet("Неявная схема");
        XSSFSheet crankNicolsonSchemeSheet = workbook.createSheet("Схема Кранка-Николсона");
        int rowNum = 0;
        Row row = implicitSchemeSheet.createRow(rowNum);
        row.createCell(0).setCellValue("J");
        row.createCell(1).setCellValue("K");
        row.createCell(2).setCellValue("ε_1_2h_r_1_4h_z");
        row.createCell(3).setCellValue("ε_h_r_h_z");
        row.createCell(4).setCellValue("δ_h_r_h_z");
        for (ImplicitSchemeEpsTableRow dataModel : implicitSchemeEpsTableRows) {
            fillImplicitSchemeSheet(implicitSchemeSheet, ++rowNum, dataModel);
        }
        rowNum = 0;
        row = crankNicolsonSchemeSheet.createRow(rowNum);
        row.createCell(0).setCellValue("J");
        row.createCell(1).setCellValue("K");
        row.createCell(2).setCellValue("ε_1_2h_r_1_2h_z");
        row.createCell(3).setCellValue("ε_h_r_h_z");
        row.createCell(4).setCellValue("δ_h_r_h_z");
        for (CrankNicolsonSchemeEpsTableRow dataModel : crankNicolsonSchemeEpsTableRows) {
            fillCrankNicolsonSchemeSheet(crankNicolsonSchemeSheet, ++rowNum, dataModel);
        }
        try (FileOutputStream out = new FileOutputStream(save())) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File save() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить таблицы погрешностей в файл");
        fileChooser.getExtensionFilters().add(xlsxExtensionFilter);
        return fileChooser.showSaveDialog(stage);
    }
}
