package main.java.gui.components.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.scene.paint.Color;
import main.java.util.Logs;


/**
 * Class for creating a clock gauge.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #ClockGauge() ClockGauge constructor}: Initialize a clock gauge.</li>
 * </ul>
 * </p>
 */

public class ClockGauge {
    private Tile clockTile;

    public ClockGauge() {
        try {
            clockTile = TileBuilder.create().skinType(Tile.SkinType.CLOCK)
                    .prefSize(200, 200)
                    .title("At")
                    .titleColor(Color.WHITE)
                    .build();

            clockTile.setMinSize(200, 200);
            clockTile.setMaxSize(200, 200);

        } catch (Exception e) {
            Logs.error("Error occurred in ClockGauge : " + e.getMessage());
        }
    }

    public Tile getClockTile() {
        return clockTile;
    }
}