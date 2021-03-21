package ui;

import dto.InputDataDto;
import dto.CrossSectionResultDataDto;
import exceptions.NoGraphsToPlotException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import method.Calculator;
import method.impl.AnalyticalMethod;
import tabulatedFunctions.TabulatedFunction;
import ui.warnings.WarningWindows;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InitController extends AbstractParentController implements Initializable, InputDataDtoHolder {
    public TextField uImplicitSchemeTextField;
    public TextField uCrankNicolsonSchemeTextField;
    public TextField uAnalyticalSolutionTextField;
    @FXML
    AnchorPane mainPane;
    @FXML
    private TextField LTextField, RTextField, nTextField, λTextField, JTextField, KTextField, rTextField, zTextField, nEigenfunctionTextField;
    private double L, R, n, λ, r, z;
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
        zTextField.setText("5");
        nTextField.setText("1.4");
        λTextField.setText("1");
        nEigenfunctionTextField.setText("1");
//        LTextField.setText("15");
//        RTextField.setText("10");
//        zTextField.setText("15");
//        nTextField.setText("1");
//        λTextField.setText("2");
        JTextField.setText("10");
        KTextField.setText("10");
        mainPane.setStyle("-fx-background-color: #cde0cd");
    }

    private void readInitialConditions() throws NumberFormatException {
        L = Double.parseDouble(LTextField.getText());
        R = Double.parseDouble(RTextField.getText());
        n = Double.parseDouble(nTextField.getText());
        λ = Double.parseDouble(λTextField.getText());
        z = Double.parseDouble(zTextField.getText());
        J = Integer.parseInt(JTextField.getText());
        K = Integer.parseInt(KTextField.getText());
        nEigenfunction = Integer.parseInt(nEigenfunctionTextField.getText());
        inputDataDto = new InputDataDto("", J, K, nEigenfunction, λ, n, L, R, z);
    }

    @FXML
    private void calculate() {
        try {
            readInitialConditions();
            r = Double.parseDouble(rTextField.getText());
            crossSectionResultDataDto = Calculator.crossSectionCalculate(inputDataDto);
            uAnalyticalSolutionTextField.setText(crossSectionResultDataDto.getAnalyticalSolution().apply(r).abs() + "");
            uImplicitSchemeTextField.setText(crossSectionResultDataDto.getImplicitSchemeSolution().apply(r).abs() + "");
            uCrankNicolsonSchemeTextField.setText(crossSectionResultDataDto.getCrankNicolsonSchemeSolution().apply(r).abs() + "");
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
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
    public void plot() {
        try {
            readInitialConditions();
            PlotController controller = (PlotController) getController();
            crossSectionResultDataDto = Calculator.crossSectionCalculate(inputDataDto);
            List<TabulatedFunction> functions = new ArrayList<>();
            if (analyticalSolutionCheckBox.isSelected()) {
                functions.add(crossSectionResultDataDto.getAnalyticalSolution());
            }
            if (implicitSchemeCheckBox.isSelected()) {
                functions.add(crossSectionResultDataDto.getImplicitSchemeSolution());
            }
            if (crankNicolsonSchemeCheckBox.isSelected()) {
                functions.add(crossSectionResultDataDto.getCrankNicolsonSchemeSolution());
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

    @Override
    public InputDataDto getInputDataDto() {
        return inputDataDto;
    }

    @Override
    public void setInputDataDto(InputDataDto inputDataDto) {
        this.inputDataDto = inputDataDto;
    }
}
