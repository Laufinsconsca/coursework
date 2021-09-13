package ui.plot;

import enums.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.Style;
import javafx.css.StyleableProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.Point;
import model.tabulatedFunction.TabulatedFunction;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;
import ui.AutoInitializableController;
import ui.aWindow;
import ui.warnings.WarningWindows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

@AutoInitializableController(name = "Построение графика", type = Item.CONTROLLER, pathFXML = "plot.fxml")
public class PlotController implements Initializable, aWindow {
    private final Map<TabulatedFunction, Color> functionColorMap = new HashMap<>();
    private final double crosshairLineWidth = 0.5;
    private Stage stage;
    @FXML
    private StackPane stackPane;
    @FXML
    private LineChart<Double, Double> lineChart;
    private AnchorPane detailsWindow;
    private PlotController.DetailsPopup detailsPopup;
    private PlotControllerConfiguration configuration;
    private int numberOfRenderedGraphs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOfRenderedGraphs = 0;
        detailsWindow = new AnchorPane();
        lineChart.setCreateSymbols(false);
        bindMouseEvents(lineChart, crosshairLineWidth);
        ChartPanManager panner = new ChartPanManager(lineChart);
        panner.setMouseFilter(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY) {
                mouseEvent.consume();
            }
        });
        panner.start();
        JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(lineChart);
        JFXChartUtil.setupZooming(lineChart, mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY)
                mouseEvent.consume();
        });
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(windowEvent -> removeAllSeries());
    }

    public void addSeries(TabulatedFunction function) {
        ObservableList<XYChart.Data<Double, Double>> data = FXCollections.observableArrayList();
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        for (Point point : function) {
            data.add(new XYChart.Data<>(point.getX(), point.getU().abs()));
        }
        lineChart.getXAxis().setLabel(configuration.getXAxisName());
        lineChart.getYAxis().setLabel(configuration.getYAxisName());
        series.setData(data);
        series.setName(function.getName());
        lineChart.getData().add(series);
        functionColorMap.putIfAbsent(function, getColorFromCSS(series));
        detailsPopup.addPopupRow(function);
        numberOfRenderedGraphs++;
    }

    public void setSeries(TabulatedFunction function) {
        removeAllSeries();
        addSeries(function);
    }

    public void offerSeries(TabulatedFunction function) {
        if (numberOfRenderedGraphs == 0) {
            setSeries(function);
        } else {
            addSeries(function);
        }
    }

    public void removeAllSeries(){
        lineChart.getData().clear();
        detailsPopup.clear();
        numberOfRenderedGraphs = 0;
    }

    private Color getColorFromCSS(XYChart.Series<Double, Double> series) {
        Method[] methods = series.getClass().getClass().getDeclaredMethods();
        Method getPrivate = null;
        for (Method method : methods) {
            if (method.getName().equals("privateGetDeclaredMethods")) {
                getPrivate = method;
                break;
            }
        }
        assert getPrivate != null;
        getPrivate.setAccessible(true);
        Method getStyleMap = null;
        Color color = null;
        try {
            methods = (Method[]) getPrivate.invoke(series.getNode().getClass().getSuperclass().getSuperclass(), false);
            for (Method method : methods) {
                if (method.getName().equals("getStyleMap")) {
                    getStyleMap = method;
                    break;
                }
            }
            assert getStyleMap != null;
            getStyleMap.setAccessible(true);
            Object[] arrayOfListsOfStyles = ((ObservableMap<StyleableProperty<?>, List<Style>>) getStyleMap.invoke(series.getNode())).values().toArray();
            for (Object arrayOfListsOfStyle : arrayOfListsOfStyles) {
                Object value = ((Style) (((ArrayList<Style>) arrayOfListsOfStyle).toArray())[0]).getDeclaration().getParsedValue().getValue();
                if (value instanceof Color) {
                    color = (Color) value;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            WarningWindows.showError(e);
        }
        return color;
    }

    private void bindMouseEvents(LineChart<Double, Double> baseChart, Double crosshairLineWidth) {
        detailsPopup = new PlotController.DetailsPopup();
        stackPane.getChildren().add(detailsWindow);
        detailsWindow.getChildren().add(detailsPopup);
        detailsWindow.prefHeightProperty().bind(stackPane.heightProperty());
        detailsWindow.prefWidthProperty().bind(stackPane.widthProperty());
        detailsWindow.setMouseTransparent(true);

        stackPane.setOnMouseMoved(null);
        stackPane.setMouseTransparent(false);

        final Axis xAxis = baseChart.getXAxis();
        final Axis yAxis = baseChart.getYAxis();

        final Line xLine = new Line();
        final Line yLine = new Line();

        yLine.setFill(Color.GRAY);
        xLine.setFill(Color.GRAY);
        yLine.setStrokeWidth(crosshairLineWidth / 2);
        xLine.setStrokeWidth(crosshairLineWidth / 2);
        xLine.setVisible(false);
        yLine.setVisible(false);

        final Node chartBackground = baseChart.lookup(".chart-plot-background");
        for (Node n : chartBackground.getParent().getChildrenUnmodifiable()) {
            if (n != chartBackground && n != xAxis && n != yAxis) {
                n.setMouseTransparent(true);
            }
        }
        chartBackground.setCursor(Cursor.CROSSHAIR);
        chartBackground.setOnMouseEntered((event) -> {
            chartBackground.getOnMouseMoved().handle(event);
            detailsPopup.setVisible(true);
            xLine.setVisible(true);
            yLine.setVisible(true);
            detailsWindow.getChildren().addAll(xLine, yLine);
        });
        chartBackground.setOnMouseExited((event) -> {
            detailsPopup.setVisible(false);
            xLine.setVisible(false);
            yLine.setVisible(false);
            detailsWindow.getChildren().removeAll(xLine, yLine);
        });
        chartBackground.setOnMouseMoved(event -> {
            double x = event.getX() + chartBackground.getLayoutX();
            double y = event.getY() + chartBackground.getLayoutY();

            double a = 10;
            double b = 5;

            xLine.setStartX(a);
            xLine.setEndX(detailsWindow.getWidth() - a);
            xLine.setStartY(y + b);
            xLine.setEndY(y + b);

            yLine.setStartX(x + b);
            yLine.setEndX(x + b);
            yLine.setStartY(a);
            yLine.setEndY(detailsWindow.getHeight() - a);

            detailsPopup.showChartDescription(event);

            if (y + detailsPopup.getHeight() + a < stackPane.getHeight()) {
                AnchorPane.setTopAnchor(detailsPopup, y + a);
            } else {
                AnchorPane.setTopAnchor(detailsPopup, y - a - detailsPopup.getHeight());
            }

            if (x + detailsPopup.getWidth() + a < stackPane.getWidth()) {
                AnchorPane.setLeftAnchor(detailsPopup, x + a);
            } else {
                AnchorPane.setLeftAnchor(detailsPopup, x - a - detailsPopup.getWidth());
            }
        });
    }

    public void setConfiguration(PlotControllerConfiguration configuration) {
        this.configuration = configuration;
    }

    private class DetailsPopup extends VBox {

        private final ObservableList<TabulatedFunction> functions = FXCollections.observableArrayList();

        private DetailsPopup() {
            setStyle("-fx-border-width: 1; -fx-padding: 5 5 5 5; -fx-border-color: gray; -fx-background-color: whitesmoke;");
            setVisible(false);
        }

        public void addPopupRow(TabulatedFunction function) {
            if (!functions.contains(function)) {
                functions.add(function);
            }
        }

        public void clear() {
            functions.clear();
        }

        public void showChartDescription(MouseEvent event) {
            getChildren().clear();
            double x = lineChart.getXAxis().getValueForDisplay(event.getX());

            for (TabulatedFunction function : functions) {
                HBox popupRow = buildPopupRow(event, x, function);
                getChildren().add(popupRow);
            }
        }

        private HBox buildPopupRow(MouseEvent event, double r, TabulatedFunction function) {
            Label seriesName = new Label(function.getName());
            seriesName.setTextFill(functionColorMap.get(function));

            double u = function.apply(r).abs();

            double yValueLower = normalizeYValue(lineChart, event.getY() - 3);
            double yValueUpper = normalizeYValue(lineChart, event.getY() + 3);
            double yValueUnderMouse = lineChart.getYAxis().getValueForDisplay(event.getY());

            // make series name bold when mouse is near given chart's line
            if (isMouseNearLine(u, yValueUnderMouse, Math.abs(yValueLower - yValueUpper))) {
                seriesName.setStyle("-fx-font-weight: bold");
            }

            return new HBox(10, seriesName, new Label(configuration.getXVariableName() + ": [" + r + "]\n" + configuration.getYVariableName() + ": [" + u + "]"));
        }

        private double normalizeYValue(LineChart<Double, Double> lineChart, double value) {
            return lineChart.getYAxis().getValueForDisplay(value);
        }

        private boolean isMouseNearLine(Double realYValue, Double yValueUnderMouse, Double tolerance) {
            return (Math.abs(yValueUnderMouse - realYValue) < tolerance);
        }
    }

}
