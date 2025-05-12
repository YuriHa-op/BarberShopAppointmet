package common.utility;


public class ClientCommunicator {

    private static ClientCommunicator instance;
    private final RMICommunicator rmiCommunicator;

    private ClientCommunicator() {
        // Private constructor for singleton pattern
        this.rmiCommunicator = RMICommunicator.getInstance();
    }

    public static ClientCommunicator getInstance() {
        if (instance == null) {
            instance = new ClientCommunicator();
        }
        return instance;
    }

    /**
     * Sends a request to the server and returns the response using RMI
     *
     * @param requestJSON The JSON string to send to the server
     * @return The response from the server as a string
     */
    public String sendRequest(String requestJSON) {
        try {
            return rmiCommunicator.sendRequest(requestJSON);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\",\"message\":\"Communication error: " + e.getMessage() + "\"}";
        }
    }
}