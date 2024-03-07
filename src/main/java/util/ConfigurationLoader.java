package main.java.util;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * The {@code ConfigLoader} class provides functionality for loading and reading configuration
 * data from a YAML file. It supports reading values for specific sections and keys within the
 * configuration.
 *
 * <p>This class uses the SnakeYAML library for parsing YAML files.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     ConfigLoader configLoader = new ConfigLoader("path/to/config.yaml");
 *     String value = configLoader.read("sectionName", "keyName");
 *     String value = configLoader.read("sectionName", "SubSectionName.keyName");
 * </pre>
 */
public class ConfigurationLoader {
    private Map<String, Object> config;

    /**
     * Constructs an instance of the {@code ConfigLoader} class and loads the configuration
     * from the specified YAML file.
     *
     * @param filePath The path to the YAML configuration file.
     */
    public ConfigurationLoader(String filePath) {
        try {
            loadConfig(filePath);
        } catch (Exception e) {
            Logs.error("Error occurred in ConfigLoader constructor: " + e.getMessage());
        }
    }

    /**
     * Loads the configuration from the specified YAML file.
     *
     * @param filePath The path to the YAML configuration file.
     */
    private void loadConfig(String filePath) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            config = yaml.load(inputStream);
        } catch (Exception e) {
            Logs.error("Error occurred in ConfigLoader.loadConfig() : " + e.getMessage());
        }
    }

    /**
     * Reads a value from the configuration for the specified section and key.
     *
     * @param section The name of the section in the configuration.
     * @param key     The key within the specified section.
     * @return The value associated with the specified section and key, or null if not found.
     */
    public String read(String section, String key) {
        try {
            String[] keys = key.split("\\.");

            if (config != null && config.containsKey(section)) {
                Map<String, Object> currentSection = getMap(config.get(section));

                for (int i = 0; i < keys.length - 1; i++) {
                    if (currentSection != null && currentSection.containsKey(keys[i])) {
                        currentSection = getMap(currentSection.get(keys[i]));
                    } else {
                        return null;
                    }
                }

                String lastKey = keys[keys.length - 1];
                if (currentSection != null && currentSection.containsKey(lastKey)) {
                    return currentSection.get(lastKey).toString();
                }
            }
        } catch (Exception e) {
            Logs.error("Error occurred in ConfigLoader.read() : " + e.getMessage());
        }
        return null;
    }

    /**
     * Safely casts an object to a Map<String, Object>.
     *
     * @param obj The object to be cast.
     * @return The object cast to Map<String, Object>, or null if the object is not of the expected type.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        } else {
            return null;
        }
    }
}