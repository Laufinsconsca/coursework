package ui;

import enums.Item;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ui.warnings.WarningWindows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.StreamSupport;

public class Initializer {
    private static final String FXML_PATH = "fxml/";
    private static final Class<AutoInitializableController> annotationClass = AutoInitializableController.class;

    public static void initializeWindowControllers(Class<? extends aWindow> parentController, Stage ownerStage, Map<String, aWindow> controllerMap) {
        StreamSupport.stream(ClassIndex.getAnnotated(annotationClass).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(annotationClass).type() == Item.CONTROLLER)
                .filter(f -> f.getDeclaredAnnotation(annotationClass).parentController().equals(parentController.getSimpleName()))
                .forEach(clazz -> controllerMap.put(clazz.getDeclaredAnnotation(annotationClass).pathFXML(),
                        initializeWindowController(clazz, ownerStage)));
    }

    private static aWindow initializeWindowController(Class<?> clazz, Stage ownerStage) {
        aWindow controller = null;
        try {
            controller = (aWindow) clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        String path = FXML_PATH + controller.getClass().getDeclaredAnnotation(annotationClass).pathFXML();
        controller = initializeModalityWindow(path, controller);
        controller.getStage().initOwner(ownerStage);
        controller.getStage().setTitle(controller.getClass().getDeclaredAnnotation(annotationClass).name());
        return controller;
    }

    private static <T extends aWindow> T initializeModalityWindow(String pathFXML, T modalityWindow) {
        FXMLLoader loader;
        Parent parent;
        Stage stage = new Stage();
        try {
            loader = new FXMLLoader(modalityWindow.getClass().getClassLoader().getResource(pathFXML));
            parent = loader.load();
            modalityWindow = loader.getController();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            modalityWindow.setStage(stage);
        } catch (IOException e) {
            WarningWindows.showError(e);
        }
        return modalityWindow;
    }
}