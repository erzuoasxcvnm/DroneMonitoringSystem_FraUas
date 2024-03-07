package main.java.gui.interfaces;

import javafx.scene.control.TextField;

/**
 * Interface for defining filter actions.
 * <p>
 * This interface defines methods to execute a query, reset a query, and handle pressing Enter in a text field.
 * </p>
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #executeQuery(String, String, String, String, String) executeQuery}: Executes a filter query based on specified parameters.</li>
 *     <li>{@link #resetQuery() resetQuery}: Resets all filter fields and clears the filter results. Displays all data Again</li>
 *     <li>{@link #pressEnter(TextField) pressEnter}: Handles pressing Enter in a text field.</li>
 * </ul>
 * </p>
 */

public interface FilterAction {
    void executeQuery(String droneId, String serialNumber, String manufacturer, String typeName, String carriageType);

    void resetQuery();

    void pressEnter(TextField textField);
}
