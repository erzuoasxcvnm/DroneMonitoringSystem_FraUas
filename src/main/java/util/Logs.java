package main.java.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.*;

/**
 * The {@code Logs} class provides logging functionality for the application
 * using the Java Logging API. It allows logging messages at different severity levels,
 * including INFO, WARNING, ERROR, DEBUG, and CONFIG.
 *
 * <p>The class initializes logging to a specified log file and formats log messages
 * using a custom formatter that includes timestamp information.</p>
 *
 * <p>Libraries used in this class:</p>
 * <ul>
 *     <li>{@link SimpleDateFormat} - For formatting dates in log messages.</li>
 *     <li>{@link IOException} - For handling input/output exceptions during logging initialization.</li>
 *     <li>{@link Logger} - The Java Logging API for logging messages at different levels.</li>
 *     <li>{@link Date} - For working with date and time information in log messages.</li>
 *     <li>{@link File} - For working with file system operations, including creating log directories.</li>
 * </ul>
 */


public class Logs {
    private static final Logger LOGGER = Logger.getLogger(Logs.class.getName());

    public static void initialize(String logFileName) {
        if (logFileName == null) {
            throw new IllegalArgumentException("Log File Name must not be null");
        }

        try {
            File logFile = new File(logFileName);
            File logDirectory = logFile.getParentFile();
            if (!logDirectory.exists()) {
                if (logDirectory.mkdirs()) {
                    LOGGER.log(Level.INFO, "Log directory created successfully.");
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to create log directory. Check permissions.");
                    return;
                }
            }

            LOGGER.setLevel(Level.ALL);

            Handler fileHandler = new FileHandler(logFileName, true);
            fileHandler.setFormatter(new CustomFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (SecurityException e) {
            LOGGER.log(Level.SEVERE, "Permission denied for log file or directory: " + logFileName, e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error initializing logging for file: " + logFileName, e);
        }
    }

    /**
     * Logs an INFO-level message.
     *
     * @param message The message to be logged.
     */
    public static void info(String message) {
        LOGGER.info(message);
    }

    /**
     * Logs a WARNING-level message.
     *
     * @param message The message to be logged.
     */
    public static void warning(String message) {
        LOGGER.warning(message);
    }

    /**
     * Logs an ERROR-level message.
     *
     * @param message The message to be logged.
     */
    public static void error(String message) {
        LOGGER.severe(message);
    }

    /**
     * Logs a DEBUG-level message.
     *
     * @param message The message to be logged.
     */
    public static void debug(String message) {
        LOGGER.fine(message);
    }

    /**
     * Custom formatter class for formatting log messages with timestamp information.
     */
    private static class CustomFormatter extends Formatter {
        private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        @Override
        public String format(LogRecord record) {
            return String.format("[%s][%s] %s%n",
                    DATE_FORMAT.format(record.getInstant()),
                    record.getLevel(),
                    formatMessage(record));
        }
    }

    /**
     * Custom exception class for handling invalid arguments.
     */
    public static class ArgumentException extends IllegalArgumentException {
        public ArgumentException(String name) {
            super(name + " must not be null or empty");
        }
    }

    /**
     * Represents an exception that is thrown when a connection fails.
     */
    public static class ConnectionFailedException extends RuntimeException {
        public ConnectionFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}


