package main.java.gui.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.util.Logs;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Service class for taking screenshots of the application window.
 * <p>
 * This class provides a method to take a screenshot of the application window and save it as a PNG file.
 * </p>
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #takeScreenshot(Stage) takeScreenshot}: Takes a screenshot of the application window and saves it as a PNG file.</li>
 * </ul>
 * </p>
 */

public class ScreenshotService {
    public void takeScreenshot(Stage primaryStage) {
        try {
            WritableImage screenshot = primaryStage.getScene().snapshot(null);
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
            String filename = "screenshot-" + timestamp + ".png";

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
            fileChooser.setInitialFileName(filename);
            File outputFile = fileChooser.showSaveDialog(primaryStage);

            if (outputFile != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(screenshot, null), "png", outputFile);
                System.out.println("Screenshot saved to: " + outputFile.getAbsolutePath());
            }

        } catch (IOException e) {
            Logs.error("Error in ScreenshotService.takeScreenshot() : " + e.getMessage());
        }
    }
}