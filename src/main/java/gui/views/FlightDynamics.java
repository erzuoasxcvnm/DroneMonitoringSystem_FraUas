package main.java.gui.views;

import eu.hansolo.tilesfx.Tile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.gui.components.buttons.CustomButton;
import main.java.gui.components.factory.ButtonBar;
import main.java.gui.components.gauges.*;
import main.java.gui.controllers.DataRefresher;
import main.java.gui.controllers.DroneStatus;
import main.java.gui.controllers.ScreenshotService;
import main.java.model.DroneBase;
import main.java.util.Constants;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * View class representing a tab for displaying flight dynamics information.
 * <p>
 * This class extends Tab and provides methods to initialize the user interface for the flight dynamics tab.
 * </p>
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #FlightDynamics(String, Stage, ButtonBar, ScreenshotService, DataRefresher, ObservableList) FlightDynamics constructor}:
 *         Initializes the flight dynamics tab with the specified title, stage, button bar factory, screenshot service, data refresher, and drone models.</li>
 *     <li>{@link #initializeUI(String, ObservableList) initializeUI}:
 *         Initializes the user interface components for the flight dynamics tab.</li>
 *     <li>{@link #setupFilterField() setupFilterField}:
 *         Sets up the filter text field for filtering drone IDs.</li>
 *     <li>{@link #setupLeftPane(Node...) setupLeftPane}:
 *         Sets up the left pane containing the filter field and drone ID list view.</li>
 *     <li>{@link #setupDroneDetailsPane() setupDroneDetailsPane}:
 *         Sets up the scroll pane for displaying drone details.</li>
 *     <li>{@link #setupRightPane(Node) setupRightPane}:
 *         Sets up the right pane containing the drone details pane.</li>
 *     <li>{@link #setupContentPane(Node...) setupContentPane}:
 *         Sets up the content pane containing the left and right panes as split panes.</li>
 *     <li>{@link #setupMainVBox(Node...) setupMainVBox}:
 *         Sets up the main VBox containing the button bar and content pane.</li>
 *     <li>{@link #setupButtonBar() setupButtonBar}:
 *         Sets up the button bar with refresh, screenshot, and quit buttons.</li>
 *     <li>{@link #setupButton(String, String, EventHandler) setupButton}:
 *         Sets up a custom button with the specified text, icon, and event handler.</li>
 *     <li>{@link #setupListView(ObservableList, TextField) setupListView}:
 *         Sets up the list view for displaying drone IDs.</li>
 *     <li>{@link #resetDroneData(String) resetDroneData}:
 *         Clears the drone data container and sets a default message.</li>
 *     <li>{@link #setDroneData(DroneBase) setDroneData}:
 *         Sets the drone data in the drone data container.</li>
 *     <li>{@link #mapAndAddGauges(GridPane, Map) mapAndAddGauges}:
 *         Maps and adds the drone gauges to the grid pane.</li>
 *     <li>{@link #getSpeedLevel(DroneBase) getSpeedLevel}:
 *         Gets the speed level gauge for the selected drone.</li>
 *     <li>{@link #getAlignmentGauge(DroneBase) getAlignmentGauge}:
 *         Gets the alignment gauge for the selected drone.</li>
 *     <li>{@link #getBatteryLevel(DroneBase) getBatteryLevel}:
 *         Gets the battery level gauge for the selected drone.</li>
 *     <li>{@link #calculateBatteryLevel(DroneBase) calculateBatteryLevel}:
 *         Calculates the battery level percentage for the selected drone.</li>
 *     <li>{@link #getTimeStampGauge(DroneBase) getTimeStampGauge}:
 *         Gets the time stamp gauge for the selected drone.</li>
 *     <li>{@link #getLastSeenGauge(DroneBase) getLastSeenGauge}:
 *         Gets the last seen gauge for the selected drone.</li>
 *     <li>{@link #getLatitudeGauge(DroneBase) getLatitudeGauge}:
 *         Gets the latitude gauge for the selected drone.</li>
 *     <li>{@link #getLongitudeGauge(DroneBase) getLongitudeGauge}:
 *         Gets the longitude gauge for the selected drone.</li>
 *     <li>{@link #getStatusGauge(DroneBase) getStatusGauge}:
 *         Gets the status gauge for the selected drone.</li>
 * </ul>
 * </p>
 */


public class FlightDynamics extends Tab {
    private final ButtonBar buttonBarFactory;
    private final ScreenshotService screenshotService;
    private final DataRefresher dataRefresher;
    private final Stage primaryStage;
    private GridPane droneDataContainer;

    public FlightDynamics(String tabTitle, Stage primaryStage, ButtonBar buttonBarFactory,
                          ScreenshotService screenshotService, DataRefresher dataRefresher,
                          ObservableList<DroneBase> droneModels) {
        this.buttonBarFactory = buttonBarFactory;
        this.screenshotService = screenshotService;
        this.dataRefresher = dataRefresher;
        this.primaryStage = primaryStage;
        initializeUI(tabTitle, droneModels);
    }

    private void initializeUI(String tabTitle, ObservableList<DroneBase> droneModels) {
        setText(tabTitle);
        getStyleClass().add("tabs");

        TextField filterField = setupFilterField();
        ScrollPane droneDetailsPane = setupDroneDetailsPane();
        ListView<String> listView = setupListView(droneModels, filterField);
        VBox leftPane = setupLeftPane(filterField, listView);
        VBox rightPane = setupRightPane(droneDetailsPane);
        SplitPane contentPane = setupContentPane(leftPane, rightPane);
        Node buttonBar = setupButtonBar();

        VBox mainVBox = setupMainVBox(buttonBar, contentPane);

        setContent(mainVBox);
        setClosable(false);
    }

    private TextField setupFilterField() {
        TextField filterField = new TextField();
        filterField.setPromptText("drone Id ...");
        filterField.getStyleClass().add("filter-input");
        return filterField;
    }

    private VBox setupLeftPane(Node... children) {
        VBox leftPane = new VBox(children);
        VBox.setVgrow(leftPane.getChildren().get(1), Priority.ALWAYS);
        leftPane.getStyleClass().add("tab-content");
        return leftPane;
    }

    private ScrollPane setupDroneDetailsPane() {
        droneDataContainer = new GridPane();
        droneDataContainer.getStyleClass().add("tab-content");
        ScrollPane scrollPane = new ScrollPane(droneDataContainer);
        scrollPane.setFitToWidth(true);
        GridPane.setHgrow(droneDataContainer, Priority.ALWAYS);
        return scrollPane;
    }

    private VBox setupRightPane(Node child) {
        VBox rightPane = new VBox(child);
        VBox.setVgrow(rightPane, Priority.ALWAYS);
        rightPane.getStyleClass().add("tab-content");
        return rightPane;
    }


    private SplitPane setupContentPane(Node... items) {
        SplitPane contentPane = new SplitPane(items);
        contentPane.setDividerPositions(0.2);
        contentPane.getDividers().get(0).positionProperty().addListener(
                (observable, oldValue, newValue) -> contentPane.setDividerPosition(0, 0.2)
        );
        contentPane.getStyleClass().add("tab-content");
        return contentPane;
    }

    private VBox setupMainVBox(Node... children) {
        VBox mainVBox = new VBox(children);
        VBox.setVgrow(mainVBox.getChildren().get(1), Priority.ALWAYS);
        return mainVBox;
    }

    private Node setupButtonBar() {
        CustomButton refreshButton = setupButton("Refresh Data", "refresh.png", event -> dataRefresher.refreshData());
        CustomButton screenshotButton = setupButton("Take Screenshot", "screenshot.png", event -> screenshotService.takeScreenshot(primaryStage));
        CustomButton quitButton = setupButton("Close Program", "power.png", event -> primaryStage.close());
        return buttonBarFactory.createButtonBar(refreshButton, screenshotButton, quitButton);
    }

    private CustomButton setupButton(String text, String icon, EventHandler<ActionEvent> eventHandler) {
        CustomButton button = new CustomButton(text, icon);
        button.setOnAction(eventHandler);
        return button;
    }

    private ListView<String> setupListView(ObservableList<DroneBase> droneModels, TextField filterField) {
        ListView<String> listView = new ListView<>();
        ObservableList<String> ids = FXCollections.observableArrayList(droneModels.stream()
                .map(drone -> drone.getDroneDetails().get(Constants.DRONE_ID).toString())
                .collect(Collectors.toList()));

        FilteredList<String> filteredIds = new FilteredList<>(ids, s -> true);
        filterField.textProperty().addListener(obs -> {
            String filter = filterField.getText();
            if (filter == null || filter.isEmpty()) {
                filteredIds.setPredicate(s -> true);
            } else {
                filteredIds.setPredicate(s -> s.contains(filter));
            }
            if (listView.getSelectionModel().getSelectedItem() == null || !listView.getSelectionModel().getSelectedItem().contains(Objects.requireNonNull(filter))) {
                listView.getSelectionModel().selectFirst();
            }
        });

        listView.setItems(filteredIds);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                DroneBase selectedDrone = droneModels.stream()
                        .filter(drone -> drone.getDroneDetails().get(Constants.DRONE_ID).toString().equals(newValue))
                        .findFirst().orElse(null);
                if (selectedDrone != null) {
                    setDroneData(selectedDrone);
                } else {
                    resetDroneData("No drone information available");
                }
            } else {
                resetDroneData("No data to display, please refine your query");
            }
        });
        listView.getSelectionModel().selectFirst();
        listView.getStyleClass().add("drone-id-listview");
        return listView;
    }

    private void resetDroneData(String defaultMessage) {
        droneDataContainer.getChildren().clear();
        Label messageLabel = new Label(defaultMessage);
        messageLabel.getStyleClass().add("message");
        droneDataContainer.getChildren().add(messageLabel);
    }

    private void setDroneData(DroneBase selectedDrone) {
        droneDataContainer.getChildren().clear();
        Tile speedLevel = getSpeedLevel(selectedDrone);
        Tile batteryLevel = getBatteryLevel(selectedDrone);
        Tile statusGauge = getStatusGauge(selectedDrone);
        Tile timeStampGauge = getTimeStampGauge(selectedDrone);
        Tile lastSeenGauge = getLastSeenGauge(selectedDrone);
        Tile longitudeGauge = getLongitudeGauge(selectedDrone);
        Tile latitudeGauge = getLatitudeGauge(selectedDrone);
        Tile alignmentGauge = getAlignmentGauge(selectedDrone);

        Map<String, Tile> droneGauges = new LinkedHashMap<>();
        droneGauges.put("atTime", new ClockGauge().getClockTile());
        droneGauges.put("statusGauge", statusGauge);
        droneGauges.put("batteryLevel", batteryLevel);
        droneGauges.put("speedLevel", speedLevel);
        droneGauges.put("alignmentGauge", alignmentGauge);
        droneGauges.put("timeStampGauge", timeStampGauge);
        droneGauges.put("lastSeenGauge", lastSeenGauge);
        droneGauges.put("longitudeGauge", longitudeGauge);
        droneGauges.put("latitudeGauge", latitudeGauge);

        mapAndAddGauges(droneDataContainer, droneGauges);
    }

    private void mapAndAddGauges(GridPane container, Map<String, Tile> tiles) {
        int index = 0;
        for (Tile tile : tiles.values()) {
            int row = index / 5;
            int column = index % 5;
            container.add(tile, column, row);
            index++;
        }
    }

    private Tile getSpeedLevel(DroneBase selectedDrone) {
        int speedValue = (int) selectedDrone.getDroneLatestData().get(Constants.SPEED);
        return new SpeedGauge(speedValue).getSpeedTile();
    }

    private Tile getAlignmentGauge(DroneBase selectedDrone) {
        String yawValue = (String) selectedDrone.getDroneLatestData().get(Constants.ALIGN_YAW);
        String rollValue = (String) selectedDrone.getDroneLatestData().get(Constants.ALIGN_ROLL);
        String pitchValue = (String) selectedDrone.getDroneLatestData().get(Constants.ALIGN_PITCH);
        return new AlignmentGauge(yawValue, rollValue, pitchValue).getAlignmentTile();
    }

    private Tile getBatteryLevel(DroneBase selectedDrone) {
        return new BatteryCapacityGauge(calculateBatteryLevel(selectedDrone)).getBatteryTile();
    }

    private int calculateBatteryLevel(DroneBase drone) {
        int batteryCapacity = (int) drone.getDroneTypeInfo().get(Constants.BATTERY_CAPACITY);
        int currentLevel = (int) drone.getDroneLatestData().get(Constants.BATTERY_STATUS);
        return batteryCapacity != 0 ? (currentLevel * 100 / batteryCapacity) : 0;
    }

    private Tile getTimeStampGauge(DroneBase selectedDrone) {
        String timeStampValue = (String) selectedDrone.getDroneLatestData().get(Constants.TIMESTAMP);
        return new CharacterGauge("Time Stamp", timeStampValue, true).getCharacterTile();
    }

    private Tile getLastSeenGauge(DroneBase selectedDrone) {
        String lastSeenValue = (String) selectedDrone.getDroneLatestData().get(Constants.LAST_SEEN);
        return new CharacterGauge("Last Seen", lastSeenValue, true).getCharacterTile();
    }

    private Tile getLatitudeGauge(DroneBase selectedDrone) {
        String latitudeValue = (String) selectedDrone.getDroneLatestData().get(Constants.LATITUDE);
        return new CharacterGauge("Latitude", latitudeValue, false).getCharacterTile();
    }

    private Tile getLongitudeGauge(DroneBase selectedDrone) {
        String longitudeValue = (String) selectedDrone.getDroneLatestData().get(Constants.LONGITUDE);
        return new CharacterGauge("Longitude", longitudeValue, false).getCharacterTile();
    }

    private Tile getStatusGauge(DroneBase selectedDrone) {
        String statusValue = (String) selectedDrone.getDroneLatestData().get(Constants.STATUS);
        return switch (statusValue) {
            case Constants.STATUS_ON -> new StatusGauge(DroneStatus.State.ON).getStatusTile();
            case Constants.STATUS_OF -> new StatusGauge(DroneStatus.State.OFF).getStatusTile();
            case Constants.STATUS_IS -> new StatusGauge(DroneStatus.State.ISSUE).getStatusTile();
            default -> new StatusGauge(DroneStatus.State.DEFAULT).getStatusTile();
        };
    }
}