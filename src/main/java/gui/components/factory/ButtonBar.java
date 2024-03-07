package main.java.gui.components.factory;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import main.java.gui.components.buttons.CustomButton;
import main.java.gui.components.other.DigitalClock;
import main.java.util.Logs;

/**
 * Creates a button bar with the specified alignment and buttons.
 */
public class ButtonBar {

    /**
     * Creates a button bar with the specified alignment and buttons.
     *
     * @param buttons   the buttons to include in the button bar
     * @return the created button bar as a Node
     * @see CustomButton
     */
    public Node createButtonBar(CustomButton... buttons) {
        try {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox buttonBar = new HBox(10);
            HBox buttonsBox = new HBox(10);
            HBox clockBox = new HBox(5);

            DigitalClock clock = new DigitalClock();
            clockBox.setAlignment(Pos.CENTER_LEFT);
            clockBox.getChildren().add(clock);
            buttonsBox.getStyleClass().add("button-bar");
            buttonsBox.getChildren().addAll(buttons);
            buttonsBox.setAlignment(Pos.TOP_RIGHT);
            buttonBar.getChildren().addAll(clockBox, spacer, buttonsBox);
            return buttonBar;
        } catch (Exception e) {
            Logs.error("Error in ButtonBarFactory.createButtonBar() : " + e.getMessage());
            return null;
        }
    }
}
