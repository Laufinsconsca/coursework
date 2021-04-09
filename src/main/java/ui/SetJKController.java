package ui;

import enums.Item;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ui.tableRows.JKTableRow;
import ui.warnings.WarningWindows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AutoInitializableController(name = "Настройка J и K", type = Item.CONTROLLER, pathFXML = "setJK.fxml")
public class SetJKController implements Initializable, aWindow {
    private final List<Integer> implicitEpsTableExistingPoints = new ArrayList<>();
    private final List<Integer> implicitEpsPlotExistingPoints = new ArrayList<>();
    private final List<Integer> crankNicolsonEpsTableExistingPoints = new ArrayList<>();
    private final List<Integer> crankNicolsonEpsPlotExistingPoints = new ArrayList<>();
    private List<ObservableList<JKTableRow>> JKConfiguration = new ArrayList<>();

    static final String DEFAULT_DIRECTORY = "src/main/resources";
    @FXML
    private TabPane tabPane, tabPane1, tabPane2;
    private Stage stage;
    @FXML
    private TextField j, k;
    @FXML
    private TableView<JKTableRow> implicitEpsTable, implicitPlotTable, crankNicolsonEpsTable, crankNicolsonPlotTable;
    @FXML
    TableColumn<JKTableRow, String> epsTableJColumn1, epsTableJColumn2;
    @FXML
    TableColumn<JKTableRow, String> epsTableKColumn1, epsTableKColumn2;
    @FXML
    TableColumn<JKTableRow, String> epsPlotJColumn1, epsPlotJColumn2;
    @FXML
    TableColumn<JKTableRow, String> epsPlotKColumn1, epsPlotKColumn2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        epsTableJColumn1.setCellValueFactory(new PropertyValueFactory<>("J"));
        epsTableKColumn1.setCellValueFactory(new PropertyValueFactory<>("K"));
        epsPlotJColumn1.setCellValueFactory(new PropertyValueFactory<>("J"));
        epsPlotKColumn1.setCellValueFactory(new PropertyValueFactory<>("K"));
        epsTableJColumn2.setCellValueFactory(new PropertyValueFactory<>("J"));
        epsTableKColumn2.setCellValueFactory(new PropertyValueFactory<>("K"));
        epsPlotJColumn2.setCellValueFactory(new PropertyValueFactory<>("J"));
        epsPlotKColumn2.setCellValueFactory(new PropertyValueFactory<>("K"));
    }

    private TableView<JKTableRow> currentTable(){
        if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
            if (tabPane1.getSelectionModel().getSelectedIndex() == 0) {
                return implicitEpsTable;
            } else {
                return crankNicolsonEpsTable;
            }
        } else {
            if (tabPane2.getSelectionModel().getSelectedIndex() == 0) {
                return implicitPlotTable;
            } else {
                return crankNicolsonPlotTable;
            }
        }
    }

    private List<Integer> currentExistingPoints(){
        if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
            if (tabPane1.getSelectionModel().getSelectedIndex() == 0) {
                return implicitEpsTableExistingPoints;
            } else {
                return crankNicolsonEpsTableExistingPoints;
            }
        } else {
            if (tabPane2.getSelectionModel().getSelectedIndex() == 0) {
                return implicitEpsPlotExistingPoints;
            } else {
                return crankNicolsonEpsPlotExistingPoints;
            }
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        stage.setOnCloseRequest(windowEvent -> {
            JKConfiguration.add(implicitEpsTable.getItems());
            JKConfiguration.add(crankNicolsonEpsTable.getItems());
            JKConfiguration.add(implicitPlotTable.getItems());
            JKConfiguration.add(crankNicolsonPlotTable.getItems());
        });
        this.stage = stage;
    }

    @FXML
    private void add(){
        TableView<JKTableRow> table = currentTable();
        List<Integer> existingPoints = currentExistingPoints();
        try {
            if (!j.getText().isEmpty() && !k.getText().isEmpty()) {
                if (!existingPoints.contains(Integer.parseInt(j.getText()))) {
                    table.getItems().add(new JKTableRow(Integer.parseInt(j.getText()), Integer.parseInt(k.getText())));
                    existingPoints.add(Integer.parseInt(j.getText()));
                }
                table.getItems().sort(Comparator.comparingInt(JKTableRow::getJ));
            } else if (j.getText().isEmpty()) {
                WarningWindows.showWarning("Поле J пусто");
            } else if (k.getText().isEmpty()) {
                WarningWindows.showWarning("Поле K пусто");
            }
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Введены некорректные значения");
        }
    }

    public void importFromFile() {
        List<JKTableRow> jkTableRowList = getJK();
        currentTable().getItems().addAll(jkTableRowList);
        currentExistingPoints().addAll(jkTableRowList.stream().map(JKTableRow::getJ).collect(Collectors.toList()));
    }

    public void delete() {
        TableView<JKTableRow> table = currentTable();
        if (table.getItems().size() != 0) {
            int index = table.selectionModelProperty().getValue().getFocusedIndex();
            currentExistingPoints().remove(index);
            table.getItems().remove(index);
        }
    }

    private File load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load function");
        fileChooser.setInitialDirectory(new File(DEFAULT_DIRECTORY));
        fileChooser.getExtensionFilters().add(EpsTableController.xlsxExtensionFilter);
        return fileChooser.showOpenDialog(stage);
    }

    public List<JKTableRow> getJK() {
        FileInputStream file = null;
        try {
            file = new FileInputStream(load());
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
        List<JKTableRow> jkList = new ArrayList<>();
        XSSFSheet sheet = workbook.getSheetAt(0);
        JKTableRow jk = new JKTableRow();
        Iterator<Row> iterator = sheet.iterator();
        iterator.next();
        iterator.forEachRemaining(row -> jkList.add(jk.toBuilder().J((int)row.getCell(0).getNumericCellValue()).K((int)row.getCell(1).getNumericCellValue()).build()));
        return jkList;
    }

    public List<ObservableList<JKTableRow>> getJKConfiguration() {
        return JKConfiguration;
    }
}
