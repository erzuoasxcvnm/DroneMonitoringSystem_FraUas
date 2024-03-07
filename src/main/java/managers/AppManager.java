package main.java.managers;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The {@code AppManager} class serves as the entry point of the application.
 * It extends {@code Application} and initializes the GUI manager to start the application.
 *
 * <p>Methods:
 * <ul>
 *     <li>{@link #main(String[])}: The main method that serves as the entry point of the JavaFX application.</li>
 *     <li>{@link #start(Stage)}: The overridden method from {@code Application} class, responsible for starting the JavaFX application.</li>
 * </ul>
 */

public class AppManager extends Application {
    private final GuiManager guiManager = new GuiManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        guiManager.startGui(primaryStage, getParameters().getRaw().toArray(new String[0]));
    }
}
