package main.java.gui.components.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.scene.paint.Color;
import main.java.util.Logs;

/**
 * Class for creating a battery capacity gauge.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #BatteryCapacityGauge(int) BatteryCapacityGauge constructor}: Initialize a battery capacity gauge with the given value.</li>
 *     <li>{@link #createBatteryTile() createBatteryTile}: Create the Tile for the battery gauge.</li>
 *     <li>{@link #chartColor(double) chartColor}: Determine the color for the battery gauge based on the value.</li>
 * </ul>
 * </p>
 */

public class BatteryCapacityGauge {
    private final Tile batteryTile;
    private final int batteryValue;

    public BatteryCapacityGauge(int batteryValue) {
        this.batteryValue = batteryValue;
        this.batteryTile = createBatteryTile();
    }

    private Tile createBatteryTile() {
        try {
            Tile batteryTile = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                    .prefSize(200, 200)
                    .title("Battery")
                    .titleColor(Color.WHITE)
                    .barColor(chartColor(batteryValue))
                    .unit("%")
                    .middleValue(50)
                    .maxValue(100)
                    .minValue(0)
                    .build();
            batteryTile.setMinSize(200, 200);
            batteryTile.setMaxSize(200, 200);
            batteryTile.setValue(batteryValue);

            return batteryTile;
        } catch (Exception e) {
            Logs.error("Error occurred in BatteryCapacityGauge.createBatteryTile() : " + e.getMessage());
            return null;
        }
    }

    private Color chartColor(double value) {
        if (value >= 0 && value <= 10) {
            return Color.RED;
        } else if (value >= 11 && value <= 30) {
            return Color.ORANGE;
        } else if (value >= 31 && value <= 50) {
            return Color.YELLOW;
        } else if (value > 50) {
            return Color.GREEN;
        } else {
            return Color.BLACK;
        }
    }

    public Tile getBatteryTile() {
        return this.batteryTile;
    }
}