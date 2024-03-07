package main.java.gui.components.factory;

import javafx.scene.layout.Pane;
import main.java.gui.components.panels.CustomPanel;
import main.java.util.Logs;

/**
 * Factory class for creating drone panels.
 * <p>
 * This class provides a method {@code createDronePanel()} to create a drone panel with specified drone name and status.
 * </p>
 */

public class DronePanel {
    public Pane createDronePanel(String droneName, String status) {
        try {
            return new CustomPanel(droneName, status);
        } catch (Exception e) {
            Logs.error("Error in DronePanelFactory.createDronePanel() : " + e.getMessage());
            return null;
        }
    }
}