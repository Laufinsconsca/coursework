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
import matrix.ComplexMatrix;
import model.Point;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import solution.Calculator;
import tabulatedFunctions.ArrayTabulatedFunction;
import tabulatedFunctions.TabulatedFunction;
import ui.epsTableRows.CrankNicolsonSchemeEpsTableRow;
import ui.epsTableRows.ImplicitSchemeEpsTableRow;
import ui.plot.PlotAccessible;
import ui.plot.PlotController;
import ui.plot.PlotControllerConfiguration;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@AutoInitializableController(name = "Таблица погрешностей аппроксимации", type = Item.CONTROLLER, pathFXML = "epsTable.fxml", isPlotAccessible = true)
public class EpsTableController implements Initializable, aWindow, InputDataDtoHolder, PlotAccessible {
    private static final FileChooser.ExtensionFilter xlsxExtensionFilter =
            new FileChooser.ExtensionFilter("XSLX files (*.xlsx)", "*.xlsx");
    private final PlotControllerConfiguration configuration = new PlotControllerConfiguration("J", "δ", "J", "δ(h_r,h_z)");
    public TableView<ImplicitSchemeEpsTableRow> implicitSchemeTableView;
    public TableView<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeTableView;
    public TableColumn<ImplicitSchemeEpsTableRow, Integer> implicitSchemeTableColumnJ, implicitSchemeTableColumnK;
    public TableColumn<ImplicitSchemeEpsTableRow, Double> implicitSchemeTableColumnΕ_1_2h_r_1_4h_z, implicitSchemeTableColumnΕ_h_r_h_z, implicitSchemeTableColumnΔ_h_r_h_z;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Integer> crankNicolsonSchemeTableColumnJ, crankNicolsonSchemeTableColumnK;
    public TableColumn<CrankNicolsonSchemeEpsTableRow, Double> crankNicolsonSchemeTableColumnΕ_1_2h_r_1_2h_z, crankNicolsonSchemeTableColumnΕ_h_r_h_z, crankNicolsonSchemeTableColumnΔ_h_r_h_z;
    private Stage stage;
    private InputDataDto inputDataDto;
    private double R, L;
    private List<Map<Integer, Integer>> jk;
    private List<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableRows;
    private List<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableRows;
    private PlotController plotController;

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
            jk = getJK("src/main/resources/j_k.xlsx");
            jk.get(0).forEach((key, value) -> implicitSchemeEpsTableRows.add(populateImplicitSchemeEpsTableRow(inputDataDto.toBuilder().J(key).K(value).build())));
            jk.get(1).forEach((key, value) -> crankNicolsonSchemeEpsTableRows.add(populateCrankNicolsonSchemeEpsTableRow(inputDataDto.toBuilder().J(key).K(value).build())));
            ObservableList<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableObservableRows = FXCollections.observableList(implicitSchemeEpsTableRows);
            ObservableList<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableObservableRows = FXCollections.observableList(crankNicolsonSchemeEpsTableRows);
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

    private double maxEps(ComplexMatrix schemeSolution, BiFunction<Double, Double, Complex> analyticalSolution) {
        double max = 0;
        double h_r = R / (schemeSolution.getCountRows() - 1);
        double h_z = L / (schemeSolution.getCountColumns() - 1);
        for (int j = 0; j < schemeSolution.getCountRows(); j++) {
            for (int k = 0; k < schemeSolution.getCountColumns(); k++) {
                double temp = schemeSolution.get(j + 1, k + 1).subtract(analyticalSolution.apply(j * h_r, k * h_z)).abs();
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
        row.createCell(2).setCellValue("ε(h_r,h_z)");
        row.createCell(3).setCellValue("ε(h_r/2,h_z/4)");
        row.createCell(4).setCellValue("δ(h_r,h_z)");
        for (ImplicitSchemeEpsTableRow dataModel : implicitSchemeEpsTableRows) {
            fillImplicitSchemeSheet(implicitSchemeSheet, ++rowNum, dataModel);
        }
        rowNum = 0;
        row = crankNicolsonSchemeSheet.createRow(rowNum);
        row.createCell(0).setCellValue("J");
        row.createCell(1).setCellValue("K");
        row.createCell(2).setCellValue("ε(h_r,h_z)");
        row.createCell(3).setCellValue("ε(h_r/2,h_z/2)");
        row.createCell(4).setCellValue("δ(h_r,h_z)");
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

    @Override
    public PlotController getPlotController() {
        return plotController;
    }

    @Override
    public void setPlotController(PlotController plotController) {
        this.plotController = plotController;
    }

    @FXML
    public void plot() {
        List<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableRows = new ArrayList<>();
        List<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableRows = new ArrayList<>();
        plotController.setConfiguration(configuration);
        List<Map<Integer, Integer>> jk = getJK("src/main/resources/plot/j_k.xlsx");
        jk.get(0).forEach((key, value) -> implicitSchemeEpsTableRows.add(populateImplicitSchemeEpsTableRow(inputDataDto.toBuilder().J(key).K(value).build())));
        jk.get(1).forEach((key, value) -> crankNicolsonSchemeEpsTableRows.add(populateCrankNicolsonSchemeEpsTableRow(inputDataDto.toBuilder().J(key).K(value).build())));
        TabulatedFunction implicitSchemeFunction = getImplicitSchemeFunction(implicitSchemeEpsTableRows);
        implicitSchemeFunction.setName("Неявная схема");
        TabulatedFunction crankNicolsonSchemeFunction = getCrankNicolsonSchemeFunction(crankNicolsonSchemeEpsTableRows);
        crankNicolsonSchemeFunction.setName("Схема Кранка-Николсона");
        List<Point> points = List.of(new Point(implicitSchemeEpsTableRows.get(0).getJ(), 0, new Complex(4, 0)), new Point(implicitSchemeEpsTableRows.get(implicitSchemeEpsTableRows.size() - 1).getJ(), 0, new Complex(4, 0)));
        TabulatedFunction function = new ArrayTabulatedFunction(FXCollections.observableList(points), 0);
        function.setName("Теоретический предел");
        plotController.setSeries(function);
        plotController.addSeries(implicitSchemeFunction);
        plotController.addSeries(crankNicolsonSchemeFunction);
        plotController.getStage().show();
    }

    private TabulatedFunction getImplicitSchemeFunction(List<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableRows) {
        List<Point> implicitSchemePoints = implicitSchemeEpsTableRows.stream()
                .map(implicitSchemeEpsTableRow ->
                        new Point(implicitSchemeEpsTableRow.getJ(),
                                implicitSchemeEpsTableRow.getK(),
                                new Complex(implicitSchemeEpsTableRow.getΔ_h_r_h_z(), 0)))
                .collect(Collectors.toList());
        return new ArrayTabulatedFunction(FXCollections.observableList(implicitSchemePoints), 0);
    }

    private TabulatedFunction getCrankNicolsonSchemeFunction(List<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableRows) {
        List<Point> crankNicolsonSchemePoints = crankNicolsonSchemeEpsTableRows.stream()
                .map(crankNicolsonSchemeEpsTableRow ->
                        new Point(crankNicolsonSchemeEpsTableRow.getJ(),
                                crankNicolsonSchemeEpsTableRow.getK(),
                                new Complex(crankNicolsonSchemeEpsTableRow.getΔ_h_r_h_z(), 0)))
                .collect(Collectors.toList());
        return new ArrayTabulatedFunction(FXCollections.observableList(crankNicolsonSchemePoints), 0);
    }
}
