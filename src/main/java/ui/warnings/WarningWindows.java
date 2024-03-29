package ui.warnings;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

public class WarningWindows {
    public static void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText(e.getMessage());
        VBox dialogPaneContent = new VBox();
        Label label = new Label("Stack trace:");
        String stackTrace = getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);
        dialogPaneContent.getChildren().addAll(label, textArea);
        alert.getDialogPane().setContent(dialogPaneContent);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Objects.requireNonNull(WarningWindows.class.getResourceAsStream("/computer.png"))));
        alert.showAndWait();
    }

    private static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private static void doOperation(String message, Alert.AlertType messageType) {
        Alert alert = new Alert(messageType);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Objects.requireNonNull(WarningWindows.class.getResourceAsStream("/computer.png"))));
        alert.showAndWait();
    }

    public static void showWarning(String message) {
        doOperation(message, Alert.AlertType.WARNING);
    }

    public static void showNotification(String message) {
        doOperation(message, Alert.AlertType.INFORMATION);
    }
}
