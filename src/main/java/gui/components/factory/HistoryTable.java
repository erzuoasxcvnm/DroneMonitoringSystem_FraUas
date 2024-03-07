package main.java.gui.components.factory;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Pair;
import main.java.util.Constants;
import main.java.util.Logs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Factory class for creating dynamic tables.
 * <p>
 * This class extends TableView and provides functionality to dynamically generate table columns
 * based on provided data keys. It is used to display dynamic data in a tabular format.
 * </p>
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #HistoryTable() HistoryTable constructor}: Initialize a new instance of HistoryTable.</li>
 *     <li>{@link #createTableColumn(String, String) createTableColumn}: Create a TableColumn based on provided title and data key.</li>
 * </ul>
 * </p>
 * <p>
 * </p>
 */
public final class HistoryTable extends TableView<Map<String, Object>> {
    public HistoryTable() {
        final var columnsData = Arrays.asList(
                new Pair<>("Timestamp", Constants.TIMESTAMP),
                new Pair<>("Status", Constants.STATUS),
                new Pair<>("Speed (km/h)", Constants.SPEED),
                new Pair<>("Roll", Constants.ALIGN_ROLL),
                new Pair<>("Pitch", Constants.ALIGN_PITCH),
                new Pair<>("Yaw", Constants.ALIGN_YAW),
                new Pair<>("Longitude", Constants.LONGITUDE),
                new Pair<>("Latitude", Constants.LATITUDE),
                new Pair<>("Battery (mAh)", Constants.BATTERY_STATUS),
                new Pair<>("Last seen", Constants.LAST_SEEN)
        );

        List<TableColumn<Map<String, Object>, String>> columnList = columnsData.stream()
                .map(data -> createTableColumn(data.getKey(), data.getValue()))
                .collect(Collectors.toList());

        getColumns().setAll(columnList);

        getColumns().setAll(columnList);
        this.setEditable(false);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.getStyleClass().add("table");
    }

    private TableColumn<Map<String, Object>, String> createTableColumn(String title, String dataKey) {
        try {
            TableColumn<Map<String, Object>, String> column = new TableColumn<>(title);
            column.setCellValueFactory(cellData -> {
                if (cellData.getValue().containsKey(dataKey))
                    return new SimpleStringProperty(String.valueOf(cellData.getValue().get(dataKey)));
                else
                    return new SimpleStringProperty("n/a");
            });
            column.setCellFactory(TextFieldTableCell.forTableColumn());

            if (title.equalsIgnoreCase("Timestamp") || title.equalsIgnoreCase("Last seen")) {
                column.setPrefWidth(200);
            }
            return column;
        } catch (Exception e) {
            Logs.error("Error occurred in DynamicsTable.createTableColumn() : " + e.getMessage());
            return null;
        }

    }
}