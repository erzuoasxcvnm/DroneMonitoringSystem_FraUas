package main.java.services;

import main.java.model.DroneBase;
import main.java.util.ConfigurationLoader;
import main.java.util.Constants;
import main.java.util.Logs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * The {@code ApiWorkers} class extends {@code ApiFetchers} and implements the {@code Runnable} interface.
 * It represents worker threads responsible for fetching and processing drone data in a concurrent environment.
 * <p>
 * It overrides the {@code run} method, which is called when the thread is started. The worker thread fetches
 * drone type data and drone dynamics data concurrently using CompletableFuture. It then processes the
 * drone data and adds the resulting {@code DroneBase} object to the shared {@code ConcurrentLinkedQueue}.
 * </p>
 */


public class ApiWorkers extends ApiFetchers implements Runnable {
    private final ConcurrentLinkedQueue<DroneBase> droneModels;
    private final Map<String, Object> droneInfo;
    private final CountDownLatch completionLatch;

    public ApiWorkers(Map<String, Object> droneInfo, ConfigurationLoader configLoader, CountDownLatch completionLatch) {
        super(configLoader);
        this.completionLatch = completionLatch;
        this.droneModels = ModelsHandler.getInstance();
        this.droneInfo = droneInfo;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletableFuture<Map<String, Object>> droneTypeFuture = CompletableFuture.supplyAsync(
                () -> fetchDroneTypeData(droneInfo), executorService
        );

        CompletableFuture<List<Map<String, Object>>> droneDynamicsFuture = CompletableFuture.supplyAsync(
                () -> fetchDroneDynamicsData(droneInfo, false), executorService
        );

        CompletableFuture.allOf(droneTypeFuture, droneDynamicsFuture).join();

        DroneBase droneObject = processDroneData(droneInfo, droneTypeFuture.join(), droneDynamicsFuture.join());
        droneModels.add(droneObject);
        Logs.info("Drone data added to droneModels, id: " + droneObject.getDroneDetails().get(Constants.DRONE_ID));
        completionLatch.countDown();
        executorService.shutdown();
    }
}