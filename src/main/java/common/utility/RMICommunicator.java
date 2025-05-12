package common.utility;

import common.model.Request;
import common.model.Response;
import server.Interface.MainInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMICommunicator {
    private static RMICommunicator instance;
    private MainInterface remoteService;

    private RMICommunicator() {
        try {
            // Use the correct method name getServerIPAddress()
            String serverIP = NetworkUtil.getServerIPAddress();
            Registry registry = LocateRegistry.getRegistry(serverIP, 7770);
            remoteService = (MainInterface) registry.lookup("RemoteService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Remove the unused setServerIP method since we're using NetworkUtil's implementation
    public static synchronized RMICommunicator getInstance() {
        if (instance == null) {
            instance = new RMICommunicator();
        }
        return instance;
    }

    public MainInterface getRemoteService() {
        return remoteService;
    }

    public void init(String serverIP) throws Exception {
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, 7770);
            remoteService = (MainInterface) registry.lookup("RemoteService");

            // Test the connection
            try {
                // Simple ping operation to test connection
                remoteService.processRequest(new Request("ping", ""));
                System.out.println("Successfully connected to RMI server at " + serverIP);
            } catch (Exception e) {
                System.err.println("RMI connection test failed: " + e.getMessage());
                throw new Exception("Could not establish working connection with server", e);
            }
        } catch (Exception e) {
            throw new Exception("Failed to connect to RMI server: " + e.getMessage(), e);
        }
    }

    public Response processRequest(Request request) throws Exception {
        if (remoteService == null) {
            throw new IllegalStateException("RMI service not initialized. Call init() first.");
        }

        String operation = request.getOperation();
        String data = request.getData();

        try {
            switch (operation) {
                case "login":
                    return remoteService.handleLogin(data);
                case "register":
                    return remoteService.handleRegister(data);
                case "view":
                    return remoteService.handleViewAppointments();
                case "view_finished":
                    return remoteService.handleViewFinishedAppointments();
                case "view_cancelled":
                    return remoteService.handleViewCancelledAppointments();
                case "book":
                    return remoteService.handleBookAppointment(data);
                case "cancel":
                    return remoteService.handleCancelAppointment(data);
                case "create_schedule":
                    return remoteService.handleCreateSchedule(data);
                case "mark_finished":
                    return remoteService.handleMarkFinishedAppointment(data);
                case "delete_cancelled":
                    return remoteService.handleDeleteCancelledAppointment(data);
                default:
                    return new Response("error", "Unknown operation: " + operation, "");
            }
        } catch (Exception e) {
            throw new Exception("RMI communication error: " + e.getMessage(), e);
        }
    }


    public String sendRequest(String requestJSON) {
        try {
            Request request = JSONUtil.unmarshal(requestJSON, Request.class);
            Response response = processRequest(request);
            return JSONUtil.marshal(response, Response.class);
        } catch (Exception e) {
            try {
                Response errorResponse = new Response("error", "Error processing request: " + e.getMessage(), "");
                return JSONUtil.marshal(errorResponse, Response.class);
            } catch (Exception ex) {
                return "{\"status\":\"error\",\"message\":\"Fatal error: " + ex.getMessage() + "\"}";
            }
        }
    }
}