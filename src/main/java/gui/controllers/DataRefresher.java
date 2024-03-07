package main.java.gui.controllers;

import main.java.util.Logs;

import java.util.function.Consumer;

/**
 * Class for handling data refreshing.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #DataRefresher(Consumer) DataRefresher constructor}: Initialize a data refresher with a consumer for refreshing data.</li>
 *     <li>{@link #refreshData() refreshData}: Refresh the data using the provided consumer.</li>
 * </ul>
 * </p>
 */

public class DataRefresher {

    private final Consumer<Void> onRefreshData;

    public DataRefresher(Consumer<Void> onRefreshData) {
        this.onRefreshData = onRefreshData;
    }

    public void refreshData() {
        try {
            this.onRefreshData.accept(null);
            Logs.debug("Data Reload is in Progress");
        } catch (Exception e) {
            Logs.error("Error in DataRefresher.refreshData() : " + e.getMessage());
        }
    }
}