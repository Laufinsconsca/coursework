package ui;

import complex.Complex;
import dto.CalculationResultDto;
import dto.InputDataDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import method.MethodHelper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class InitController implements Initializable, aWindow {
    @FXML
    AnchorPane mainPane;
    private Stage stage;
    private double L, R, n, λ, r, z;
    private Complex u;
    private Integer J, K;
    private InputDataDto inputDataDto;
    private CalculationResultDto calculationResultDto;
    @FXML
    private TextField LTextField, RTextField, nTextField, λTextField, JTextField, KTextField, rTextField, uTextField, zTextField;
    private final Map<String, aWindow> controllerMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Initializer(stage).initializeWindowControllers(controllerMap);
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
        aWindow controller = controllerMap.get(path + ".fxml");
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
    private void uTheory() {
        try {
            readInitialConditions();
            r = Double.parseDouble(rTextField.getText());
            calculationResultDto = MethodHelper.doCalculation(inputDataDto);
            uTextField.setText(calculationResultDto.getAnalyticalSolution().apply(r).abs() + "");
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
            calculationResultDto = MethodHelper.doCalculation(inputDataDto);
            controller.setSeries(calculationResultDto.getAnalyticalSolution(), "аналитическое\nрешение");
            controller.addSeries(calculationResultDto.getSolutionByTheCrankNicholsonScheme(), "метод\nКранка-Николсона");
            controller.getStage().show();
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
        }
    }
}
