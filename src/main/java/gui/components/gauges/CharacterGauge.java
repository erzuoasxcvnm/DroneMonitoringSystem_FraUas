package main.java.gui.components.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import main.java.util.Logs;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for creating a character gauge.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #CharacterGauge(String, String, boolean) CharacterGauge constructor}: Initialize a character gauge with the given title, value, and type.</li>
 *     <li>{@link #convertDateTimeFormat(String, boolean) convertDateTimeFormat}: Convert the date-time format if the value is a date.</li>
 *     <li>{@link #createCharacterTile() createCharacterTile}: Create the Tile for the character gauge.</li>
 * </ul>
 * </p>
 */

public class CharacterGauge {
    private final Tile characterTile;
    private final String value;
    private final String title;
    private final boolean isDate;

    public CharacterGauge(String title, String value, boolean isDate) {
        this.value = value;
        this.title = title;
        this.isDate = isDate;
        this.characterTile = createCharacterTile();
    }

    public static String convertDateTimeFormat(String value, boolean isDate) {
        try {
            if (isDate) {
                ZonedDateTime inputDateTime = ZonedDateTime.parse(value);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yy.MM.dd - HH.mm.ss.SS");
                return inputDateTime.format(outputFormatter);
            } else {
                return value;
            }
        } catch (Exception e) {
            Logs.error("Error occurred in CharacterGauge.convertDateTimeFormat() : " + e.getMessage());
            return null;
        }
    }

    private Tile createCharacterTile() {
        try {
            return TileBuilder.create().skinType(Tile.SkinType.CHARACTER)
                    .prefSize(200, 200)
                    .title(title)
                    .maxSize(200, 200)
                    .minSize(200, 200)
                    .description(convertDateTimeFormat(value, isDate))
                    .build();

        } catch (Exception e) {
            Logs.error("Error occurred in CharacterGauge.createCharacterTile() : " + e.getMessage());
            return null;
        }
    }

    public Tile getCharacterTile() {
        return this.characterTile;
    }
}