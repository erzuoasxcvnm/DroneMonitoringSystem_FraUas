package main.java.services;

import main.java.api.DroneAPI;
import main.java.model.DroneBase;
import main.java.util.ConfigurationLoader;
import main.java.util.Constants;
import main.java.util.Logs;
import main.java.util.ResponseHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The {@code ApiFetchers} class extends {@link ApiOperations} and is responsible for fetching data from the API.
 * It retrieves drone details, type information, and dynamics information.
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #executeDroneRequest(Map) executeDroneRequest}:
 *         Executes a drone request and returns the response.</li>
 *     <li>{@link #fetchDroneTypeData(Map) fetchDroneTypeData}:
 *         Fetches drone type data and returns the response.</li>
 *     <li>{@link #fetchDroneDynamicsData(Map, boolean) fetchDroneDynamicsData}:
 *         Fetches drone dynamics data and returns the response.</li>
 *     <li>{@link #fetchCount(Map, boolean) fetchCount}:
 *         Fetches the count of drones or drone dynamics and returns it.</li>
 *     <li>{@link #processDroneData(Map, Map, List) processDroneData}:
 *         Processes drone data and returns a {@link DroneBase} object.</li>
 * </ul>
 * </p>
 */

public class ApiFetchers extends ApiOperations {
    public ApiFetchers(ConfigurationLoader configLoader) {
        super(configLoader);
    }

    @Override
    public List<Map<String, Object>> executeDroneRequest(Map<String, Object> droneInfo) {
        try {
            int droneCount = fetchCount(droneInfo, false);
            String endpoint = droneEndpoint + "?limit=" + droneCount;
            DroneAPI dronesDataAPI = new DroneAPI(apiUrl, endpoint, authenticationToken, userAgentHeader);
            return ResponseHandler.handle(Constants.DETAILS, dronesDataAPI.retrieveResponse());
        } catch (Exception e) {
            Logs.error("Error occurred in Fetchers.executeDroneRequest() : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> fetchDroneTypeData(Map<String, Object> droneInfo) {
        try {
            String typeUrl = (String) droneInfo.get(Constants.DRONE_TYPE);
            DroneAPI droneTypeAPI = new DroneAPI(typeUrl, "", authenticationToken, userAgentHeader);
            List<Map<String, Object>> typeInfo = ResponseHandler.handle(Constants.TYPES, droneTypeAPI.retrieveResponse());
            return !typeInfo.isEmpty() ? typeInfo.get(0) : null;
        } catch (Exception e) {
            Logs.error("Error occurred in Fetchers.fetchDroneTypeData() : " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    @Override
    public List<Map<String, Object>> fetchDroneDynamicsData(Map<String, Object> droneInfo, boolean isLast) {
        try {
            int dynamicsCount = fetchCount(droneInfo, true);
            String endpoint = "/api/" + droneInfo.get(Constants.DRONE_ID) + "/dynamics?limit=" + dynamicsCount;
            if (isLast) {
                endpoint = "/api/" + droneInfo.get(Constants.DRONE_ID) + "/dynamics?limit=1&offset=" + (dynamicsCount - 1);
            }
            DroneAPI droneDynamicsDataAPI = new DroneAPI(apiUrl, endpoint, authenticationToken, userAgentHeader);
            return ResponseHandler.handle(Constants.DYNAMICS, droneDynamicsDataAPI.retrieveResponse());
        } catch (Exception e) {
            Logs.error("Error occurred in Fetchers.fetchDroneDynamicsData() : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public int fetchCount(Map<String, Object> droneInfo, boolean isDynamics) {
        try {
            String endpoint = droneEndpoint;
            if (isDynamics) {
                endpoint = "/api/" + droneInfo.get(Constants.DRONE_ID) + "/dynamics";
            }
            DroneAPI dronesAPI = new DroneAPI(apiUrl, endpoint, authenticationToken, userAgentHeader);
            return ResponseHandler.extractCount(dronesAPI.retrieveResponse());
        } catch (Exception e) {
            Logs.error("Error occurred in Fetchers.fetchCount() : " + e.getMessage());
            return -1;
        }
    }

    @Override
    public DroneBase processDroneData(Map<String, Object> droneInfo, Map<String, Object> typeInfo, List<Map<String, Object>> dynamicsInfo) {
        try {
            DroneBase droneObject = new DroneBase(droneInfo, typeInfo, dynamicsInfo);
            droneObject.updateLatestData();
            return droneObject;
        } catch (Exception e) {
            Logs.error("Error occurred in Fetchers.processDroneData() : " + e.getMessage());
            return null;
        }
    }
}