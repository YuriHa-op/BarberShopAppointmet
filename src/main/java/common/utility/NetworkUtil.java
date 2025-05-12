package common.utility;

import javax.swing.*;

public class NetworkUtil {
    private static String serverIP = null;

    public static String getServerIPAddress() {
        if (serverIP == null || serverIP.trim().isEmpty()) {
            serverIP = JOptionPane.showInputDialog(
                    null,
                    "Enter Server IP Address:",
                    "Server IP",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (serverIP == null || serverIP.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No IP entered. Exiting.");
                System.exit(0);
            }
        }
        return serverIP;
    }

    public static void initRMIConnection() {
        String ip = getServerIPAddress();
        try {
            System.out.println("Attempting to connect to RMI server at " + ip + "...");
            RMICommunicator.getInstance().init(ip);
            System.out.println("Successfully connected to RMI server");
        } catch (Exception e) {
            String errorMsg = "Failed to connect to RMI server at " + ip + "\n" +
                    "Error: " + e.getMessage() + "\n\n" +
                    "Please verify:\n" +
                    "1. Server is running\n" +
                    "2. IP address is correct\n" +
                    "3. Port 7770 is not blocked by firewall";

            JOptionPane.showMessageDialog(null, errorMsg,
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}