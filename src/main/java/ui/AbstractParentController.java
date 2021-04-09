package ui;

import javafx.stage.Stage;
import ui.plot.PlotController;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractParentController implements aWindow {
    protected final Map<String, aWindow> controllerMap = new HashMap<>();
    protected Stage stage;

    protected void open(boolean isResizable, StackTraceElement stackTraceElement) {
        Stage stage = getController(stackTraceElement.getMethodName()).getStage();
        stage.setResizable(isResizable);
        stage.show();
    }

    protected void open(boolean isResizable) {
        open(isResizable, Thread.currentThread().getStackTrace()[2]);
    }

    protected aWindow getController() {
        return getController(Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    protected aWindow getController(String path) {
        return controllerMap.get(path + ".fxml");
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
}
