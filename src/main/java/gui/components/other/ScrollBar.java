package main.java.gui.components.other;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Class for creating a custom scroll bar.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #ScrollBar() ScrollBar constructor}: Initialize a custom scroll bar.</li>
 * </ul>
 * </p>
 */
public class ScrollBar extends ScrollPane {

    public ScrollBar() {
        setFitToWidth(true);
        setFitToHeight(true);

        VBox contentPane = new VBox();
        contentPane.getStyleClass().add("scroll-bar");

        setContent(contentPane);
    }
}