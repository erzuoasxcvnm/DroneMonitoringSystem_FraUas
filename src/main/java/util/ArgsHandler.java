package main.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The {@code Args} class provides functionality for parsing command-line arguments,
 * specifically handling configuration and log file paths.
 *
 * <p>This class allows users to provide custom paths for configuration and log files
 * through command-line arguments or defaults to predefined values if not provided.
 * It also performs checks for the existence of default configuration files and logs
 * appropriate messages accordingly.</p>
 * <ul>
 *     <li>{@link #parseArgs(String[])} : Loops through the argument and decides if user args or default</li>
 *     <li>{@link #createDefaultLogFile()}: Method designed to use the default config file</li>
 *     <li>{@link #assignDefaultConfigFile()}: Method designed create a log file if not --log in {$PWD}/logs/0.log. If file exist , Append new content</li>
 * </ul>
 * We used System.out.println because at this step the logger is not initialized and any error here will stop the program System.exit(1);
 */
public class ArgsHandler {
    private String configPath;
    private String logPath;

    public ArgsHandler(String[] args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        boolean defaultConfig = false;
        boolean defaultLog = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case Constants.CONFIG_ARG:
                    if (i + 1 < args.length) {
                        configPath = args[i + 1];
                        defaultConfig = true;
                        i++;
                    } else {
                        System.out.println("Missing argument for " + Constants.CONFIG_ARG);
                    }
                    break;

                case Constants.LOG_ARG:
                    if (i + 1 < args.length) {
                        logPath = args[i + 1];
                        defaultLog = true;
                        i++;
                    } else {
                        System.out.println("Missing argument for " + Constants.LOG_ARG);
                    }
                    break;

                default:
                    System.out.println("Unknown option: " + args[i]);
                    System.exit(1);
            }
        }
        if (!defaultLog) {
            createDefaultLogFile();
        }

        if (!defaultConfig) {
            assignDefaultConfigFile();
        }
    }

    private void assignDefaultConfigFile() {
        try {
            URL defaultConfigUrl = ArgsHandler.class.getResource("/config/defaultConfig.yaml");
            System.out.println("Default config URL: " + defaultConfigUrl);
            if (defaultConfigUrl != null) {
                Path tempDir = Files.createTempDirectory("config");
                Path tempConfigFile = tempDir.resolve("defaultConfig.yaml");
                try (InputStream inputStream = defaultConfigUrl.openStream()) {
                    Files.copy(inputStream, tempConfigFile);
                }
                configPath = tempConfigFile.toString();
                System.out.println("Using default config file: " + configPath);
            } else {
                System.out.println("Default config file not found in resources: /config/defaultConfig.yaml");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Error checking default config file: " + e.getMessage());
            System.exit(1);
        }
    }

    private void createDefaultLogFile() {
        try {
            String currentDir = System.getProperty("user.dir");
            Path logsDir = Paths.get(currentDir, "logs");
            if (!Files.exists(logsDir)) {
                Files.createDirectories(logsDir);
                System.out.println("Logs directory created: " + logsDir);
            }
            Path defaultLogFile = Paths.get(logsDir.toString(), "0.log");
            if (!Files.exists(defaultLogFile)) {
                Files.createFile(defaultLogFile);
                System.out.println("Default log file created: " + defaultLogFile);
            } else {
                System.out.println("Default log file already exists: " + defaultLogFile);
            }
            logPath = defaultLogFile.toString();
        } catch (IOException e) {
            System.out.println("Error creating default log file: " + e.getMessage());
            System.exit(1);
        }
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getLogPath() {
        return logPath;
    }
}