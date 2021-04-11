package ui;

import dto.CrossSectionResultDataDto;
import dto.InputDataDto;
import enums.FixedVariableType;
import exceptions.InvalidArrayLengthException;
import exceptions.JKConfigurationException;
import exceptions.NoGraphsToPlotException;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Point;
import model.complex.Complex;
import model.tabulatedFunction.ArrayTabulatedFunction;
import model.tabulatedFunction.TabulatedFunction;
import solution.Calculator;
import ui.plot.PlotController;
import ui.plot.PlotControllerConfiguration;
import ui.tableRows.CrankNicolsonSchemeEpsTableRow;
import ui.tableRows.ImplicitSchemeEpsTableRow;
import ui.tableRows.JKTableRow;
import ui.warnings.WarningWindows;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class InitController extends AbstractParentController implements Initializable {
    private final String uiColor = "#cde0cd";
    public TextField uImplicitSchemeTextField;
    public TextField uCrankNicolsonSchemeTextField;
    public TextField uAnalyticalSolutionTextField;
    public CheckBox rCheckBox, zCheckBox;
    @FXML
    AnchorPane mainPane;
    private FixedVariableType fixedVariableType = FixedVariableType.z;
    @FXML
    private TextField LTextField, RTextField, nTextField, λTextField, JTextField, KTextField, rTextField, zTextField, nEigenfunctionTextField;
    private double[] r, z;
    private InputDataDto inputDataDto;
    private CrossSectionResultDataDto crossSectionResultDataDto;
    private PlotControllerConfiguration rConstPlotConfiguration;
    private PlotControllerConfiguration zConstPlotConfiguration;
    private PlotControllerConfiguration currentPlotConfiguration;
    private PlotControllerConfiguration epsConfiguration;
    private List<ObservableList<JKTableRow>> JKConfiguration;

    @FXML
    private CheckBox analyticalSolutionCheckBox, implicitSchemeCheckBox, crankNicolsonSchemeCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rCheckBox.selectedProperty()
                .addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    zCheckBox.setSelected(old_val);
                    fixedVariableType = FixedVariableType.r;
                });
        zCheckBox.selectedProperty()
                .addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    rCheckBox.setSelected(old_val);
                    fixedVariableType = FixedVariableType.z;
                });
        Initializer.initializeWindowControllers(this.getClass(), stage, controllerMap);
        LTextField.setText("5");
        RTextField.setText("6");
        nTextField.setText("1.4");
        λTextField.setText("1");
        rTextField.setText("3");
        zTextField.setText("5");
        nEigenfunctionTextField.setText("1");
        JTextField.setText("10");
        KTextField.setText("10");
        mainPane.setStyle("-fx-background-color:" + uiColor);
        rConstPlotConfiguration = new PlotControllerConfiguration("z", "u", "r, расстояние вдоль радиуса волновода", "|u(r,z)|");
        zConstPlotConfiguration = new PlotControllerConfiguration("r", "u", "z, расстояние вдоль волновода", "|u(r,z)|");
        epsConfiguration = new PlotControllerConfiguration("J", "δ", "J", "δ(h_r,h_z)");
    }

    private void readInitialConditions() throws NumberFormatException {
        double l = Double.parseDouble(LTextField.getText());
        double r1 = Double.parseDouble(RTextField.getText());
        double n = Double.parseDouble(nTextField.getText());
        double λ = Double.parseDouble(λTextField.getText());
        r = Arrays.stream(rTextField.getText().split(";")).mapToDouble(Double::parseDouble).toArray();
        z = Arrays.stream(zTextField.getText().split(";")).mapToDouble(Double::parseDouble).toArray();
        Integer j = Integer.parseInt(JTextField.getText());
        Integer k = Integer.parseInt(KTextField.getText());
        Integer nEigenfunction = Integer.parseInt(nEigenfunctionTextField.getText());
        if (fixedVariableType.equals(FixedVariableType.r)) {
            inputDataDto = new InputDataDto(j, k, nEigenfunction, λ, n, l, r1, r, fixedVariableType);
            currentPlotConfiguration = rConstPlotConfiguration;
        } else {
            inputDataDto = new InputDataDto(j, k, nEigenfunction, λ, n, l, r1, z, fixedVariableType);
            currentPlotConfiguration = zConstPlotConfiguration;
        }
        JKConfiguration = ((SetJKController) getController("setJK")).getJKConfiguration();
    }

    @FXML
    private void calculate() {
        try {
            readInitialConditions();
            if (r.length != 1 || z.length != 1) {
                throw new InvalidArrayLengthException();
            }
            crossSectionResultDataDto = Calculator.crossSectionCalculate(inputDataDto);
            uAnalyticalSolutionTextField.setText(crossSectionResultDataDto.getAnalyticalSolution().get(0).apply(r[0]).abs() + "");
            uImplicitSchemeTextField.setText(crossSectionResultDataDto.getImplicitSchemeSolution().get(0).apply(r[0]).abs() + "");
            uCrankNicolsonSchemeTextField.setText(crossSectionResultDataDto.getCrankNicolsonSchemeSolution().get(0).apply(r[0]).abs() + "");
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
        } catch (InvalidArrayLengthException ex) {
            WarningWindows.showWarning("Слишком много входных переменных");
        }
    }

    @FXML
    public void epsTable() {
        try {
            readInitialConditions();
            EpsTableController controller = (EpsTableController) getController();
            if (JKConfiguration.isEmpty() || (JKConfiguration.get(0).isEmpty() && JKConfiguration.get(1).isEmpty())) {
                throw new JKConfigurationException("Не заданы J и K");
            } else if (JKConfiguration.get(0).isEmpty() || JKConfiguration.get(1).isEmpty()) {
                throw new JKConfigurationException("J и K заданы только для одной схемы");
            } else {
                controller.setJKConfiguration(JKConfiguration);
            }
            controller.setInputDataDto(inputDataDto);
            controller.getStage().show();
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
        } catch (JKConfigurationException re) {
            WarningWindows.showWarning(re.getMessage());
        }
    }

    @FXML
    private void setJK() {
        open(false);
    }

    @FXML
    public void plot() {
        try {
            readInitialConditions();
            PlotController plotController = (PlotController) getController();
            plotController.getStage().setTitle("Построение графика");
            plotController.setConfiguration(currentPlotConfiguration);
            crossSectionResultDataDto = Calculator.crossSectionCalculate(inputDataDto);
            List<TabulatedFunction> functions = new ArrayList<>();
            if (analyticalSolutionCheckBox.isSelected()) {
                functions.addAll(crossSectionResultDataDto.getAnalyticalSolution());
            }
            if (implicitSchemeCheckBox.isSelected()) {
                functions.addAll(crossSectionResultDataDto.getImplicitSchemeSolution());
            }
            if (crankNicolsonSchemeCheckBox.isSelected()) {
                functions.addAll(crossSectionResultDataDto.getCrankNicolsonSchemeSolution());
            }
            if (functions.isEmpty()) {
                throw new NoGraphsToPlotException();
            }
            boolean isFirst = true;
            for (TabulatedFunction function : functions) {
                if (isFirst) {
                    plotController.setSeries(function);
                    isFirst = false;
                } else {
                    plotController.addSeries(function);
                }
            }
            plotController.getStage().show();
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
        } catch (NoGraphsToPlotException ex) {
            WarningWindows.showWarning("К построению не выбран ни один из графиков");
        }
    }

    @FXML
    public void plotEpsTable() {
        try {
            readInitialConditions();
            if (JKConfiguration.isEmpty() || (JKConfiguration.get(2).isEmpty() && JKConfiguration.get(3).isEmpty())) {
                throw new JKConfigurationException("Не заданы J и K");
            } else if (JKConfiguration.get(2).isEmpty() || JKConfiguration.get(3).isEmpty()) {
                throw new JKConfigurationException("J и K заданы только для одной схемы");
            }
            PlotController plotController = (PlotController) getController("plot");
            plotController.getStage().setTitle("Построение графика отношения погрешностей аппроксимации");
            List<ImplicitSchemeEpsTableRow> implicitSchemeEpsTableRows = new ArrayList<>();
            List<CrankNicolsonSchemeEpsTableRow> crankNicolsonSchemeEpsTableRows = new ArrayList<>();
            plotController.setConfiguration(epsConfiguration);
            JKConfiguration.get(2).forEach(value -> implicitSchemeEpsTableRows.add(ImplicitSchemeEpsTableRow.populateImplicitSchemeEpsTableRow(inputDataDto.toBuilder().J(value.getJ()).K(value.getK()).build())));
            JKConfiguration.get(3).forEach(value -> crankNicolsonSchemeEpsTableRows.add(CrankNicolsonSchemeEpsTableRow.populateCrankNicolsonSchemeEpsTableRow(inputDataDto.toBuilder().J(value.getJ()).K(value.getK()).build())));
            TabulatedFunction implicitSchemeFunction = ImplicitSchemeEpsTableRow.getImplicitSchemeFunction(implicitSchemeEpsTableRows);
            implicitSchemeFunction.setName("Неявная схема");
            TabulatedFunction crankNicolsonSchemeFunction = CrankNicolsonSchemeEpsTableRow.getCrankNicolsonSchemeFunction(crankNicolsonSchemeEpsTableRows);
            crankNicolsonSchemeFunction.setName("Схема Кранка-Николсона");
            List<Point> points = new ArrayList<>();
            points.add(new Point(implicitSchemeEpsTableRows.get(0).getJ(), 0, new Complex(4, 0)));
            points.add(new Point(implicitSchemeEpsTableRows.get(implicitSchemeEpsTableRows.size() - 1).getJ(), 0, new Complex(4, 0)));
            TabulatedFunction function = new ArrayTabulatedFunction(points, 0);
            function.setName("Теоретический предел");
            plotController.setSeries(function);
            plotController.addSeries(implicitSchemeFunction);
            plotController.addSeries(crankNicolsonSchemeFunction);
            plotController.getStage().show();
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
        } catch (JKConfigurationException re) {
            WarningWindows.showWarning(re.getMessage());
        }
    }

}
