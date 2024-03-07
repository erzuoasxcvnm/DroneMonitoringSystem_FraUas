package main.java.gui.components.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.scene.paint.Color;
import main.java.gui.controllers.DroneStatus;
import main.java.util.Logs;

/**
 * Class for creating a status gauge.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #StatusGauge(DroneStatus.State) StatusGauge constructor}: Initialize a status gauge with the given state.</li>
 *     <li>{@link #createStatusTile() createStatusTile}: Create the Tile for the status gauge.</li>
 *     The class is DroneStatus dependent to apply teh enum operation.
 * </ul>
 * </p>
 */

public class StatusGauge {
    private final Tile statusTile;
    private final DroneStatus.State status;

    public StatusGauge(DroneStatus.State status) {
        this.status = status;
        this.statusTile = createStatusTile();
    }

    private Tile createStatusTile() {
        try {
            Tile statusTile = TileBuilder.create().skinType(Tile.SkinType.LED)
                    .prefSize(200, 200)
                    .barBackgroundColor(Color.TRANSPARENT)
                    .title("Status")
                    .activeColor(DroneStatus.getStatusColor(status))
                    .build();

            DroneStatus.updateStatus(statusTile, status);
            statusTile.setMinSize(200, 200);
            statusTile.setMaxSize(200, 200);

            return statusTile;
        } catch (Exception e) {
            Logs.error("Error occurred in StatusGauge.createStatusTile() : " + e.getMessage());
            return null;
        }
    }

    public Tile getStatusTile() {
        return this.statusTile;
    }
}