package ui;

import enums.Item;
import exceptions.JKConfigurationException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ui.tableRows.JKTableRow;
import ui.warnings.WarningWindows;

import java.io.*;
import java.net.URL;
import java.util.*;

@AutoInitializableController(name = "Настройка J и K", type = Item.CONTROLLER, pathFXML = "setJK.fxml")
public class SetJKController implements Initializable, aWindow {
    public static final FileChooser.ExtensionFilter xlsxExtensionFilter =
            new FileChooser.ExtensionFilter("XSLX files (*.xlsx)", "*.xlsx");
    static final String DEFAULT_DIRECTORY = System.getProperty("user.home");
    private final Set<Integer> implicitEpsTableExistingPoints = new HashSet<>();
    private final Set<Integer> implicitEpsPlotExistingPoints = new HashSet<>();
    private final Set<Integer> crankNicolsonEpsTableExistingPoints = new HashSet<>();
    private final Set<Integer> crankNicolsonEpsPlotExistingPoints = new HashSet<>();
    private final List<ObservableList<JKTableRow>> JKConfiguration = new ArrayList<>();
    @FXML
    TableColumn<JKTableRow, String> epsTableJColumn1, epsTableJColumn2;
    @FXML
    TableColumn<JKTableRow, String> epsTableKColumn1, epsTableKColumn2;
    @FXML
    TableColumn<JKTableRow, String> epsPlotJColumn1, epsPlotJColumn2;
    @FXML
    TableColumn<JKTableRow, String> epsPlotKColumn1, epsPlotKColumn2;
    @FXML
    private TabPane tabPane, tabPane1, tabPane2;
    private Stage stage;
    @FXML
    private TextField j, k;
    @FXML
    private TableView<JKTableRow> implicitEpsTable, implicitPlotTable, crankNicolsonEpsTable, crankNicolsonPlotTable;

    private static void fillSheet(XSSFSheet sheet, TableView<JKTableRow> table) {
        int rowNum = 0;
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("J");
        row.createCell(1).setCellValue("K");
        for (JKTableRow dataModel : table.getItems()) {
            row = sheet.createRow(++rowNum);
            row.createCell(0).setCellValue(dataModel.getJ());
            row.createCell(1).setCellValue(dataModel.getK());
        }
    }

    public static File save(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить таблицы погрешностей в файл");
        fileChooser.getExtensionFilters().add(xlsxExtensionFilter);
        return fileChooser.showSaveDialog(stage);
    }

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

