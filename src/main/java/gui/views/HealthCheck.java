package main.java.gui.views;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.gui.components.buttons.CustomButton;
import main.java.gui.components.factory.ButtonBar;
import main.java.gui.components.factory.DronePanel;
import main.java.gui.components.other.ScrollBar;
import main.java.gui.controllers.DataRefresher;
import main.java.gui.controllers.ScreenshotService;
import main.java.model.DroneBase;
import main.java.util.Constants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The {@code HealthCheck} class extends from {@code Tab} and forms the primary user interface.
 * This class represents a tab for displaying health check information of drones.
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #HealthCheck(String, Stage, DronePanel, ButtonBar, ScreenshotService, DataRefresher, ObservableList) HealthCheck constructor}:
 *         Initializes the health check tab with the specified title, primary stage, drone panel factory,
 *         button bar factory, screenshot service, data refresher, and drone models.</li>
 *     <li>{@link #initializeUI(String, ObservableList) initializeUI}:
 *         Initializes the user interface components for the health check tab.</li>
 *     <li>{@link #initializeTopContainer() initializeTopContainer}:
 *         Initializes the top container including buttons.</li>
 *     <li>{@link #initializeScrollBar(ObservableList) initializeScrollBar}:
 *         Initializes the scroll bar with drone panels.</li>
 *     <li>{@link #createDronePanel(DroneBase) createDronePanel}:
 *         Creates a drone panel for a specific drone.</li>
 *     <li>{@link #createButton(String, String, javafx.event.EventHandler) createButton}:
 *         Creates a custom button with the specified text, icon, and action.</li>
 * </ul>
 * </p>
 */

public class HealthCheck extends Tab {
    private final Stage primaryStage;
    private final DataRefresher dataRefresher;
    private final ScreenshotService screenshotService;
    private final ButtonBar buttonBarFactory;
    private final DronePanel dronePanelFactory;

    public HealthCheck(String tabTitle, Stage primaryStage, DronePanel dronePanelFactory,
                       ButtonBar buttonBarFactory, ScreenshotService screenshotService,
                       DataRefresher dataRefresher, ObservableList<DroneBase> droneModels) {

        this.primaryStage = primaryStage;
        this.dataRefresher = dataRefresher;
        this.screenshotService = screenshotService;
        this.buttonBarFactory = buttonBarFactory;
        this.dronePanelFactory = dronePanelFactory;

        initializeUI(tabTitle, droneModels);
    }

    private void initializeUI(String tabTitle, ObservableList<DroneBase> droneModels) {
        setText(tabTitle);
        getStyleClass().add("tabs");

        VBox topContainer = initializeTopContainer();
        ScrollBar scrollBar = initializeScrollBar(droneModels);

        setContent(new VBox(topContainer, scrollBar));
        setClosable(false);
    }

    private VBox initializeTopContainer() {
        CustomButton refreshButton = createButton("Refresh Data", "refresh.png", event -> dataRefresher.refreshData());
        CustomButton screenshotButton = createButton("Take Screenshot", "screenshot.png", event -> screenshotService.takeScreenshot(primaryStage));
        CustomButton quitButton = createButton("Close Program", "power.png", event -> primaryStage.close());
        Node buttonBar = buttonBarFactory.createButtonBar(refreshButton, screenshotButton, quitButton);
        return new VBox(buttonBar);
    }

    private ScrollBar initializeScrollBar(ObservableList<DroneBase> droneModels) {
        List<Pane> dronePanels = new ArrayList<>();
        droneModels.sort(Comparator.comparing(d -> (int) d.getDroneDetails().get(Constants.DRONE_ID)));
        for (DroneBase drone : droneModels) {
            dronePanels.add(createDronePanel(drone));
        }
        ScrollBar scrollBar = new ScrollBar();
        FlowPane contentPane = new FlowPane();
        contentPane.getStyleClass().add("tab-content");
        contentPane.getChildren().addAll(dronePanels);
        scrollBar.setContent(contentPane);
        return scrollBar;
    }

    private Pane createDronePanel(DroneBase drone) {
        String droneId = String.valueOf(drone.getDroneDetails().get(Constants.DRONE_ID));
        String droneStatus = String.valueOf(drone.getDroneLatestData().get(Constants.STATUS));
        return dronePanelFactory.createDronePanel(droneId, droneStatus);
    }

    private CustomButton createButton(String text, String iconName, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        CustomButton button = new CustomButton(text, iconName);
        button.setOnAction(action);
        return button;
    }
}