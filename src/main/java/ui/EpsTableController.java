package ui;

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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ui.tableRows.CrankNicolsonSchemeEpsTableRow;
import ui.tableRows.ImplicitSchemeEpsTableRow;
import ui.tableRows.JKTableRow;

import java.io.*;
import java.net.URL;
import java.util.*;

@AutoInitializableController(name = "Таблица погрешностей аппроксимации", type = Item.CONTROLLER, pathFXML = "epsTable.fxml")
public class EpsTableController implements Initializable, aWindow, InputDataDtoHolder {
    public static final FileChooser.ExtensionFilter xlsxExtensionFilter =
            new FileChooser.ExtensionFilter("XSLX files (*.xlsx)", "*.xlsx");
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
    private List<ObservableList<JKTableRow>> JKConfiguration;

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
            JKConfiguration.get(0).forEach(value -> implicitSchemeEpsTableRows.add(ImplicitSchemeEpsTableRow.populateImplicitSchemeEpsTableRow(inputDataDto.toBuilder().J(value.getJ()).K(value.getK()).build())));
            JKConfiguration.get(1).forEach(value -> crankNicolsonSchemeEpsTableRows.add(CrankNicolsonSchemeEpsTableRow.populateCrankNicolsonSchemeEpsTableRow(inputDataDto.toBuilder().J(value.getJ()).K(value.getK()).build())));
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

    private File save() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить таблицы погрешностей в файл");
        fileChooser.getExtensionFilters().add(xlsxExtensionFilter);
        return fileChooser.showSaveDialog(stage);
    }

    public void setJKConfiguration(List<ObservableList<JKTableRow>> JKConfiguration) {
        this.JKConfiguration = JKConfiguration;
    }
}
