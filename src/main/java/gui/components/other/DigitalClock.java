package main.java.gui.components.other;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Class for creating a digital clock label.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #DigitalClock() DigitalClock constructor}: Initialize a digital clock label.</li>
 *     <li>{@link #bindToTime() bindToTime}: Bind the label to the current time.</li>
 * </ul>
 * </p>
 */
public class DigitalClock extends Label {

    public DigitalClock() {
        getStyleClass().add("digital-clock");
        bindToTime();
    }

    private void bindToTime() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        event -> setText(new SimpleDateFormat("dd.MM.yy - HH:mm:ss").format(new Date()))),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}