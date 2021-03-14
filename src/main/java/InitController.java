import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Point;

import java.net.URL;
import java.util.*;

public class InitController implements Initializable, aWindow {
    private Stage stage;
    private double L, R, n, k, r, z;
    private Complex u;
    private long N;
    @FXML
    private TextField LTextField, RTextField, nTextField, kTextField, NTextField, xTextField, uTextField, zTextField;
    private Map<String, aWindow> controllerMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Initializer(stage).initializeWindowControllers(controllerMap);
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
        k = Double.parseDouble(kTextField.getText());
        z = Double.parseDouble(zTextField.getText());
        N = Long.parseLong(NTextField.getText());
    }

    @FXML
    private void uTheory() {
        try {
            readInitialConditions();
            r = Double.parseDouble(xTextField.getText());
            uTextField.setText(uExp(r, z) + "");
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
            //todo
            //controller.setSeries(getSeries(z), "тестовый график");
            controller.getStage().show();
        } catch (NumberFormatException e) {
            WarningWindows.showWarning("Ошибка ввода начальных условий");
        }
    }

    private List<Point> getSeries(double t) {
        List<Point> list = new ArrayList<>();
        for (double i = 0; i < L; i += L/N) {
            //list.add(new Point(i, uExp(i, t)));
        }
        return list;
    }
}
