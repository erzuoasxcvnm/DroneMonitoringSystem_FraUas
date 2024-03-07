package main.java.gui.controllers;

import eu.hansolo.tilesfx.Tile;
import javafx.scene.paint.Color;

/**
 * Class for managing drone status.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #updateStatus(Tile, State) updateStatus}: Update the status of a tile based on the provided state.</li>
 *     <li>{@link #getStatusColor(State) getStatusColor}: Get the color associated with a given state.</li>
 * </ul>
 * </p>
 */

public class DroneStatus {
    public static void updateStatus(Tile statusTile, State status) {
        statusTile.setActiveColor(getStatusColor(status));
        statusTile.setActive(true);
        statusTile.setDescription(status.getDescription());
    }

    public static Color getStatusColor(State status) {
        return switch (status) {
            case ON -> Color.GREEN.brighter();
            case OFF -> Color.RED.brighter();
            case ISSUE -> Color.ORANGE.brighter();
            default -> Color.GRAY.brighter();
        };
    }

    public enum State {
        ON("ON"),
        OFF("OFF"),
        ISSUE("ISSUE"),
        DEFAULT;

        private final String description;

        State(String description) {
            this.description = description;
        }

        State() {
            this.description = "UNKNOWN";
        }

        public String getDescription() {
            return description;
        }
    }
}
