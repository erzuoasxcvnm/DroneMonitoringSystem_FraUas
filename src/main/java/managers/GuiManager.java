package main.java.managers;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import main.java.gui.components.factory.ButtonBar;
import main.java.gui.components.factory.DronePanel;
import main.java.gui.controllers.DataRefresher;
import main.java.gui.controllers.ScreenshotService;
import main.java.gui.views.DroneCatalogue;
import main.java.gui.views.DroneHistory;
import main.java.gui.views.FlightDynamics;
import main.java.gui.views.HealthCheck;
import main.java.model.DroneBase;
import main.java.util.Logs;

import java.net.URL;

/**
 * The {@code GuiManager} class manages the GUI components of the application.
 * It initializes the main GUI and refreshes the views based on the drone data.
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #startGui(Stage, String[]) startGui}:
 *         Starts the GUI with the specified primary stage and arguments.</li>
 *     <li>{@link #refreshViews() refreshViews}:
 *         Refreshes the tabs in the GUI based on the drone data.</li>
 *     <li>{@link #createViewInstance(String, String) createViewInstance}:
 *         Creates a new instance of a view based on the provided title and type.</li>
 *     <li>{@link #applyStyleSheetToScene(Scene) applyStyleSheetToScene}:
 *         Applies the CSS stylesheet to the scene.</li>
 * </ul>
 * </p>
 */


public class GuiManager {
    private final DronePanel dronePanelFactory = new DronePanel();
    private final ButtonBar buttonBarFactory = new ButtonBar();
    private final ScreenshotService screenshotService = new ScreenshotService();
    private final TabPane tabPane = new TabPane();

    private Stage primaryStage;
    private String[] applicationArgs;

    private ObservableList<DroneBase> droneModels;

    public void startGui(Stage primaryStage, String[] args) {
        this.primaryStage = primaryStage;
        applicationArgs = args;
        dataRefresher.refreshData();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Scene scene = new Scene(tabPane, 1000, 800);
        applyStyleSheetToScene(scene);
        primaryStage.setTitle("Drone Monitoring System");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        Logs.info("Starting main GUI");
        primaryStage.show();

        refreshViews();
    }

    private void refreshViews() {
        tabPane.getTabs().clear();
        tabPane.getTabs().addAll(
                createViewInstance("Drone Health Check", "DHC"),
                createViewInstance("Drone Flight Dynamics", "DFD"),
                createViewInstance("Drone Catalogue", "DC"),
                createViewInstance("Drone History", "DH")
        );
    }    private final DataRefresher dataRefresher = new DataRefresher(ignored -> {
        ApiManager.setupData(applicationArgs);
        droneModels = ApiManager.getDroneModels();
        refreshViews();
    });

    private Tab createViewInstance(String title, String type) {
        try {
            return switch (type) {
                case "DHC" ->
                        new HealthCheck(title, primaryStage, dronePanelFactory, buttonBarFactory, screenshotService, dataRefresher, droneModels);
                case "DC" ->
                        new DroneCatalogue(title, primaryStage, buttonBarFactory, screenshotService, dataRefresher, droneModels);
                case "DFD" ->
                        new FlightDynamics(title, primaryStage, buttonBarFactory, screenshotService, dataRefresher, droneModels);
                case "DH" ->
                        new DroneHistory(title, primaryStage, buttonBarFactory, screenshotService, dataRefresher, droneModels);
                default -> throw new IllegalArgumentException("Invalid Tab type: " + type);
            };
        } catch (Exception e) {
            Logs.error("Error occurred in GuiManager.createViewInstance(): " + e.getMessage());
            return null;
        }
    }

    private void applyStyleSheetToScene(Scene scene) {
        try {
            URL file = GuiManager.class.getResource("/css/style.css");
            if (file != null) {
                scene.getStylesheets().add(file.toExternalForm());
            } else {
                Logs.warning("CSS file not found, GuiManager.applyStyleSheetToScene()");
            }
        } catch (Exception e) {
            Logs.error("Error occurred in GuiManager.applyStyleSheetToScene(): " + e.getMessage());
        }
    }
}
