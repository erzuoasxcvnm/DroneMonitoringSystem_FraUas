package main.java.services;

import main.java.model.DroneBase;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@code ModelsHandler} class provides a thread-safe way to access and manage a shared collection of drone models.
 * It utilizes a {@code ConcurrentLinkedQueue} to store instances of {@code DroneBase}.
 * <p>
 * The class provides two static synchronized methods:
 * - {@code getInstance}: Returns the singleton instance of the {@code ConcurrentLinkedQueue<DroneBase>}. If the instance
 *   doesn't exist, it creates a new one.
 * - {@code clearInstance}: Clears the singleton instance of the {@code ConcurrentLinkedQueue<DroneBase>}.
 * </p>
 */


public class ModelsHandler {
    private static ConcurrentLinkedQueue<DroneBase> droneModels;

    public static synchronized ConcurrentLinkedQueue<DroneBase> getInstance() {
        if (droneModels == null) {
            droneModels = new ConcurrentLinkedQueue<>();
        }
        return droneModels;
    }

    public static synchronized void clearInstance() {
        if (droneModels != null) {
            droneModels.clear();
        }
    }
}