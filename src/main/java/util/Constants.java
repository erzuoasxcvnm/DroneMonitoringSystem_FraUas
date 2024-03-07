package main.java.util;

public final class Constants {
    /**
     * The reason we want this class to be non-instantiable
     * is because utility classes should not have a state.
     * The constants defined in them are public static final,
     * meaning they belong to the class itself, not to instances
     * of the class.
     * private Constants() {} to avoid instantiating the class
     */

    // Drone Data (api/drones/):
    public static final String DRONE_ID = "id";
    public static final String DRONE_TYPE = "dronetype";
    public static final String CREATED = "created";
    public static final String SERIAL_NUMBER = "serialnumber";
    public static final String CARRIAGE_WEIGHT = "carriage_weight";
    public static final String CARRIAGE_TYPE = "carriage_type";
    public static final String[] DRONE_KEYS = {DRONE_ID, DRONE_TYPE, CREATED, SERIAL_NUMBER, CARRIAGE_WEIGHT, CARRIAGE_TYPE};

    // Drone Type (api/dronetypes/id/):
    public static final String TYPE_ID = "id";
    public static final String MANUFACTURER = "manufacturer";
    public static final String TYPE_NAME = "typename";
    public static final String WEIGHT = "weight";
    public static final String MAX_SPEED = "max_speed";
    public static final String BATTERY_CAPACITY = "battery_capacity";
    public static final String CONTROL_RANGE = "control_range";
    public static final String MAX_CARRIAGE = "max_carriage";
    public static final String[] DRONE_TYPE_KEYS = {TYPE_ID, MANUFACTURER, TYPE_NAME, WEIGHT, MAX_SPEED,
            BATTERY_CAPACITY, CONTROL_RANGE, MAX_CARRIAGE};

    // Drone Dynamics (api/dronedynamics/):
    public static final String DRONE_URL = "drone";
    public static final String TIMESTAMP = "timestamp";
    public static final String SPEED = "speed";
    public static final String ALIGN_ROLL = "align_roll";
    public static final String ALIGN_PITCH = "align_pitch";
    public static final String ALIGN_YAW = "align_yaw";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String BATTERY_STATUS = "battery_status";
    public static final String LAST_SEEN = "last_seen";
    public static final String STATUS = "status";
    public static final String[] DRONE_DYNAMIC_KEYS = {DRONE_URL, TIMESTAMP, SPEED, ALIGN_ROLL, ALIGN_PITCH, ALIGN_YAW,
            LONGITUDE, LATITUDE, BATTERY_STATUS, LAST_SEEN, STATUS};

    // Drone intern vars :
    public static final String STATUS_ON = "ON";
    public static final String STATUS_OF = "OF";
    public static final String STATUS_IS = "IS";

    // Used in ResponseHandler.java and ApiManager.java :
    public static final String DETAILS = "details";
    public static final String TYPES = "types";
    public static final String DYNAMICS = "dynamics";
    public static final String RESULTS = "results";
    public static final String COUNT = "count";

    // User in ArgsHandler.java
    public static final String CONFIG_ARG = "--config";
    public static final String LOG_ARG = "--log";

    private Constants() {
    }
}
