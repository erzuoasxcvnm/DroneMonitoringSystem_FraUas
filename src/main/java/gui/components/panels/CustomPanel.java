package main.java.gui.components.panels;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.java.util.Constants;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Class for creating a custom panel.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #CustomPanel(String, String) CustomPanel constructor}: Initialize a custom panel with the given drone ID and status.</li>
 *     <li>{@link #initializePanel(String, String) initializePanel}: Initialize the custom panel with the provided drone ID and status.</li>
 *     <li>{@link #createPanelNameLabel(String) createPanelNameLabel}: Create a label for the panel's name.</li>
 *     <li>{@link #createStatusLabel(String) createStatusLabel}: Create a label for the panel's status.</li>
 *     <li>{@link #createDivisions(String) createDivisions}: Create divisions based on the panel's status.</li>
 *     <li>An enum {@link StyleStatus} to handle different panel styles based on status.</li>
 * </ul>
 * </p>
 */

public class CustomPanel extends StackPane {

    public CustomPanel(String droneId, String status) {
        initializePanel(droneId, status);
    }

    private void initializePanel(String droneId, String status) {

        VBox vbox = new VBox(10);
        vbox.setMaxWidth(Double.MAX_VALUE);
        vbox.getChildren().addAll(createPanelNameLabel(droneId), createStatusLabel(status), createDivisions(status));
        vbox.setFocusTraversable(false);

        getChildren().add(vbox);
        setPrefWidth(Region.USE_COMPUTED_SIZE);

        StyleStatus styleStatus;
        try {
            styleStatus = StyleStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            styleStatus = StyleStatus.DEFAULT_STATUS;
        }
        getStyleClass().setAll(styleStatus.getStyle().split(" "));
    }

    private Label createPanelNameLabel(String droneId) {
        Label droneLabel = new Label(droneId);
        droneLabel.getStyleClass().add("panel-name");
        return droneLabel;
    }

    private Label createStatusLabel(String status) {
        String label = switch (status) {
            case Constants.STATUS_OF -> "OFF";
            case Constants.STATUS_IS -> "ISSUE";
            case Constants.STATUS_ON -> "ON";
            default -> "UNKNOWN";
        };

        Label statusLabel = new Label(label);
        statusLabel.getStyleClass().addAll(status.toLowerCase(), "status-label");
        return statusLabel;
    }

    private VBox createDivisions(String status) {
        VBox divisions = new VBox(10);
        divisions.setMaxWidth(Double.MAX_VALUE);

        IntStream.rangeClosed(1, 3).forEach(i -> {
            StackPane division = new StackPane();
            division.getStyleClass().addAll("division", status.toLowerCase());
            divisions.getChildren().add(division);
        });

        return divisions;
    }

    private enum StyleStatus {
        ON("panel on"),
        OF("panel of"),
        IS("panel is"),
        DEFAULT_STATUS("panel unknown");

        private final String style;

        StyleStatus(String style) {
            this.style = style;
        }

        public String getStyle() {
            return this.style;
        }
    }
}