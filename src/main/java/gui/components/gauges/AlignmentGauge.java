package main.java.gui.components.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.skins.BarChartItem;
import main.java.util.Logs;

/**
 * Factory class for creating alignment gauges.
 * <p>
 * This class provides a method to create an alignment gauge, which displays yaw, roll, and pitch values
 * in a bar chart format.
 * </p>
 */

public class AlignmentGauge {
    private final Tile alignmentTile;
    private final String yawValue;
    private final String rollValue;
    private final String pitchValue;

    public AlignmentGauge(String yawValue, String rollValue, String pitchValue) {
        this.yawValue = yawValue;
        this.rollValue = rollValue;
        this.pitchValue = pitchValue;
        this.alignmentTile = createAlignmentTile();
    }

    private Tile createAlignmentTile() {
        try {
            return TileBuilder.create()
                    .prefSize(200, 200)
                    .skinType(Tile.SkinType.BAR_CHART)
                    .title("Alignment")
                    .textVisible(true)
                    .maxSize(200, 200)
                    .minSize(200, 200)
                    .decimals(0)
                    .barChartItems(
                            new BarChartItem("Yaw", Double.parseDouble(yawValue), Tile.BLUE),
                            new BarChartItem("Pitch", Double.parseDouble(pitchValue), Tile.GREEN),
                            new BarChartItem("Roll", Double.parseDouble(rollValue), Tile.RED))
                    .build();
        } catch (Exception e) {
            Logs.error("Error occurred in AlignmentGauge.createAlignmentTile() : " + e.getMessage());
            return null;
        }
    }

    public Tile getAlignmentTile() {
        return this.alignmentTile;
    }
}