package ui;

import dto.CrossSectionResultDataDto;
import dto.InputDataDto;
import enums.FixedVariableType;
import exceptions.InvalidArrayLengthException;
import exceptions.NoGraphsToPlotException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import solution.Calculator;
import tabulatedFunctions.TabulatedFunction;
import ui.warnings.WarningWindows;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class InitController extends AbstractParentController implements Initializable {
    public TextField uImplicitSchemeTextField;
    public TextField uCrankNicolsonSchemeTextField;
    public TextField uAnalyticalSolutionTextField;
    @FXML
    AnchorPane mainPane;
    private FixedVariableType fixedVariableType = FixedVariableType.z;
    @FXML
    private TextField LTextField, RTextField, nTextField, λTextField, JTextField, KTextField, rTextField, zTextField, nEigenfunctionTextField;
    private double L, R, n, λ;
    private double[] r, z;
    private Integer J, K, nEigenfunction;
    private InputDataDto inputDataDto;
    private CrossSectionResultDataDto crossSectionResultDataDto;

    @FXML
    private CheckBox analyticalSolutionCheckBox, implicitSchemeCheckBox, crankNicolsonSchemeCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        mainPane.setStyle("-fx-background-color: #cde0cd");
    }

    private void readInitialConditions() throws NumberFormatException {
        L = Double.parseDouble(LTextField.getText());
        R = Double.parseDouble(RTextField.getText());
        n = Double.parseDouble(nTextField.getText());
        λ = Double.parseDouble(λTextField.getText());
        r = Arrays.stream(rTextField.getText().split(";")).mapToDouble(Double::parseDouble).toArray();
        z = Arrays.stream(zTextField.getText().split(";")).mapToDouble(Double::parseDouble).toArray();
        J = Integer.parseInt(JTextField.getText());
        K = Integer.parseInt(KTextField.getText());
        nEigenfunction = Integer.parseInt(nEigenfunctionTextField.getText());
        if (fixedVariableType.equals(FixedVariableType.r)) {
            inputDataDto = new InputDataDto(J, K, nEigenfunction, λ, n, L, R, r, fixedVariableType);
        } else {
            inputDataDto = new InputDataDto(J, K, nEigenfunction, λ, n, L, R, z, fixedVariableType);
        }
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
            controller.setInputDataDto(inputDataDto);
            controller.getStage().show();
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
        }
    }

    @FXML
    public void plotRConst() {
        fixedVariableType = FixedVariableType.r;
        plot();
    }

    @FXML
    public void plotZConst() {
        fixedVariableType = FixedVariableType.z;
        plot();
    }

    public void plot() {
        try {
            readInitialConditions();
            PlotController controller = (PlotController) getController();
            controller.setInputDataDto(inputDataDto);
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
                    controller.setSeries(function);
                    isFirst = false;
                } else {
                    controller.addSeries(function);
                }
            }
            controller.getStage().show();
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
        } catch (NoGraphsToPlotException ex) {
            WarningWindows.showWarning("К построению не выбран ни один из графиков");
        }
    }
}
