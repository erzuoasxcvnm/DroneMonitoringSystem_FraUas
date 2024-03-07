package main.java.gui.views;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.gui.components.buttons.CustomButton;
import main.java.gui.components.factory.ButtonBar;
import main.java.gui.components.factory.HistoryTable;
import main.java.gui.controllers.DataRefresher;
import main.java.gui.controllers.ScreenshotService;
import main.java.model.DroneBase;
import main.java.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * View class representing a tab for displaying drone history information.
 * <p>
 * This class extends Tab and provides methods to initialize the user interface for the drone history tab.
 * </p>
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #DroneHistory(String, Stage, ButtonBar, ScreenshotService, DataRefresher, ObservableList) DroneHistory constructor}:
 *         Initializes the drone history tab with the specified title, stage, button bar factory, screenshot service, data refresher, and drone models.</li>
 *     <li>{@link #initializeUI(String, ObservableList) initializeUI}:
 *         Initializes the user interface components for the drone history tab.</li>
 *     <li>{@link #createDroneSearchNode(ObservableList) createDroneSearchNode}:
 *         Creates a search node containing input fields and a search button for filtering drone history.</li>
 *     <li>{@link #createDatePicker() createDatePicker}:
 *         Creates a date picker for selecting dates.</li>
 *     <li>{@link #createTimePicker() createTimePicker}:
 *         Creates a time picker for selecting times.</li>
 *     <li>{@link #createDroneIdInput() createDroneIdInput}:
 *         Creates a text field for entering drone ID.</li>
 *     <li>{@link #createSearchButton(TextField, DatePicker, TextField, ObservableList, HistoryTable) createSearchButton}:
 *         Creates a search button with an event handler to perform a search based on user input.</li>
 *     <li>{@link #searchButtonBehaviour(TextField, DatePicker, TextField, ObservableList, HistoryTable) searchButtonBehaviour}:
 *         Defines the behavior of the search button when clicked, filtering drone history based on user input.</li>
 *         Creates a label for indicating the purpose of the input field.</li>
 * </ul>
 * </p>
 */


public class DroneHistory extends Tab {
    private final HistoryTable dynamicsTable = new HistoryTable();
    private final ButtonBar buttonBarFactory;
    private final ScreenshotService screenshotService;
    private final DataRefresher dataRefresher;
    private final Stage primaryStage;

    public DroneHistory(String tabTitle, Stage primaryStage, ButtonBar buttonBarFactory,
                        ScreenshotService screenshotService, DataRefresher dataRefresher,
                        ObservableList<DroneBase> droneModels) {
        this.buttonBarFactory = buttonBarFactory;
        this.screenshotService = screenshotService;
        this.dataRefresher = dataRefresher;
        this.primaryStage = primaryStage;
        FlowPane contentPane = new FlowPane();
        contentPane.getStyleClass().add("catalogue-content");

        initializeUI(tabTitle, droneModels);
    }

    private static Stream<Map<String, Object>> getMapStream(List<Map<String, Object>> droneDynamicsInfo, String selectedDate, String selectedTime) {
        Stream<Map<String, Object>> droneInfoStream = droneDynamicsInfo.stream();

        if (!selectedDate.isBlank()) {
            droneInfoStream = droneInfoStream.filter(d -> d.get(Constants.TIMESTAMP).toString().startsWith(selectedDate));

            if (!selectedTime.isBlank()) {
                droneInfoStream = droneInfoStream.filter(d -> d.get(Constants.TIMESTAMP).toString().startsWith(selectedDate + "T" + selectedTime));
            }
        }
        return droneInfoStream;
    }

    private void initializeUI(String tabTitle, ObservableList<DroneBase> droneModels) {
        setText(tabTitle);
        getStyleClass().add("tabs");

        VBox topContainer = new VBox();
        topContainer.getChildren().add(initializeTopContainer());
        VBox searchNode = (VBox) createDroneSearchNode(droneModels);

        VBox mainVBox = new VBox(topContainer, searchNode);
        VBox.setVgrow(searchNode, Priority.ALWAYS);

        setContent(mainVBox);
        setClosable(false);
    }

    private VBox initializeTopContainer() {
        CustomButton refreshButton = createCustomButton("Refresh Data", "refresh.png", event -> dataRefresher.refreshData());
        CustomButton screenshotButton = createCustomButton("Take Screenshot", "screenshot.png", event -> screenshotService.takeScreenshot(primaryStage));
        CustomButton quitButton = createCustomButton("Close Program", "power.png", event -> primaryStage.close());
        Node buttonBar = buttonBarFactory.createButtonBar(refreshButton, screenshotButton, quitButton);
        return new VBox(buttonBar);
    }

    private CustomButton createCustomButton(String text, String iconName, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        CustomButton button = new CustomButton(text, iconName);
        button.setOnAction(action);
        return button;
    }

    private Node createDroneSearchNode(ObservableList<DroneBase> droneModels) {
        Label droneNotFound = new Label("No data to display, please refine your query");
        droneNotFound.getStyleClass().add("message");

        TextField timePicker = createTimePicker();
        TextField droneIdInput = createDroneIdInput();
        DatePicker datePicker = createDatePicker();
        dynamicsTable.setPlaceholder(droneNotFound);

        CustomButton searchButton = createSearchButton(droneIdInput, datePicker, timePicker, droneModels, dynamicsTable);
        HBox inputAndButton = new HBox(10, droneIdInput, datePicker, timePicker, searchButton);
        inputAndButton.setAlignment(Pos.CENTER);
        inputAndButton.getStyleClass().add("inputAndButton");

        VBox searchNode = new VBox(20, inputAndButton);
        VBox.setVgrow(dynamicsTable, Priority.ALWAYS);
        searchNode.getChildren().addAll(dynamicsTable);
        return searchNode;
    }

    private DatePicker createDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("YYYY-MM-DD");
        datePicker.getStyleClass().add("datePicker");
        return datePicker;
    }

    private TextField createTimePicker() {
        TextField timePicker = new TextField();
        timePicker.setPromptText("HH:MM:SS");
        timePicker.getStyleClass().add("timePicker");
        return timePicker;
    }

    private TextField createDroneIdInput() {
        TextField droneIdInput = new TextField();
        droneIdInput.getStyleClass().add("droneIdInput");
        droneIdInput.setPromptText("Drone ID");
        return droneIdInput;
    }

    private CustomButton createSearchButton(TextField droneIdInput, DatePicker datePicker, TextField timePicker, ObservableList<DroneBase> droneModels, HistoryTable dynamicsTable) {
        CustomButton searchButton = new CustomButton("Search", "search.png");
        searchButton.setOnAction(event -> searchButtonBehaviour(droneIdInput, datePicker, timePicker, droneModels, dynamicsTable));
        return searchButton;
    }

    private void searchButtonBehaviour(
            TextField droneIdInput,
            DatePicker datePicker,
            TextField timePicker,
            ObservableList<DroneBase> droneModels,
            HistoryTable dynamicsTable
    ) {
        String droneId = droneIdInput.getText();
        dynamicsTable.getItems().clear();
        String selectedDate = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        String selectedTime = timePicker.getText();

        for (DroneBase drone : droneModels) {
            if (String.valueOf(drone.getDroneDetails().get(Constants.DRONE_ID)).equals(droneId)) {
                List<Map<String, Object>> droneDynamicsInfo = drone.getDroneDynamicsInfo();
                if (droneDynamicsInfo != null) {
                    Stream<Map<String, Object>> droneInfoStream = getMapStream(droneDynamicsInfo, selectedDate, selectedTime);
                    droneInfoStream.forEach(dynamicsTable.getItems()::add);
                }
                break;
            }
        }
    }
}