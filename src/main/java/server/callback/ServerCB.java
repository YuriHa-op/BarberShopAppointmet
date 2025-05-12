package server.callback;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerCB {
    private static Map<String, ClientCB> clientCallbacks = new ConcurrentHashMap<>();
    private static Map<String, AdminCB> adminCallbacks = new ConcurrentHashMap<>();

    // Client callback methods
    public static void registerClientCallback(String username, ClientCB callback) {
        clientCallbacks.put(username, callback);
        System.out.println("Registered client callback for: " + username);
    }

    public static void unregisterClientCallback(String username) {
        clientCallbacks.remove(username);
        System.out.println("Unregistered client callback for: " + username);
    }

    public static void notifyClient(String username, String message) throws RemoteException {
        if (clientCallbacks.containsKey(username)) {
            clientCallbacks.get(username).notifyClient(message);
            System.out.println("Notification sent to client: " + username);
        }
    }

    public static void notifyAllClients(String message) throws RemoteException {
        for (Map.Entry<String, ClientCB> entry : clientCallbacks.entrySet()) {
            try {
                entry.getValue().notifyClient(message);
            } catch (RemoteException e) {
                System.err.println("Error notifying client " + entry.getKey() + ": " + e.getMessage());
                // Remove stale callback
                clientCallbacks.remove(entry.getKey());
            }
        }
    }

    // Admin callback methods
    public static void registerAdminCallback(String username, AdminCB callback) {
        adminCallbacks.put(username, callback);
        System.out.println("Registered admin callback for: " + username);
    }

    public static void unregisterAdminCallback(String username) {
        adminCallbacks.remove(username);
        System.out.println("Unregistered admin callback for: " + username);
    }

    public static void notifyAdmin(String username, String message) throws RemoteException {
        if (adminCallbacks.containsKey(username)) {
            adminCallbacks.get(username).notifyAdmin(message);
            System.out.println("Notification sent to admin: " + username);
        }
    }

    public static void notifyAllAdmins(String message) throws RemoteException {
        for (Map.Entry<String, AdminCB> entry : adminCallbacks.entrySet()) {
            try {
                entry.getValue().notifyAdmin(message);
            } catch (RemoteException e) {
                System.err.println("Error notifying admin " + entry.getKey() + ": " + e.getMessage());
                // Remove stale callback
                adminCallbacks.remove(entry.getKey());
            }
        }
    }

    // Check if a client is registered
    public static boolean hasClientCallback(String username) {
        return clientCallbacks.containsKey(username);
    }

    // Check if an admin is registered
    public static boolean hasAdminCallback(String username) {
        return adminCallbacks.containsKey(username);
    }
}