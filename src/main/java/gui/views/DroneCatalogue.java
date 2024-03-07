package main.java.gui.views;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.gui.components.buttons.CustomButton;
import main.java.gui.components.factory.ButtonBar;
import main.java.gui.components.factory.CatalogueFilter;
import main.java.gui.components.factory.DroneProfile;
import main.java.gui.components.other.ScrollBar;
import main.java.gui.controllers.DataRefresher;
import main.java.gui.controllers.ScreenshotService;
import main.java.model.DroneBase;

import java.util.Comparator;

/**
 * View class representing a tab for displaying drone catalogue information.
 * <p>
 * This class extends Tab and provides methods to initialize the user interface for the drone catalogue tab.
 * </p>
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #DroneCatalogue(String, Stage, ButtonBar, ScreenshotService, DataRefresher, ObservableList) DroneCatalogue constructor}:
 *         Initializes the drone catalogue tab with the specified title, stage, button bar factory, screenshot service, data refresher, and drone models.</li>
 *     <li>{@link #initializeUI(String, ObservableList) initializeUI}:
 *         Initializes the user interface components for the drone catalogue tab.</li>
 *     <li>{@link #initButtonBar() initButtonBar}:
 *         Initializes the button bar with refresh, screenshot, and quit buttons.</li>
 *     <li>{@link #initFilterNode(FlowPane, ObservableList) initFilterNode}:
 *         Initializes the filter node for filtering drone models.</li>
 *     <li>{@link #initScrollPane(FlowPane, ObservableList) initScrollPane}:
 *         Initializes the scroll pane for displaying drone profiles.</li>
 * </ul>
 * </p>
 */


public class DroneCatalogue extends Tab {
    private final ButtonBar buttonBarFactory;
    private final ScreenshotService screenshotService;
    private final DataRefresher dataRefresher;
    private final Stage primaryStage;

    public DroneCatalogue(String tabTitle, Stage primaryStage, ButtonBar buttonBarFactory,
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
        FlowPane contentPane = new FlowPane();
        contentPane.getStyleClass().add("catalogue-content");
        VBox topContainer = new VBox();
        Node buttonBar = initButtonBar();
        Node filterNode = initFilterNode(contentPane, droneModels);
        ScrollPane scrollPane = initScrollPane(contentPane, droneModels);
        topContainer.getChildren().addAll(buttonBar, filterNode);
        VBox mainVBox = new VBox(topContainer, scrollPane);
        setContent(mainVBox);
        setClosable(false);
    }

    private Node initButtonBar() {
        CustomButton refreshButton = new CustomButton("Refresh Data", "refresh.png");
        refreshButton.setOnAction(event -> dataRefresher.refreshData());

        CustomButton screenshotButton = new CustomButton("Take Screenshot", "screenshot.png");
        screenshotButton.setOnAction(event -> screenshotService.takeScreenshot(primaryStage));

        CustomButton quitButton = new CustomButton("Close Program", "power.png");
        quitButton.setOnAction(event -> primaryStage.close());

        return buttonBarFactory.createButtonBar(refreshButton, screenshotButton, quitButton);
    }

    private Node initFilterNode(FlowPane contentPane, ObservableList<DroneBase> droneModels) {
        CatalogueFilter catalogueFilter = new CatalogueFilter(contentPane, droneModels);
        return catalogueFilter.createFilter();
    }

    private ScrollPane initScrollPane(FlowPane contentPane, ObservableList<DroneBase> droneModels) {
        droneModels.sort(Comparator.comparing(d -> (Integer) d.getDroneDetails().get("id")));
        for (DroneBase drone : droneModels) {
            DroneProfile droneProfile = new DroneProfile(drone);
            AnchorPane wrapperPane = new AnchorPane(droneProfile);
            contentPane.getChildren().add(wrapperPane);
        }

        ScrollBar scrollPane = new ScrollBar();
        scrollPane.setContent(contentPane);

        return scrollPane;
    }
}