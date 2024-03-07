package main.java.gui.components.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import main.java.util.Logs;

/**
 * Class for creating a speed gauge.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #SpeedGauge(int) SpeedGauge constructor}: Initialize a speed gauge with the given value.</li>
 *     <li>{@link #createSpeedTile() createSpeedTile}: Create the Tile for the speed gauge.</li>
 * </ul>
 * </p>
 */

public class SpeedGauge {
    private final Tile speedTile;
    private final int speedValue;

    public SpeedGauge(int speedValue) {
        this.speedValue = speedValue;
        this.speedTile = createSpeedTile();
    }

    private Tile createSpeedTile() {
        try {
            Tile speedTile = TileBuilder.create().skinType(Tile.SkinType.GAUGE)
                    .prefSize(200, 200)
                    .title("Speed")
                    .unit("Km/h")
                    .threshold(40)
                    .build();

            speedTile.setMinSize(200, 200);
            speedTile.setMaxSize(200, 200);
            speedTile.setValue(speedValue);

            return speedTile;
        } catch (Exception e) {
            Logs.error("Error occurred in SpeedGauge.createSpeedTile() : " + e.getMessage());
            return null;
        }
    }

    public Tile getSpeedTile() {
        return this.speedTile;
    }
}