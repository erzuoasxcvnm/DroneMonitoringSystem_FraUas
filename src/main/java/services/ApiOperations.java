package main.java.services;


import main.java.model.DroneBase;
import main.java.util.Coding;
import main.java.util.ConfigurationLoader;

import java.util.List;
import java.util.Map;

/**
 * The {@code ApiOperations} class is an abstract class representing operations related to API interactions.
 * It contains methods for executing drone requests, fetching drone count, fetching drone type data,
 * fetching drone dynamics data, and processing drone data.
 * <p>
 * Subclasses:
 * <ul>
 *     <li>{@link ApiFetchers}</li>
 *     <li>{@link ApiWorkers}</li>
 * </ul>
 * </p>
 */


public abstract class ApiOperations {
    protected final String apiUrl;
    protected final String authenticationToken;
    protected final String userAgentHeader;
    protected final String droneEndpoint;

    public ApiOperations(ConfigurationLoader configLoader) {
        this.apiUrl = configLoader.read("client", "domain");
        this.authenticationToken = Coding.decode(configLoader.read("client", "token"));
        this.userAgentHeader = configLoader.read("client", "agent");
        this.droneEndpoint = configLoader.read("client", "endpoints.drones");
    }

    public abstract List<Map<String, Object>> executeDroneRequest(Map<String, Object> droneInfo);

    public abstract int fetchCount(Map<String, Object> droneInfo, boolean isDynamics);

    public abstract Map<String, Object> fetchDroneTypeData(Map<String, Object> droneInfo);

    public abstract List<Map<String, Object>> fetchDroneDynamicsData(Map<String, Object> droneInfo, boolean isLast);

    public abstract DroneBase processDroneData(Map<String, Object> droneInfo, Map<String, Object> typeInfo, List<Map<String, Object>> dynamicsInfo);
}