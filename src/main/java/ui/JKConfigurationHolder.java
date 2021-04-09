package ui;

import javafx.collections.ObservableList;
import ui.tableRows.JKTableRow;

import java.util.List;

public interface JKConfigurationHolder {
    void setJKConfiguration(List<ObservableList<JKTableRow>> JKConfiguration);
}
