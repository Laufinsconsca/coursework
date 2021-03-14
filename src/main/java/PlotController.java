import com.sun.javafx.charts.Legend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@AutoInitializableController(name = "Построение графика", type = Item.CONTROLLER, pathFXML = "plot.fxml")
public class PlotController implements Initializable, aWindow {
    private Stage stage;
    @FXML
    private StackPane stackPane;
    @FXML
    private LineChart<Double, Double> lineChart;
    private double strokeWidth = 0.5;

    public static void removeLegend(LineChart<Double, Double> lineChart) {
        ((Legend) lineChart.lookup(".chart-legend")).getItems().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lineChart.setCreateSymbols(false);
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
    }

    public void addSeries(List<Point> list, String name) {
        ObservableList<XYChart.Data<Double, Double>> data = FXCollections.observableArrayList();
        for (Point point : list) {
            data.add(new XYChart.Data<>(point.x, point.y));
        }
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setData(data);
        series.setName(name);
        lineChart.getData().add(series);
        removeLegend(lineChart);
    }

    public void setSeries(List<Point> list, String name) {
        lineChart.getData().clear();
        addSeries(list, name);
    }

}
