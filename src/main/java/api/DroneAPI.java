package main.java.api;

import main.java.util.Logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * The {@code DroneAPI} class provides an interface for establishing HTTP connection
 * and interacting with a drone API. It enables creating a connection, validating API token,
 * retrieving responses, and handling errors that may occur during these processes.
 *
 * <p>The main methods provided by the DroneAPI class include:
 * <ul>
 *   <li>{@link #DroneAPI(String, String, String, String)} - Constructor used to create a configured DroneAPI instance.
 *   <li>{@link #createConnection()} - Establishes an HTTP connection to the drone API.
 *   <li>{@link #retrieveResponse()} - Retrieves and processes the HTTP response from the connected API.
 * </ul></p>
 *
 * <p>Helper methods that support the functionalities of main methods:
 * <ul>
 *   <li>{@link #validateInput(String, String)} - Validates input data prior to establishing a connection to the drone API.
 *   <li>{@link #setConnectionProperties(HttpURLConnection)} - Sets properties for an established HTTP connection.
 *   <li>{@link #processResponse(HttpURLConnection)} - Handles the response from the connected API.
 *   <li>{@link #readResponse(HttpURLConnection)} - Reads the API response and converts it into a string.
 *   <li>{@link #handleException(Exception)} - Error handling during response retrieval and processing.
 * </ul></p>
 *
 * <p>Note: Always handle {@link main.java.util.Logs.ConnectionFailedException} that can be thrown from {@link #createConnection()} and {@link #retrieveResponse()}.
 * </p>
 *
 */

public class DroneAPI {
    private final String domain;
    private final String endpoint;
    private final String token;
    private final String agent;

    public DroneAPI(String domain, String endpoint, String token, String agent) {
        validateInput(domain, "domain");
        validateInput(token, "token");
        validateInput(agent, "agent");
        this.domain = domain;
        this.endpoint = endpoint;
        this.token = token;
        this.agent = agent;
    }

    private void validateInput(String input, String name) {
        if (input == null || input.isEmpty()) {
            throw new Logs.ArgumentException(name);
        }
    }

    public HttpURLConnection createConnection() {
        try {
            URL url = new URL(domain + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            setConnectionProperties(connection);
            return connection;
        } catch (Exception e) {
            Logs.error("Error occurred in DroneApi.createConnection(): " + e.getMessage());
            throw new Logs.ConnectionFailedException("Connection could not be established : ", e);
        }
    }

    private void setConnectionProperties(HttpURLConnection connection) throws ProtocolException {
        int TIMEOUT = 15000;
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);
        connection.setRequestProperty("Authorization", token);
        connection.setRequestProperty("User-Agent", agent);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestMethod("GET");
    }

    public String retrieveResponse() {
        HttpURLConnection connection = createConnection();
        try {
            if (connection != null) {
                return processResponse(connection);
            }
        } catch (Exception e) {
            handleException(e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private String processResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readResponse(connection);
        } else {
            Logs.error("The GET request encountered an error. Response Code: " + responseCode);
            return null;
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    private void handleException(Exception e) {
        Logs.error("Error occurred in DroneApi.retrieveResponse() : " + e.getMessage());
        throw new Logs.ConnectionFailedException("Could not retrieve API response", e);
    }
}
