package main.java.model;

import main.java.util.Constants;

import java.util.*;
import java.util.stream.Collectors;


/**
 * The {@code DroneBase} class represents a base model of a drone with details, type information, dynamics information, and the latest data.
 * It encapsulates information related to a drone, including its details, type, dynamic information, and the latest data.
 * <p>
 * Methods:
 * <ul>
 *     <li>{@link #updateLatestData() updateLatestData}:
 *         Updates the latest data of the drone based on its dynamics information.</li>
 *     <li>{@link #getDroneDetails() getDroneDetails}:
 *         Returns a map containing drone details.</li>
 *     <li>{@link #getDroneTypeInfo() getDroneTypeInfo}:
 *         Returns a map containing drone type information.</li>
 *     <li>{@link #getDroneDynamicsInfo() getDroneDynamicsInfo}:
 *         Returns a list of maps containing drone dynamics information.</li>
 *     <li>{@link #getDroneLatestData() getDroneLatestData}:
 *         Returns a map containing the latest drone data.</li>
 * </ul>
 * </p>
 */

public class DroneBase {
    private final Map<String, Object> droneDetails;
    private final Map<String, Object> droneTypeInfo;
    private final List<Map<String, Object>> droneDynamicsInfo;
    private Map<String, Object> droneLatestData;

    public DroneBase(Map<String, Object> droneDetails, Map<String, Object> droneTypeInfo, List<Map<String, Object>> droneDynamicsInfo) {
        this.droneDetails = droneDetails;
        this.droneTypeInfo = droneTypeInfo;
        this.droneDynamicsInfo = droneDynamicsInfo;
    }

    public void updateLatestData() {
        List<Map<String, Object>> dynamicsInfo = getDroneDynamicsInfo();
        if (!dynamicsInfo.isEmpty()) {
            dynamicsInfo.sort(Comparator.comparing(d -> (String) d.get(Constants.TIMESTAMP)));
            Collections.reverse(dynamicsInfo);
            this.droneLatestData = dynamicsInfo.get(0);
        }
    }

    /**
     * @return a Map containing drone details. Returns Empty Map if there are no details.
     */
    public Map<String, Object> getDroneDetails() {
        return droneDetails != null ? Collections.unmodifiableMap(droneDetails) : Collections.emptyMap();
    }

    /**
     * @return a Map containing drone type information. Returns null if there is no type information.
     */
    public Map<String, Object> getDroneTypeInfo() {
        return droneTypeInfo != null ? Collections.unmodifiableMap(droneTypeInfo) : Collections.emptyMap();
    }

    /**
     * @return List of Map objects containing drone dynamics.
     * Empty List if there is no dynamics.
     */
    public List<Map<String, Object>> getDroneDynamicsInfo() {
        return droneDynamicsInfo != null ? droneDynamicsInfo.stream().map(HashMap::new).collect(Collectors.toList()) : Collections.emptyList();
    }

    /**
     * @return Map containing the latest drone data. Empty Map if there is no data.
     */
    public Map<String, Object> getDroneLatestData() {
        return droneLatestData != null ? Collections.unmodifiableMap(droneLatestData) : Collections.emptyMap();
    }
}