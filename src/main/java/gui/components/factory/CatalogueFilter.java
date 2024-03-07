package main.java.gui.components.factory;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import main.java.gui.components.buttons.CustomButton;
import main.java.gui.interfaces.FilterAction;
import main.java.model.DroneBase;
import main.java.util.Constants;
import main.java.util.Logs;

import java.util.Arrays;
import java.util.List;

/**
 * Factory class for creating and managing filters for a drone catalogue.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #createFilter() createFilter}: Create a filter interface with specified stage.</li>
 *     <li>{@link #executeQuery(String, String, String, String, String) executeQuery}: Execute a filter query based on provided parameters.</li>
 *     <li>{@link #resetQuery() resetQuery}: Reset all filter fields and clear the filter results.</li>
 *     <li>{@link #clearFields() clearFields}: Clear all filter fields.</li>
 *     <li>{@link #applyCssClassToFilters(List, String) applyCssClassToFilters}: Apply a CSS class to a list of filter elements.</li>
 *     <li>{@link #pressEnter(TextField) pressEnter}: Trigger an action when the Enter key is pressed in a TextField.</li>
 *     <li>{@link #initializeTextFields() initializeTextFields}: Initialize the text fields with prompt texts.</li>
 *     <li>{@link #initTextField(TextField, String) initTextField}: Initialize a text field with a prompt text.</li>
 *     <li>{@link #executeFilterAction() executeFilterAction}: Execute the filter action based on the current filter settings.</li>
 * </ul>
 * </p>
 */

public class CatalogueFilter implements FilterAction {
    private final FlowPane contentPane;
    private final ObservableList<DroneBase> droneModels;
    private final TextField filterById = new TextField();
    private final TextField filterBySNumber = new TextField();
    private final TextField filterByManufacturer = new TextField();
    private final TextField filterByTypeName = new TextField();
    private final TextField filterByCarriageType = new TextField();

    public CatalogueFilter(FlowPane contentPane, ObservableList<DroneBase> droneModels) {
        this.contentPane = contentPane;
        this.droneModels = droneModels;
    }

    @Override
    public void executeQuery(String droneId, String serialNumber, String manufacturer, String typeName, String carriageType) {
        contentPane.getChildren().clear();
        droneModels.stream()
                .filter(drone -> droneId.isEmpty() || drone.getDroneDetails().get(Constants.DRONE_ID).toString().equals(droneId))
                .filter(drone -> serialNumber.isEmpty() || drone.getDroneDetails().get(Constants.SERIAL_NUMBER).toString().equals(serialNumber))
                .filter(drone -> manufacturer.isEmpty() || drone.getDroneTypeInfo().get(Constants.MANUFACTURER).toString().equals(manufacturer))
                .filter(drone -> typeName.isEmpty() || drone.getDroneTypeInfo().get(Constants.TYPE_NAME).toString().equals(typeName))
                .filter(drone -> carriageType.isEmpty() || drone.getDroneDetails().get(Constants.CARRIAGE_TYPE).toString().equals(carriageType))
                .map(DroneProfile::new)
                .forEach(droneProfile -> contentPane.getChildren().add(new AnchorPane(droneProfile)));
    }

    @Override
    public void resetQuery() {
        clearFields();
        executeQuery("", "", "", "", "");
    }

    @Override
    public void pressEnter(TextField textField) {
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                executeFilterAction();
            }
        });
    }

    public Node createFilter() {
        try {
            initializeTextFields();
            CustomButton searchButton = new CustomButton("Search", "search.png");
            searchButton.setOnAction(event -> executeFilterAction());
            List<Node> filters = Arrays.asList(filterById, filterBySNumber, filterByManufacturer, filterByTypeName, filterByCarriageType);
            applyCssClassToFilters(filters, "filter-input");
            CustomButton resetButton = new CustomButton("Reset", "reset.png");
            resetButton.setOnAction(event -> resetQuery());
            HBox filtersBox = new HBox(10);
            filtersBox.getStyleClass().add("filter");
            filtersBox.getChildren().addAll(resetButton, searchButton, filterById, filterBySNumber, filterByManufacturer, filterByTypeName, filterByCarriageType);
            filtersBox.setAlignment(Pos.CENTER_LEFT);
            return filtersBox;
        } catch (Exception e) {
            Logs.error("Error in FilterCatalogue.createFilter() : " + e.getMessage());
            return null;
        }
    }

    private void initializeTextFields() {
        initTextField(filterById, "Id ...");
        initTextField(filterBySNumber, "Serial Number ...");
        initTextField(filterByManufacturer, "Manufacturer ...");
        initTextField(filterByTypeName, "Type Name ...");
        initTextField(filterByCarriageType, "Carriage type ...");
    }

    private void initTextField(TextField textField, String promptText) {
        textField.setPromptText(promptText);
        pressEnter(textField);
    }

    private void executeFilterAction() {
        String droneId = filterById.getText();
        String serialNumber = filterBySNumber.getText();
        String manufacturer = filterByManufacturer.getText();
        String typeName = filterByTypeName.getText();
        String carriageType = filterByCarriageType.getText();
        executeQuery(droneId, serialNumber, manufacturer, typeName, carriageType);
    }

    public void clearFields() {
        filterById.clear();
        filterBySNumber.clear();
        filterByManufacturer.clear();
        filterByTypeName.clear();
        filterByCarriageType.clear();
    }

    public void applyCssClassToFilters(List<Node> filters, String cssClass) {
        for (Node filter : filters) {
            filter.getStyleClass().add(cssClass);
        }
    }
}