package main.java.managers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.model.DroneBase;
import main.java.services.ApiFetchers;
import main.java.services.ApiWorkers;
import main.java.services.ModelsHandler;
import main.java.util.ArgsHandler;
import main.java.util.ConfigurationLoader;
import main.java.util.Logs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The {@code ApiManager} class manages the retrieval and processing of drone data from the API.
 * It provides methods to set up data, fetch drone models, and manage drone requests.
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #getDroneModels() getDroneModels}:
 *         Retrieves the observable list of drone models.</li>
 *     <li>{@link #setupData(String[]) setupData}:
 *         Sets up the data for the application using the provided arguments.</li>
 *     <li>{@link #setupLogger(ArgsHandler) setupLogger}:
 *         Sets up the logger based on the provided arguments handler.</li>
 *     <li>{@link #setupConfiguration(ArgsHandler) setupConfiguration}:
 *         Sets up the configuration loader based on the provided arguments handler.</li>
 *     <li>{@link #executeDroneRequest(ConfigurationLoader) executeDroneRequest}:
 *         Executes the drone request and returns the list of drone information.</li>
 *     <li>{@link #startManager(List, ConfigurationLoader) startManager}:
 *         Starts the manager to process drone information.</li>
 *     <li>{@link #shutdownThreadPool(ExecutorService) shutdownThreadPool}:
 *         Shuts down the thread pool after a certain period of time.</li>
 * </ul>
 * </p>
 */


public class ApiManager {
    private static final int MAX_CONCURRENT_DRONE_REQUESTS = 8;
    private static final int AWAIT_TERMINATION_PERIOD_SECONDS = 30;

    public static ObservableList<DroneBase> getDroneModels() {
        return FXCollections.observableArrayList(ModelsHandler.getInstance());
    }

    public static void setupData(String[] arguments) {
        try {
            ArgsHandler argsHandler = new ArgsHandler(arguments);
            ModelsHandler.clearInstance();
            setupLogger(argsHandler);

            ConfigurationLoader configLoader = setupConfiguration(argsHandler);

            List<Map<String, Object>> dronesInfo = executeDroneRequest(configLoader);
            Logs.info("Number of drones in droneSimulator : " + dronesInfo.size());

            long startTime = System.currentTimeMillis();

            startManager(dronesInfo, configLoader);

            Logs.info("Total execution time: " + (System.currentTimeMillis() - startTime) + " ms");
        } catch (Exception e) {
            Logs.error("Error occurred in ApiManager.setupData() : " + e.getMessage());
        }
    }

    private static void setupLogger(ArgsHandler argsHandler) {
        Logs.info("Setup Logging file");
        Logs.initialize(argsHandler.getLogPath());
    }

    private static ConfigurationLoader setupConfiguration(ArgsHandler argsHandler) {
        Logs.info("Loading configuration");
        return new ConfigurationLoader(argsHandler.getConfigPath());
    }

    private static List<Map<String, Object>> executeDroneRequest(ConfigurationLoader configLoader) {
        Logs.info("Starting initial api request");
        ApiFetchers fetcher = new ApiFetchers(configLoader);
        return fetcher.executeDroneRequest(null);
    }

    private static void startManager(List<Map<String, Object>> dronesInfo, ConfigurationLoader configLoader) {
        try {
            CountDownLatch completionLatch = new CountDownLatch(dronesInfo.size());

            ExecutorService droneRequestThreadPool = Executors.newFixedThreadPool(MAX_CONCURRENT_DRONE_REQUESTS);
            for (Map<String, Object> droneInfo : dronesInfo) {
                ApiWorkers processor = new ApiWorkers(droneInfo, configLoader, completionLatch);
                droneRequestThreadPool.execute(processor);
            }
            shutdownThreadPool(droneRequestThreadPool);
        } catch (Exception e) {
            Logs.error("Error occurred in ApiManger.manageDronesData() : " + e.getMessage());
        }

    }

    private static void shutdownThreadPool(ExecutorService droneRequestThreadPool) {
        droneRequestThreadPool.shutdown();
        try {
            if (!droneRequestThreadPool.awaitTermination(AWAIT_TERMINATION_PERIOD_SECONDS, TimeUnit.SECONDS)) {
                List<Runnable> droppedTasks = droneRequestThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            droneRequestThreadPool.shutdownNow();
            Logs.error("ThreadPool shutdown interrupted: " + e.getMessage());
        }
    }
}