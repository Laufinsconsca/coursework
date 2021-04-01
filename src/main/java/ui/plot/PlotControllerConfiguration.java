package ui.plot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlotControllerConfiguration {
    private final String xVariableName;
    private final String yVariableName;
    private final String xAxisName;
    private final String yAxisName;
}
