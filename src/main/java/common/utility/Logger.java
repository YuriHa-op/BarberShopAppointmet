package common.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "activity.log";

    public static void log(String user, String operation, String data) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logEntry = "[" + timestamp + "] User: " + user
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
}