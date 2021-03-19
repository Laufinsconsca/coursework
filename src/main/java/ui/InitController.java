package ui;

import complex.Complex;
import dto.ResultDataDto;
import dto.InputDataDto;
import exceptions.NoGraphsToPlotException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import method.Calculator;
import tabulatedFunctions.TabulatedFunction;

import java.net.URL;
import java.util.*;

public class InitController implements Initializable, aWindow {
    private final Map<String, aWindow> controllerMap = new HashMap<>();
    @FXML
    private TextField LTextField, RTextField, nTextField, λTextField, JTextField, KTextField, rTextField, zTextField;
    public TextField uImplicitSchemeTextField;
    public TextField uCrankNicolsonSchemeTextField;
    public TextField uAnalyticalSolutionTextField;
    @FXML
    AnchorPane mainPane;
    private Stage stage;
    private double L, R, n, λ, r, z;
    private Complex u;
    private Integer J, K;
    private InputDataDto inputDataDto;
    private ResultDataDto resultDataDto;
    @FXML
    private CheckBox analyticalSolutionCheckBox, implicitSchemeCheckBox, crankNicolsonSchemeCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Initializer(stage).initializeWindowControllers(controllerMap);
        LTextField.setText("5");
        RTextField.setText("6");
        zTextField.setText("5");
        nTextField.setText("1.4");
        λTextField.setText("1");
        JTextField.setText("10");
        KTextField.setText("10");
        mainPane.setStyle("-fx-background-color: #cde0cd");
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        stage.setResizable(false);
        this.stage = stage;
    }

    private void open(boolean isResizable, StackTraceElement stackTraceElement) {
        Stage stage = getController(stackTraceElement.getMethodName()).getStage();
        stage.setResizable(isResizable);
        stage.show();
    }

    private void open(boolean isResizable) {
        open(isResizable, Thread.currentThread().getStackTrace()[2]);
    }

    private aWindow getController() {
        return getController(Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    private aWindow getController(String path) {
        return controllerMap.get(path + ".fxml");
    }

    private void readInitialConditions() throws NumberFormatException {
        L = Double.parseDouble(LTextField.getText());
        R = Double.parseDouble(RTextField.getText());
        n = Double.parseDouble(nTextField.getText());
        λ = Double.parseDouble(λTextField.getText());
        z = Double.parseDouble(zTextField.getText());
        J = Integer.parseInt(JTextField.getText());
        K = Integer.parseInt(KTextField.getText());
        inputDataDto = new InputDataDto("", J, K, λ, n, L, R, z);
    }

    @FXML
    private void calculate() {
        try {
            readInitialConditions();
            r = Double.parseDouble(rTextField.getText());
            resultDataDto = Calculator.doCalculation(inputDataDto);
            uAnalyticalSolutionTextField.setText(resultDataDto.getAnalyticalSolution().apply(r).abs() + "");
            uImplicitSchemeTextField.setText(resultDataDto.getImplicitSchemeSolution().apply(r).abs() + "");
            uCrankNicolsonSchemeTextField.setText(resultDataDto.getCrankNicolsonSchemeSolution().apply(r).abs() + "");
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
        }
    }

    private double uExp(double r, double z) {
        return 0;
    }

    @FXML
    public void plot() {
        try {
            readInitialConditions();
            PlotController controller = (PlotController) getController();
            resultDataDto = Calculator.doCalculation(inputDataDto);
            List<TabulatedFunction> functions = new ArrayList<>();
            if (analyticalSolutionCheckBox.isSelected()) {
                functions.add(resultDataDto.getAnalyticalSolution());
            }
            if (implicitSchemeCheckBox.isSelected()) {
                functions.add(resultDataDto.getImplicitSchemeSolution());
            }
            if (crankNicolsonSchemeCheckBox.isSelected()) {
                functions.add(resultDataDto.getCrankNicolsonSchemeSolution());
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