    private TableView<JKTableRow> currentTable() {
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

    private Set<Integer> currentExistingPoints() {
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
    private void add() {
        TableView<JKTableRow> table = currentTable();
        Set<Integer> existingPoints = currentExistingPoints();
        try {
            if (!j.getText().isEmpty() && !k.getText().isEmpty()) {
                int jValue = Integer.parseInt(j.getText());
                int kValue = Integer.parseInt(k.getText());
                if (jValue <= 0 || kValue <= 0) {
                    throw new NumberFormatException();
                }
                if (!existingPoints.contains(jValue)) {
                    table.getItems().add(new JKTableRow(jValue, kValue));
                    existingPoints.add(jValue);
                    j.setText("");
                    k.setText("");
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
        try {
            List<List<JKTableRow>> jkTableRowList = getJK();
            switch (jkTableRowList.size()) {
                case 1 : {
                    importHelper(jkTableRowList.get(0), currentTable(), currentExistingPoints());
                    currentTable().getItems().sort(Comparator.comparingInt(JKTableRow::getJ));
                    break;
                }
                case 2 : {
                    if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                        importHelper(jkTableRowList.get(0), implicitEpsTable, implicitEpsTableExistingPoints);
                        importHelper(jkTableRowList.get(1), crankNicolsonEpsTable, crankNicolsonEpsTableExistingPoints);
                    } else {
                        importHelper(jkTableRowList.get(0), implicitPlotTable, implicitEpsPlotExistingPoints);
                        importHelper(jkTableRowList.get(1), crankNicolsonPlotTable, crankNicolsonEpsPlotExistingPoints);
                    }
                    break;
                }
                case 4 : {
                    importHelper(jkTableRowList.get(0), implicitEpsTable, implicitEpsTableExistingPoints);
                    importHelper(jkTableRowList.get(1), crankNicolsonEpsTable, crankNicolsonEpsTableExistingPoints);
                    importHelper(jkTableRowList.get(2), implicitPlotTable, implicitEpsPlotExistingPoints);
                    importHelper(jkTableRowList.get(3), crankNicolsonPlotTable, crankNicolsonEpsPlotExistingPoints);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            WarningWindows.showWarning("Ошибка открытия файла");
        } catch (NullPointerException ignored) {

        }
    }

    private void importHelper(List<JKTableRow> list, TableView<JKTableRow> table, Set<Integer> existingPoints) {
        for (JKTableRow row : list) {
            if (!existingPoints.contains(row.getJ())) {
                table.getItems().add(new JKTableRow(row.getJ(), row.getK()));
                existingPoints.add(row.getJ());
            }
        }
        table.getItems().sort(Comparator.comparingInt(JKTableRow::getJ));
    }

    @FXML
    private void export() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        fillSheet(workbook.createSheet("ТП;Неявная схема"), implicitEpsTable);
        fillSheet(workbook.createSheet("ТП;Схема Кранка-Николсона"), crankNicolsonEpsTable);
        fillSheet(workbook.createSheet("ГОП;Неявная схема"), implicitPlotTable);
        fillSheet(workbook.createSheet("ГОП;Схема Кранка-Николсона"), crankNicolsonPlotTable);
        try (FileOutputStream out = new FileOutputStream(save(stage))) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
        }
    }

    public void delete() {
        TableView<JKTableRow> table = currentTable();
        if (table.getItems().size() != 0) {
            int index = table.selectionModelProperty().getValue().getFocusedIndex();
            currentExistingPoints().remove(index);
            table.getItems().remove(index);
        }
    }

    public List<List<JKTableRow>> getJK() throws FileNotFoundException, NullPointerException {
        FileInputStream file = new FileInputStream(load());
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert workbook != null;
        List<List<JKTableRow>> lists = new ArrayList<>();
        try {
            switch (workbook.getNumberOfSheets()) {
                case 1 : {
                    List<JKTableRow> jkList = new ArrayList<>();
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    JKTableRow jk = new JKTableRow();
                    Iterator<Row> iterator = sheet.iterator();
                    iterator.next();
                    iterator.forEachRemaining(row -> jkList.add(jk.toBuilder().J((int) row.getCell(0).getNumericCellValue()).K((int) row.getCell(1).getNumericCellValue()).build()));
                    lists.add(jkList);
                    break;
                }
                case 2 : {
                    if (!workbook.getSheetName(0).equals("Неявная схема") || !workbook.getSheetName(1).equals("Схема Кранка-Николсона")) {
                        throw new JKConfigurationException("Имена листов должны быть \"Неявная схема\" и \"Схема Кранка-Николсона\" соответственно");
                    }
                    for (int i = 0; i < 2; i++) {
                        List<JKTableRow> jkList = new ArrayList<>();
                        XSSFSheet sheet = workbook.getSheetAt(i);
                        JKTableRow jk = new JKTableRow();
                        Iterator<Row> iterator = sheet.iterator();
                        iterator.next();
                        iterator.forEachRemaining(row -> jkList.add(jk.toBuilder().J((int) row.getCell(0).getNumericCellValue()).K((int) row.getCell(1).getNumericCellValue()).build()));
                        lists.add(jkList);
                    }
                    break;
                }
                case 4 : {
                    if (!workbook.getSheetName(0).equals("ТП;Неявная схема")
                            || !workbook.getSheetName(1).equals("ТП;Схема Кранка-Николсона")
                            || !workbook.getSheetName(2).equals("ГОП;Неявная схема")
                            || !workbook.getSheetName(3).equals("ГОП;Схема Кранка-Николсона")) {
                        throw new JKConfigurationException(
                                "Имена листов должны быть \"ТП;Неявная схема\",\n" +
                                "\"ТП;Схема Кранка-Николсона\",\n" +
                                "\"ГОП;Неявная схема\"и \"ГОП;Схема Кранка-Николсона\"\n" +
                                "соответственно");
                    }
                    for (int i = 0; i < 4; i++) {
                        List<JKTableRow> jkList = new ArrayList<>();
                        XSSFSheet sheet = workbook.getSheetAt(i);
                        JKTableRow jk = new JKTableRow();
                        Iterator<Row> iterator = sheet.iterator();
                        iterator.next();
                        iterator.forEachRemaining(row -> jkList.add(jk.toBuilder().J((int) row.getCell(0).getNumericCellValue()).K((int) row.getCell(1).getNumericCellValue()).build()));
                        lists.add(jkList);
                    }
                    break;
                }
                default : throw new JKConfigurationException("Неверное количество листов в Excel файле");
            }
        } catch (JKConfigurationException jke) {
            WarningWindows.showWarning(jke.getMessage());
        }
        return lists;
    }

    private File load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load function");
        fileChooser.setInitialDirectory(new File(DEFAULT_DIRECTORY));
        fileChooser.getExtensionFilters().add(xlsxExtensionFilter);
        return fileChooser.showOpenDialog(stage);
    }

    public List<ObservableList<JKTableRow>> getJKConfiguration() {
        return JKConfiguration;
    }
}
