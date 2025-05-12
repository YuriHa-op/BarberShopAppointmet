package common.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger utility specifically for admin operations.
 * Logs to both console and file.
 */
public class AdminLogger {
    private static final String LOG_FILE = "admin_activity.log";

    /**
     * Logs admin activity
     * @param username Admin username
     * @param operation Operation performed
     * @param data Additional information about the operation
     */
    public static void log(String username, String operation, String data) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logEntry = "[" + timestamp + "] Admin: " + username
                + ", Operation: " + operation
                + ", Data: " + data;

        // Print log to console
        System.out.println(logEntry);

        // Write log to file
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logEntry + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs appointment-related activities
     */
    public static void logAppointment(String username, String actionType, String appointmentId, String details) {
        log(username, "APPOINTMENT_" + actionType.toUpperCase(), "ID: " + appointmentId + ", " + details);
    }

    /**
     * Logs user management activities
     */
    public static void logUserAction(String adminUsername, String actionType, String targetUsername) {
        log(adminUsername, "USER_" + actionType.toUpperCase(), "Target: " + targetUsername);
    }

    /**
     * Logs system activities
     */
    public static void logSystem(String username, String systemAction) {
        log(username, "SYSTEM", systemAction);
    }
}