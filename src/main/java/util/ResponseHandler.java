package main.java.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * The {@code ResponseHandler} class is responsible for processing API responses related to drones.
 * It provides an interface for extracting relevant data from JSON responses and organizes it into a structured format.
 *
 * <p>The main methods provided by the ResponseHandler class include:
 * <ul>
 *   <li>{@link #handle(String, String)} - Processes the JSON API response based on the provided category and retrieves relevant data.
 *   <li>{@link #extractData(String, String[])} - Extracts specific keys from the JSON response and organizes them into a list of maps.
 *   <li>{@link #extractCount(String)} - Extracts the count information from the JSON response.
 * </ul></p>
 *
 * <p>This class utilizes the standard Java libraries, {@code org.json}, and a custom {@code Logs} class for error logging.
 * The returned data is organized in a {@code List<Map<String, Object>>} where each {@code Map} represents a discrete set of key-value data parsed from the JSON response.
 * It supports handling responses from different API categories. This category is specified in the {@link #handle(String, String)} method to direct the data processing.</p>
 *
 * <p>Note: This class handles {@code JSONException} that can be thrown during the JSON processing. Always ensure to test the processed data for validity.
 * </p>
 */
public class ResponseHandler {

    public static List<Map<String, Object>> handle(String category, String response) {
        return switch (category) {
            case Constants.DETAILS -> extractData(response, Constants.DRONE_KEYS);
            case Constants.TYPES -> extractData(response, Constants.DRONE_TYPE_KEYS);
            case Constants.DYNAMICS -> extractData(response, Constants.DRONE_DYNAMIC_KEYS);
            default -> new ArrayList<>();
        };
    }

    private static List<Map<String, Object>> extractData(String jsonResponse, String[] keys) {
        List<Map<String, Object>> dataList = Collections.synchronizedList(new ArrayList<>());

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray resultsArray = jsonObject.optJSONArray(Constants.RESULTS);

            if (resultsArray == null) {
                resultsArray = new JSONArray().put(jsonObject);
            }

            List<JSONObject> jsonList = new ArrayList<>();
            for(int i = 0; i < resultsArray.length(); i++) {
                jsonList.add(resultsArray.getJSONObject(i));
            }

            jsonList.parallelStream().forEach(obj -> {
                try {
                    Map<String, Object> data = new HashMap<>();
                    for (String key : keys) {
                        if (obj.has(key)) {
                            Object value = obj.get(key);
                            data.put(key, value);
                        }
                    }
                    dataList.add(data);
                } catch (JSONException e) {
                    Logs.error("Failed to parse JSON: " + e.getMessage());
                }
            });

        } catch (JSONException e) {
            Logs.error("Failed to parse JSON: " + e.getMessage());
        }
        return dataList;
    }

    public static int extractCount(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getInt(Constants.COUNT);
        } catch (Exception e) {
            Logs.error("Error occurred in ResponseProcessor.extractData() : " + e.getMessage());
            return 0;
        }
    }
}